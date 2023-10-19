package sample.authentication;

import lombok.Data;


@Data
public class UserContext {
    private String userId;
    private String authorizationToken;
}
