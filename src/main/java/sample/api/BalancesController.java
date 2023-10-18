package sample.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.api.dto.BalancesJson;
import sample.api.dto.CreditDebitType;

@RestController
public class BalancesController {

    @RequestMapping("/api/balances")
    public BalancesJson getBalances() {
        var dto = new BalancesJson();
        dto.setBalanceTotalDisplay("100'234'567'890'123,00");
        dto.setCurrencySymbol("CHF");
        dto.setAmountType(CreditDebitType.CREDIT);

        var accountBalance = dto.addAccountBalance();
        accountBalance.setAccountId("123");
        accountBalance.setAccountName("UBS");
        accountBalance.setCurrencySymbol("USD");
        accountBalance.setBalanceDisplay("100'234'567'890'123,00");
        accountBalance.setAmountType(CreditDebitType.CREDIT);

        var accountBalance2 = dto.addAccountBalance();
        accountBalance2.setAccountId("456");
        accountBalance2.setAccountName("Credit Suisse");
        accountBalance2.setCurrencySymbol("CHF");
        accountBalance2.setBalanceDisplay("234'123'000'000'001,00");
        accountBalance2.setAmountType(CreditDebitType.DEBIT);

        var accountBalance3 = dto.addAccountBalance();
        accountBalance3.setAccountId("789");
        accountBalance3.setAccountName("LGT");
        accountBalance3.setCurrencySymbol("EUR");
        accountBalance3.setBalanceDisplay("999'888'777'666'555,44");
        accountBalance3.setAmountType(CreditDebitType.CREDIT);

        return dto;
    }



}
