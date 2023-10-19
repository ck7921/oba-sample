package sample.authentication;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.RequestScope;


@Data
public class UserContext {
    private String userId;
    private String authorizationToken;
}
