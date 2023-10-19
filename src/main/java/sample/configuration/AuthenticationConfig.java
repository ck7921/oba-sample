package sample.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
import sample.authentication.UserContext;

@Configuration
public class AuthenticationConfig {

    @Bean
    @SessionScope
    public UserContext createUserContext() {
        return new UserContext();
    }

}
