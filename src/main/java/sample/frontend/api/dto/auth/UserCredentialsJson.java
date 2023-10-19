package sample.frontend.api.dto.auth;

import lombok.Data;

@Data
public class UserCredentialsJson {
    private String userName;
    private String password;
}
