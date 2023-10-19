package sample.oba.api.dummy;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import sample.oba.api.AuthApi;
import sample.oba.api.dto.AccessTokenDto;
import sample.oba.api.dto.AccountResponseDto;
import sample.oba.api.dto.AuthorizedUserTokenDto;

@Component
@ConditionalOnProperty(
        value="app.data.dev",
        havingValue = "true")
public class AuthDummyApiImpl implements AuthApi {

    private static final Logger logger = LoggerFactory.getLogger(AuthDummyApiImpl.class);

    @PostConstruct
    public void init() {
        logger.info("Running with DEVELOPMENT Api: {}", this.getClass().getSimpleName());
    }

    @Override
    public AccessTokenDto requestAccessToken() {
        var token = new AccessTokenDto();
        token.setAccessToken("123456");
        token.setExpiresIn("600");
        return token;
    }

    @Override
    public AccountResponseDto requestConsentId(@NonNull AccessTokenDto accessTokenDto) {
        var result = new AccountResponseDto();
        result.setData(new AccountResponseDto.AccountResponseDataDto());
        result.getData().setConsentId("consent-123-456-789");
        return result;
    }

    @Override
    public String approveConsent(@NonNull String consentId, @NonNull String authorizationUserName) {
        return "cons-code-123-456";
    }

    @Override
    public AuthorizedUserTokenDto requestUserAccessToken(@NonNull String authorizationCode) {
        var token = new AuthorizedUserTokenDto();
        token.setAccessToken("access-token-123-456");
        return token;
    }
}
