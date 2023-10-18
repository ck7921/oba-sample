package experiments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import experiments.dto.AccessToken;
import experiments.dto.AccountRequestResponseJson;
import experiments.dto.AuthorizedUserTokenJson;
import experiments.dto.ConsentResponseJson;
import experiments.dto.request.AccountRequestJson;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * <p>
 * get to know the OBA API and play around with it.
 * just for experimental reason.
 * </p>
 * <strong>needs env var config to make it work:</strong>
 * <ul>
 *     <li>clientId</li>
 *     <li>clientSecret</li>
 *     <li>authorizationUserName</li>
 * </ul>
 */
public class ObaApiAccessMain {

    public static void main(String[] args) throws Exception {

        final RestTemplateBuilder builder = new RestTemplateBuilder();
        final RestTemplate temp = builder.build();

        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // auth token request
        // https://ob.sandbox.natwest.com/token

        final String clientId = System.getenv("clientId");
        final String clientSecret = System.getenv("clientSecret");
        final String authorizationUserName = System.getenv("authorizationUserName");

        System.out.printf("Configured client id: %s\n", clientId)
                .printf("Configured client secret: %s\n", clientSecret)
                .printf("Configured authorization UserName: %s\n", authorizationUserName);



        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("scope", "accounts");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        AccessToken accessToken = temp.postForObject("https://ob.sandbox.natwest.com/token", entity, AccessToken.class);

        System.out.println("AccessToken result: " + prettyJson(accessToken));

        // Account Request
        AccountRequestJson accountRequest = new AccountRequestJson();
        accountRequest.addPermissions("ReadAccountsDetail", "ReadBalances", "ReadTransactionsCredits",
                "ReadTransactionsDebits", "ReadTransactionsDetail");

        headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> accountRequestJson = new HttpEntity<>(mapper.writeValueAsString(accountRequest), headers);

        System.out.println("account request json: " + pretty(mapper.writeValueAsString(accountRequest)));

        AccountRequestResponseJson accountRequestResultJson = temp
                .postForObject("https://ob.sandbox.natwest.com/open-banking/v3.1/aisp/account-access-consents",
                        accountRequestJson, AccountRequestResponseJson.class);

        System.out.println("AccountRequestResultJson result: " + prettyJson(accountRequestResultJson));

        // Approve Consent programmatically
        // ------------------------------------------------------------------------------

        // {{authorizationEndpoint}}
        // ?client_id={{clientId}}
        // &response_type=code id_token
        // &scope=openid accounts
        // &redirect_uri={{encodedRedirectUrl}}
        // &state=ABC
        // &request={{consentId}}
        // &authorization_mode=AUTO_POSTMAN
        // &authorization_username={{psuUsername}}

        String uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.sandbox.natwest.com")
                .path("/authorize")
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code id_token")
                .queryParam("scope", "openid accounts")
                .queryParam("redirect_uri", "https://localhost:8080/goHere")
                .queryParam("state", "ABC")
                .queryParam("request", accountRequestResultJson.getData().getConsentId())
                .queryParam("authorization_mode", "AUTO_POSTMAN")
                .queryParam("authorization_username", authorizationUserName)
                .build()
                .toString();

        // ConsentResponseJson
        ConsentResponseJson redirectData = temp.getForEntity(uri.toString(), ConsentResponseJson.class).getBody();

        System.out.println("ConsentResponseJson result: " + prettyJson(redirectData));

        // Exchange Code for access token
        UriComponents uc = UriComponentsBuilder.fromUriString(redirectData.getRedirectUri()).build();
        UriComponents uc2 = UriComponentsBuilder.fromUriString("http://www.www?" + uc.getFragment()).build();

        final String authorizationCode = uc2.getQueryParams().get("code").get(0);
        System.out.println("obtained access code: " + authorizationCode);

        // request user access token
        MultiValueMap<String, String> uatRequestPayloadMap = new LinkedMultiValueMap<>();
        uatRequestPayloadMap.add("client_id", clientId);
        uatRequestPayloadMap.add("client_secret", clientSecret);
        uatRequestPayloadMap.add("redirect_uri", "https://localhost:8080/goHere");
        uatRequestPayloadMap.add("grant_type", "authorization_code");
        uatRequestPayloadMap.add("code", authorizationCode);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        entity = new HttpEntity<>(uatRequestPayloadMap, headers);

        System.out.println("Request for AuthorizedUserToken: " + uatRequestPayloadMap);

        AuthorizedUserTokenJson authorizedUserTokenJson = temp
                .postForObject("https://ob.sandbox.natwest.com/token", entity, AuthorizedUserTokenJson.class);

        System.out.println("AuthorizedUserTokenJson result: " + prettyJson(authorizedUserTokenJson));

        // list accounts
        // {{apiUrlPrefix}}/open-banking/v3.1/aisp/accounts

        URI apiUri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ob.sandbox.natwest.com")
                .path("/open-banking/v3.1/aisp/accounts")
                .build().toUri();

        headers = new HttpHeaders();
        headers.setBearerAuth(authorizedUserTokenJson.getAccessToken());

        entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = temp.exchange(apiUri, HttpMethod.GET, entity, String.class);

        System.out.println("list of accounts: " + pretty(response.getBody()));

        // balance
        // 413c94d6-bb9d-4678-af7d-fef1173a8c1c

        apiUri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ob.sandbox.natwest.com")
                // .path("/open-banking/v3.1/aisp/accounts/413c94d6-bb9d-4678-af7d-fef1173a8c1c/balances")
                // ca3acb52-34dc-4820-bf8a-d5207c078263
                .path("/open-banking/v3.1/aisp/accounts/ca3acb52-34dc-4820-bf8a-d5207c078263/balances")
                .build().toUri();

        response = temp.exchange(apiUri, HttpMethod.GET, entity, String.class);

        System.out.println("list of account balances: " + pretty(response.getBody()));

        // list transactions
        // {{apiUrlPrefix}}/open-banking/v3.1/aisp/accounts/{{accountIdUsedToRequestTransactions}}/transactions


        apiUri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ob.sandbox.natwest.com")
                .path("/open-banking/v3.1/aisp/accounts/413c94d6-bb9d-4678-af7d-fef1173a8c1c/transactions")
                .build().toUri();

        response = temp.exchange(apiUri, HttpMethod.GET, entity, String.class);

        System.out.println("list of transactions: " + pretty(response.getBody()));


    }

    private static String pretty(String json) {
        final ObjectMapper p = new ObjectMapper();
        try {
            return p.enable(SerializationFeature.INDENT_OUTPUT)
                    .writeValueAsString(p.readValue(json, Object.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String prettyJson(Object json) {

        try {
            return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
                    .writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
