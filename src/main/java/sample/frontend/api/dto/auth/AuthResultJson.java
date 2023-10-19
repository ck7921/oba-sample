package sample.frontend.api.dto.auth;

import lombok.Data;

@Data
public class AuthResultJson {
    private boolean success;
    private String userName;
    private String errorMessage;
    private String consentId;
}
