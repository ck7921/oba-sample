package sample.oba.api;

import lombok.NonNull;
import sample.oba.api.dto.AccessTokenDto;
import sample.oba.api.dto.AccountResponseDto;
import sample.oba.api.dto.AuthorizedUserTokenDto;

public interface AuthApi {
    AccessTokenDto requestAccessToken();

    AccountResponseDto requestConsentId(@NonNull AccessTokenDto accessTokenDto);

    String approveConsent(@NonNull String consentId,
                          @NonNull String authorizationUserName);

    AuthorizedUserTokenDto requestUserAccessToken(@NonNull String authorizationCode);
}
