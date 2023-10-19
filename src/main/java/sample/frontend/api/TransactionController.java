package sample.frontend.api;

import lombok.NonNull;
import org.springframework.web.bind.annotation.*;
import sample.core.balances.BalancesService;
import sample.core.balances.TransactionsService;
import sample.core.balances.entities.BankAccountEntity;
import sample.core.balances.entities.SortDirection;
import sample.core.balances.entities.TransactionsEntity;
import sample.frontend.api.dto.CreditDebitType;
import sample.frontend.api.dto.TransactionsJson;
import sample.utils.NumericUtils;
import sample.utils.TimestampHelper;

@RestController
public class TransactionController {

    private final TransactionsService transactionsService;
    private final BalancesService balancesService;

    public TransactionController(@NonNull TransactionsService transactionsService,
                                 @NonNull BalancesService balancesService) {
        this.transactionsService = transactionsService;
        this.balancesService = balancesService;
    }

    @RequestMapping(value = "/api/transactions/{accountId}", method = RequestMethod.GET)
    public TransactionsJson getTransactions(@PathVariable("accountId") final String accountId,
                                            @RequestParam(value = "page", defaultValue = "0") Integer page,
                                            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {

        // total of account
        // can be optimized to not load all
        final BankAccountEntity bankAccountEntity = balancesService.getAccountListWithAmounts()
                .stream()
                .filter(account -> accountId.equals(account.getAccountId()))
                .findFirst()
                .orElse(null);

        if(bankAccountEntity==null) {
            throw new IllegalArgumentException("something is wrong with the bank account id");
        }

        final TransactionsEntity transactionEntity = transactionsService
                .getTransactions(accountId, page, "asc".equals(sortDirection)
                        ? SortDirection.ASC : SortDirection.DESC);

        final TransactionsJson json = new TransactionsJson();

        // navigation
        json.setTotalPageCount(transactionEntity.getPageCountTotal());
        json.setCurrentPageNumber(page);

        // summary of account
        json.setAccountBalanceDisplay(NumericUtils.currencyFormatted(bankAccountEntity.getAmount()));
        json.setAccountAmountType("CREDIT".equals(bankAccountEntity.getCreditDebitIndicator())
                ? CreditDebitType.CREDIT : CreditDebitType.DEBIT);
        json.setAccountCurrencySymbol(bankAccountEntity.getCurrency());

        // transactions
        for (TransactionsEntity.TransactionDetailsEntity detail : transactionEntity.getTransactionDetails()) {
            var dto = json.createAndAddTransaction();

            dto.setDescription(detail.getDescription());
            dto.setTimestampDisplay(TimestampHelper.formatInstantToDateOnly(detail.getTimestamp()));
            // transaction amount
            dto.setAmountDisplay(NumericUtils.currencyFormatted(detail.getAmount()));
            dto.setAmountType("CREDIT".equals(detail.getType())
                    ? CreditDebitType.CREDIT : CreditDebitType.DEBIT);
            dto.setAmountCurrencySymbol(detail.getCurrency());
            // balance
            dto.setBalanceAmountDisplay(NumericUtils.currencyFormatted(detail.getBalanceAmount()));
            dto.setBalanceAmountType("CREDIT".equals(detail.getBalanceType())
                    ? CreditDebitType.CREDIT : CreditDebitType.DEBIT);
            dto.setBalanceCurrencySymbol(detail.getBalanceCurrency());

            // already added to json
        }

        return json;
    }

    // @RequestMapping(value = "/api/transactions/{accountId}", method = RequestMethod.GET)
    public TransactionsJson getTransactionsDummy(@PathVariable("accountId") final String accountId,
                                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                 @RequestParam(value = "sort", defaultValue = "desc") String sortDirection) {

        final TransactionsJson json = new TransactionsJson();

        json.setAccountBalanceDisplay("1'456'678.00");
        json.setAccountCurrencySymbol("CHF");
        json.setAccountAmountType(CreditDebitType.DEBIT);
        json.setTotalPageCount(1);
        json.setCurrentPageNumber(0);

        // add some txn
        var txn = json.createAndAddTransaction();
        txn.setDescription("test txn");
        txn.setTimestampDisplay("2023-01-01");
        // txn
        txn.setAmountDisplay("123'456,00");
        txn.setAmountCurrencySymbol("CHF");
        txn.setAmountType(CreditDebitType.DEBIT);
        // balance
        txn.setBalanceAmountDisplay("123,00");
        txn.setBalanceAmountType(CreditDebitType.CREDIT);
        txn.setBalanceCurrencySymbol("CHF");

        txn = json.createAndAddTransaction();
        txn.setDescription("test txn 2");
        txn.setTimestampDisplay("2022-01-05");
        // txn
        txn.setAmountDisplay("1'456,00");
        txn.setAmountCurrencySymbol("USD");
        txn.setAmountType(CreditDebitType.CREDIT);
        // balance
        txn.setBalanceAmountDisplay("123'123,00");
        txn.setBalanceAmountType(CreditDebitType.CREDIT);
        txn.setBalanceCurrencySymbol("CHF");

        return json;
    }
}
