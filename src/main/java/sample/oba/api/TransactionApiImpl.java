package sample.oba.api;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sample.authentication.UserContext;
import sample.oba.api.dto.BalancesDto;
import sample.oba.api.dto.TransactionDto;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TransactionApiImpl implements TransactionApi {
    private static final Logger logger = LoggerFactory.getLogger(TransactionApiImpl.class);

    private final RestTemplateBuilder restTemplateBuilder;
    private RestTemplate template;

    private final UserContext userContext;

    public TransactionApiImpl(@NonNull RestTemplateBuilder restTemplateBuilder,
                           @NonNull UserContext userContext) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.userContext = userContext;
    }

    @PostConstruct
    public void init() {
        logger.info("Running with TransactionApi: {}", this.getClass().getSimpleName());
        this.template = restTemplateBuilder.build();
    }

    @Override
    public TransactionDto getTransactionsOfAccount(@NonNull final String accountId,
                                                   final Instant bookingDate,
                                                   final int page) {
        logger.debug("Fetching account transactions for account {} and user {}",
                accountId,
                userContext.getUserId());

        final URI apiUri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ob.sandbox.natwest.com")
                .path("/open-banking/v3.1/aisp/accounts/{accountId}/transactions")
                .queryParam("toBookingDateTime", formatTimestamp(bookingDate))
                .queryParam("page", page)
                .buildAndExpand(accountId).toUri();

        final StopWatch watch = new StopWatch();
        watch.start();
        // missing error handling code, hard coded url
        final ResponseEntity<TransactionDto> response = template.exchange(apiUri, HttpMethod.GET,
                emptyBodyAndAuthHeaders(),
                TransactionDto.class);
        watch.stop();

        logger.debug("Fetched account transaction of user {} account {} in {} ms.",
                userContext.getUserId(),
                accountId,
                watch.getTotalTimeMillis());

        return response.getBody();
    }

    private HttpEntity<String> emptyBodyAndAuthHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userContext.getAuthorizationToken());

        return new HttpEntity<>(headers);
    }

    private String formatTimestamp(final Instant timestamp) {
        // ISO-8601
        return OffsetDateTime
                .ofInstant(Instant.now(), ZoneId.of("UTC"))
                .truncatedTo( ChronoUnit.SECONDS )
                .toString();
    }
}
