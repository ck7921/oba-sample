package sample.frontend.api;

import org.springframework.web.bind.annotation.*;
import sample.frontend.api.dto.CreditDebitType;
import sample.frontend.api.dto.TransactionsJson;

@RestController
public class TransactionController {

    @RequestMapping(value = "/api/transactions/{accountId}", method = RequestMethod.GET)
    public TransactionsJson getTransactions(@PathVariable("accountId") final String accountId,
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
