package sample.oba.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalancesDto {

    @JsonProperty("Data")
    private Data data;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("Balance")
        private List<Balance> balance;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Balance {
        @JsonProperty("AccountId")
        private String accountId;
        @JsonProperty("CreditDebitIndicator")
        private String creditDebitIndicator;
        @JsonProperty("Type")
        private String type;
        @JsonProperty("DateTime")
        private String dateTime;
        @JsonProperty("Amount")
        private Amount amount;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amount {
        @JsonProperty("Amount")
        private String amount;
        @JsonProperty("Currency")
        private String currency;
    }
}
