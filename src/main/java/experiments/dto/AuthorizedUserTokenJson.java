package experiments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthorizedUserTokenJson {
    @JsonProperty("access_token")
    private String accessToken; // access_token
    @JsonProperty("refresh_token")
    private String refreshToken; // refresh_token
    @JsonProperty("id_token")
    private String idToken; // id_token
    @JsonProperty("token_type")
    private String tokenType; // token_type
    @JsonProperty("expires_in")
    private long expiresIn; // expires_in
    private String scope; // scope
}
