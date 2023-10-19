package sample.core.balances;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sample.core.balances.entities.BalancesEntity;
import sample.core.balances.entities.BankAccountEntity;
import sample.oba.api.BalancesApi;
import sample.oba.api.dto.AccountListDto;
import sample.oba.api.dto.BalancesDto;
import sample.utils.NumericUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BalancesService {

    private static final Logger logger = LoggerFactory.getLogger(BalancesService.class);

    private BalancesApi balancesApi;

    public BalancesService(@NonNull BalancesApi balancesApi) {
        this.balancesApi = balancesApi;
    }

    public List<BankAccountEntity> getAccountListWithAmounts() {
        logger.debug("Gathering Bank Account entity list.");

        final AccountListDto apiResult = balancesApi.getAccountList();
        final List<BankAccountEntity> accounts = new ArrayList<>();

        for (AccountListDto.Account accountDto : apiResult.getData().getAccounts()) {
            final BankAccountEntity bankAccount = new BankAccountEntity();
            bankAccount.setAccountId(accountDto.getId());
            bankAccount.setDescription(accountDto.getDescription());
            bankAccount.setCurrency(accountDto.getCurrency());

            // sequential invoking remote API could be parallelized
            final List<BalancesEntity> balances = getBalances(accountDto.getId());
            final BigDecimal accountValue = calculateBalance(balances);
            bankAccount.setAmount(accountValue);
            bankAccount.setCreditDebitIndicator(accountValue.signum() < 0 ? "DEBIT" : "CREDIT");

            accounts.add(bankAccount);
        }

        return accounts;
    }

    private List<BalancesEntity> getBalances(final String accountId) {
        logger.debug("Gathering Balances for account {}", accountId);

        final BalancesDto apiResult = balancesApi.getBalances(accountId);
        final List<BalancesEntity> balances = new ArrayList<>();

        for (BalancesDto.Balance dto : apiResult.getData().getBalance()) {
            final BalancesEntity balance = new BalancesEntity();
            balance.setAccountId(dto.getAccountId());
            balance.setCurrency(dto.getAmount().getCurrency());
            balance.setAmount(NumericUtils.round(new BigDecimal(dto.getAmount().getAmount())));
            balance.setType(dto.getType());
            balance.setCreditDebitIndicator(dto.getCreditDebitIndicator());

            balances.add(balance);
        }
        return balances;
    }

    /**
     * method takes first credit value of first debit value it findes
     * ignoring the type
     *
     * @param balances list of balances for an account
     * @return balance of account to display
     */
    private BigDecimal calculateBalance(final List<BalancesEntity> balances) {
        if (balances == null || balances.isEmpty()) return BigDecimal.ZERO;
        var firstCredit = balances.stream()
                .filter(b -> "CREDIT".equals(b.getCreditDebitIndicator()))
                .findFirst();
        if (firstCredit.isEmpty()) {
            return balances.get(0).getAmount();
        } else {
            return firstCredit.get().getAmount();
        }
    }

}
