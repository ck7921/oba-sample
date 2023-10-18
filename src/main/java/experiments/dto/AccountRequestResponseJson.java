package experiments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountRequestResponseJson {

    @JsonProperty("Data")
    private AccountRequestDataResponseJson data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountRequestDataResponseJson {
        @JsonProperty("ConsentId")
        private String consentId;
        @JsonProperty("Status")
        private String status;
        @JsonProperty("CreationDateTime")
        private String creationDateTime;
        @JsonProperty("StatusUpdateDateTime")
        private String statusUpdateDateTime;
        @JsonProperty("Permissions")
        private List<String> permissions;
    }

}
