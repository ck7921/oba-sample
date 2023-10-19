package sample.configuration;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ObaApiConfig {

    private static final Logger logger = LoggerFactory.getLogger(ObaApiConfig.class);

    private String clientId;
    private String clientSecret;

    public ObaApiConfig() {
        // avoid credentials on github, using env vars instead of application config file
        // could be taken from secret source instead
        this.clientId = System.getenv("clientId");
        this.clientSecret = System.getenv("clientSecret");
    }

    @PostConstruct
    public void init() {
        logger.debug("Using OBA client id {}", clientId); // logging without masking
        logger.debug("Using OBA client secret {}", clientSecret); // logging without masking
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
