package sample.oba.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {

    @JsonProperty("Data")
    private Data data;

    @JsonProperty("Meta")
    private Meta meta;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("Transaction")
        private List<Transaction> transactions;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transaction {

        @JsonProperty("TransactionId")
        private String transactionId;
        @JsonProperty("TransactionInformation")
        private String transactionInformation;
        @JsonProperty("CreditDebitIndicator")
        private String creditDebitIndicator;
        @JsonProperty("BookingDateTime")
        private String bookingDateTime;
        @JsonProperty("Amount")
        private Amount amount;
        @JsonProperty("Balance")
        private Balance balance;

    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amount {
        @JsonProperty("Amount")
        private String amount;
        @JsonProperty("Currency")
        private String currency;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Balance {
        @JsonProperty("CreditDebitIndicator")
        private String creditDebitIndicator;
        @JsonProperty("Amount")
        private Amount amount;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        @JsonProperty("TotalPages")
        private String totalPages;
    }
}
