package sample.frontend.api;

import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.core.balances.BalancesService;
import sample.core.balances.entities.BankAccountEntity;
import sample.frontend.api.dto.BalancesJson;
import sample.frontend.api.dto.CreditDebitType;
import sample.utils.NumericUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BalancesController {

    private BalancesService balancesService;

    public BalancesController(@NonNull BalancesService balancesService) {
        this.balancesService = balancesService;
    }


    @RequestMapping("/api/balances")
    public BalancesJson getBalances() {
        final BalancesJson result = new BalancesJson();

        final List<BankAccountEntity> accounts = balancesService.getAccountListWithAmounts();

        final Map<String, BigDecimal> totalsPerCurrency = accounts
                .stream()
                .collect(Collectors.groupingBy(BankAccountEntity::getCurrency,
                        Collectors.mapping(BankAccountEntity::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        totalsPerCurrency.forEach((key, value) ->
                result.addTotal(key,
                        NumericUtils.currencyFormatted(value),
                        value.signum() < 0 ? CreditDebitType.DEBIT : CreditDebitType.CREDIT));

        for (final BankAccountEntity account : accounts) {
            var balance = result.createAndAdd();
            balance.setAccountId(account.getAccountId());
            balance.setAccountName(account.getDescription());
            balance.setCurrencySymbol(account.getCurrency());
            balance.setBalanceDisplay(NumericUtils.currencyFormatted(account.getAmount()));
            balance.setAmountType(account.getAmount().signum() < 0 ? CreditDebitType.DEBIT : CreditDebitType.CREDIT);
        }

        return result;
    }

   // @RequestMapping("/api/balances")
    public BalancesJson getBalancesDummy() {
        var dto = new BalancesJson();
        dto.addTotal("CHF",
                "100'234'567'890'123,00",
                CreditDebitType.CREDIT);
        dto.addTotal("USD",
                "567'890'123,00",
                CreditDebitType.DEBIT);
        dto.addTotal("EUR",
                "123,00",
                CreditDebitType.DEBIT);



        var accountBalance = dto.createAndAdd();
        accountBalance.setAccountId("123");
        accountBalance.setAccountName("UBS");
        accountBalance.setCurrencySymbol("USD");
        accountBalance.setBalanceDisplay("100'234'567'890'123,00");
        accountBalance.setAmountType(CreditDebitType.CREDIT);

        var accountBalance2 = dto.createAndAdd();
        accountBalance2.setAccountId("456");
        accountBalance2.setAccountName("Credit Suisse");
        accountBalance2.setCurrencySymbol("CHF");
        accountBalance2.setBalanceDisplay("234'123'000'000'001,00");
        accountBalance2.setAmountType(CreditDebitType.DEBIT);

        var accountBalance3 = dto.createAndAdd();
        accountBalance3.setAccountId("789");
        accountBalance3.setAccountName("LGT");
        accountBalance3.setCurrencySymbol("EUR");
        accountBalance3.setBalanceDisplay("999'888'777'666'555,44");
        accountBalance3.setAmountType(CreditDebitType.CREDIT);

        return dto;
    }


}
