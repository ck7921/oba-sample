package sample.oba.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import experiments.dto.AccountRequestResponseJson;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponseDto {

    @JsonProperty("Data")
    private AccountResponseDataDto data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountResponseDataDto {
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
