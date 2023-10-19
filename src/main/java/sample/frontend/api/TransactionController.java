package sample.frontend.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sample.frontend.api.dto.CreditDebitType;
import sample.frontend.api.dto.TransactionsJson;

public class TransactionController {

    @RequestMapping(value = "/api/transactions/{accountId}", method = RequestMethod.GET)
    public TransactionsJson getTransactions(@PathVariable("accountId") final String accountId,
                                            @RequestParam("page") int page,
                                            @RequestParam("sort") int sortDirection) {

        final TransactionsJson json = new TransactionsJson();

        json.setAccountBalanceDisplay("1'456'678.00");
        json.setAccountCurrencySymbol("CHF");

        var txn = json.createAndAddTransaction();
        txn.setDescription("test txn");
        txn.setAmountDisplay("123'456,00");
        txn.setAmountType(CreditDebitType.DEBIT);
        txn.setBalanceAmountDisplay("123,00");
        txn.setBalanceAmountType(CreditDebitType.CREDIT);

        return json;
    }
}
