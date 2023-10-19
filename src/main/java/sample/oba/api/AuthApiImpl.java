package sample.oba.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import sample.configuration.ObaApiConfig;
import sample.oba.api.dto.*;

@Component
@ConditionalOnProperty(
        value="app.data.dev",
        havingValue = "false", matchIfMissing = true)
public class AuthApiImpl implements AuthApi {

    private static final Logger logger = LoggerFactory.getLogger(AuthApiImpl.class);

    private final @NonNull ObaApiConfig obaApiConfig;

    private final @NonNull RestTemplateBuilder restTemplateBuilder;
    private RestTemplate template;
    private final ObjectMapper mapper;

    @Value("${app.data.dev}")
    private String xxx;

    public AuthApiImpl(@NonNull RestTemplateBuilder restTemplateBuilder,
                       @NonNull ObaApiConfig obaApiConfig) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.obaApiConfig = obaApiConfig;
        this.mapper = new ObjectMapper();
    }

    @PostConstruct
    public void init() {
        logger.info("Running with AuthApi: {}", this.getClass().getSimpleName());
        logger.info("this is " + xxx);

        this.template = restTemplateBuilder.build();
        this.mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public AccessTokenDto requestAccessToken() {
        logger.debug("Requesting access token from NatWest...");

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", obaApiConfig.getClientId());
        body.add("client_secret", obaApiConfig.getClientSecret());
        body.add("scope", "accounts");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        final StopWatch watch = new StopWatch();
        watch.start();
        // missing error handling code, hard coded url
        final AccessTokenDto token = template
                .postForObject("https://ob.sandbox.natwest.com/token", entity, AccessTokenDto.class);
        watch.stop();

        logger.debug("Access token request completed in {} ms.", watch.getTotalTimeMillis());
        return token;
    }

    @Override
    public AccountResponseDto requestConsentId(@NonNull final AccessTokenDto accessTokenDto) {
        logger.debug("Requesting consent id from NatWest...");

        final AccountRequestDto accountRequest = new AccountRequestDto();
        // hard coded list, should be determined by service
        accountRequest.addPermissions("ReadAccountsDetail", "ReadBalances", "ReadTransactionsCredits",
                "ReadTransactionsDebits", "ReadTransactionsDetail");

        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessTokenDto.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> accountRequestJson;
        try {
            accountRequestJson = new HttpEntity<>(
                    mapper.writeValueAsString(accountRequest), headers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        final StopWatch watch = new StopWatch();
        // missing error handling code, hard coded url
        final AccountResponseDto consentIdDto = template
                .postForObject("https://ob.sandbox.natwest.com/open-banking/v3.1/aisp/account-access-consents",
                        accountRequestJson, AccountResponseDto.class);
        watch.stop();

        logger.debug("Consent id request completed in {} ms.", watch.getTotalTimeMillis());
        return consentIdDto;
    }

    /**
     * this only work's in development mode.
     * in production for security reason this is not possible.
     */
    @Override
    public String approveConsent(@NonNull final String consentId,
                                 @NonNull final String authorizationUserName) {
        logger.info("DEVELOPMENT procedure: consent approval requested.");

        String uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.sandbox.natwest.com")
                .path("/authorize")
                .queryParam("client_id", obaApiConfig.getClientId())
                .queryParam("response_type", "code id_token")
                .queryParam("scope", "openid accounts")
                .queryParam("redirect_uri", "https://localhost:8080/goHere")
                .queryParam("state", "ABC")
                .queryParam("request", consentId)
                .queryParam("authorization_mode", "AUTO_POSTMAN")
                .queryParam("authorization_username", authorizationUserName)
                .build()
                .toString();

        final StopWatch watch = new StopWatch();
        final ConsentResponseDto redirectData = template
                .getForEntity(uri, ConsentResponseDto.class).getBody();
        watch.stop();

        // dirty parsing of auth code
        UriComponents uc = UriComponentsBuilder.fromUriString(redirectData.getRedirectUri()).build();
        UriComponents uc2 = UriComponentsBuilder.fromUriString("http://www.www?" + uc.getFragment()).build();
        final String authorizationCode = uc2.getQueryParams().get("code").get(0);

        logger.info("DEVELOPMENT procedure: redirect uri {} and authorization code {} received in {} ms.",
                redirectData.getRedirectUri(),
                authorizationCode,
                watch.getTotalTimeMillis());

        return authorizationCode;
    }

    @Override
    public AuthorizedUserTokenDto requestUserAccessToken(@NonNull final String authorizationCode) {
        logger.debug("Requesting user access token from NatWest...");

        // request user access token
        MultiValueMap<String, String> uatRequestPayloadMap = new LinkedMultiValueMap<>();
        uatRequestPayloadMap.add("client_id", obaApiConfig.getClientId());
        uatRequestPayloadMap.add("client_secret", obaApiConfig.getClientSecret());
        uatRequestPayloadMap.add("redirect_uri", "https://localhost:8080/goHere");
        uatRequestPayloadMap.add("grant_type", "authorization_code");
        uatRequestPayloadMap.add("code", authorizationCode);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(uatRequestPayloadMap, headers);

        final StopWatch watch = new StopWatch();
        // missing error handling code, hard coded url
        final AuthorizedUserTokenDto token = template.postForObject("https://ob.sandbox.natwest.com/token",
                entity, AuthorizedUserTokenDto.class);
        watch.stop();

        logger.debug("User access token {} received after {} ms.",
                token.getAccessToken(),
                watch.getTotalTimeMillis());

        return token;
    }

}
