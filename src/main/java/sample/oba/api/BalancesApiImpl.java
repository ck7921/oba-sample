package sample.oba.api;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sample.authentication.UserContext;
import sample.oba.api.dto.AccountListDto;
import sample.oba.api.dto.BalancesDto;

import java.net.URI;

@Component
@ConditionalOnProperty(
        value = "app.data.dev",
        havingValue = "false", matchIfMissing = true)
public class BalancesApiImpl implements BalancesApi {

    private static final Logger logger = LoggerFactory.getLogger(BalancesApiImpl.class);

    private final RestTemplateBuilder restTemplateBuilder;
    private RestTemplate template;

    private final UserContext userContext;

    public BalancesApiImpl(@NonNull RestTemplateBuilder restTemplateBuilder,
                           @NonNull UserContext userContext) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.userContext = userContext;
    }

    @PostConstruct
    public void init() {
        logger.info("Running with BalancesApi: {}", this.getClass().getSimpleName());
        this.template = restTemplateBuilder.build();
    }

    @Override
    public AccountListDto getAccountList() {
        logger.debug("Fetching account list for user {}", userContext.getUserId());

        final URI apiUri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ob.sandbox.natwest.com")
                .path("/open-banking/v3.1/aisp/accounts")
                .build().toUri();

        final StopWatch watch = new StopWatch();
        watch.start();
        // missing error handling code, hard coded url
        final ResponseEntity<AccountListDto> response = template.exchange(apiUri, HttpMethod.GET,
                emptyBodyAndAuthHeaders(),
                AccountListDto.class);
        watch.stop();

        logger.debug("Fetched account list of user {} in {} ms.",
                userContext.getUserId(),
                watch.getTotalTimeMillis());

        return response.getBody();
    }

    @Override
    public BalancesDto getBalances(@NonNull final String accountId) {
        logger.debug("Fetching account balances for account {} and user {}",
                accountId,
                userContext.getUserId());

        final URI apiUri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ob.sandbox.natwest.com")
                .path("/open-banking/v3.1/aisp/accounts/{accountId}/balances")
                .buildAndExpand(accountId).toUri();

        final StopWatch watch = new StopWatch();
        watch.start();
        // missing error handling code, hard coded url
        final ResponseEntity<BalancesDto> response = template.exchange(apiUri, HttpMethod.GET,
                emptyBodyAndAuthHeaders(),
                BalancesDto.class);
        watch.stop();

        logger.debug("Fetched account balances of user {} in {} ms.",
                userContext.getUserId(),
                watch.getTotalTimeMillis());

        return response.getBody();
    }

    private HttpEntity<String> emptyBodyAndAuthHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userContext.getAuthorizationToken());

        return new HttpEntity<>(headers);
    }
}
