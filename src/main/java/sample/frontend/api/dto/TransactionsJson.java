package sample.frontend.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionsJson {

    private String accountBalanceDisplay;
    private String accountCurrencySymbol;

    private List<TransactionDetails> transactionDetails = new ArrayList<>();

    @Data
    public static class TransactionDetails {
        private String description;
        private String amountDisplay;
        private CreditDebitType amountType;
        private String balanceAmountDisplay;
        private CreditDebitType balanceAmountType;
    }

    public TransactionDetails createAndAddTransaction() {
        var json = new TransactionDetails();
        this.transactionDetails.add(json);
        return json;
    }
}
