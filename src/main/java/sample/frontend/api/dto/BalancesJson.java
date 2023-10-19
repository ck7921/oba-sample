package sample.frontend.api.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class BalancesJson {

    private List<CurrencyTotalJson> currencyTotals  = new ArrayList<>();
    private List<AccountBalanceJson> accountBalances = new ArrayList<>();

    @Data
    public static class AccountBalanceJson {
        private String accountId;
        private String accountName;
        private String balanceDisplay;
        private String currencySymbol;
        private CreditDebitType amountType;
    }

    @Data
    public static class CurrencyTotalJson {
        private String balanceDisplay;
        private String currencySymbol;
        private CreditDebitType amountType;
    }

    public AccountBalanceJson createAndAdd() {
        final AccountBalanceJson json = new AccountBalanceJson();
        this.accountBalances.add(json);
        return json;
    }

    public BalancesJson addTotal(@NonNull final String currency,
                                 @NonNull final String balanceDisplay,
                                 @NonNull final CreditDebitType amountType) {
        var total = new CurrencyTotalJson();
        total.setCurrencySymbol(currency);
        total.setBalanceDisplay(balanceDisplay);
        total.setAmountType(amountType);
        this.currencyTotals.add(total);
        return this;
    }

}
