package experiments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
// @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessToken {

    private String accessToken;
    private String tokenType;
    private String expiresIn;
    private String scope;

}
