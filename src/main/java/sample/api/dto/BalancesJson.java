package sample.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BalancesJson {
    private String balanceTotalDisplay;
    private String currencySymbol;

    private List<AccountBalanceJson> accountBalances = new ArrayList<>();

    @Data
    public static class AccountBalanceJson {
        private String accountId;
        private String accountName;
        private String balanceDisplay;
        private String currencySymbol;
    }

    public AccountBalanceJson addAccountBalance() {
        final AccountBalanceJson json = new AccountBalanceJson();
        this.accountBalances.add(json);
        return json;
    }

}
