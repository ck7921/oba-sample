package sample.oba.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountListDto {

    @JsonProperty("Data")
    private Data data;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("Account")
        private List<Account> accounts;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        @JsonProperty("AccountId")
        private String id;
        @JsonProperty("Currency")
        private String currency;
        @JsonProperty("Description")
        private String description;
        @JsonProperty("Nickname")
        private String nickName;
        @JsonProperty("Account")
        private List<AccountDetails> account;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountDetails {
        @JsonProperty("Name")
        private String name;
        @JsonProperty("Identification")
        private String identification;
    }
}
