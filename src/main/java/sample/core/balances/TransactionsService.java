package sample.core.balances;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sample.core.balances.entities.SortDirection;
import sample.core.balances.entities.TransactionsEntity;
import sample.oba.api.TransactionApi;
import sample.oba.api.dto.TransactionDto;
import sample.utils.NumericUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

@Service
public class TransactionsService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionsService.class);

    private final TransactionApi transactionApi;

    public TransactionsService(@NonNull TransactionApi transactionApi) {
        this.transactionApi = transactionApi;
    }

    public TransactionsEntity getTransactions(final String accountId,
                                              final int page,
                                              final SortDirection sortDirection) {

        final Instant now = Instant.now();
        final Instant past = now.minusSeconds(60 * 60 * 24 * 30);

        // calculate which page depending on sort direction
        int totalPageCount = sortDirection == SortDirection.ASC ?
                getTotalPageCountOfAccount(accountId, now, past) : -1;

        int pageToLoad = sortDirection == SortDirection.DESC ? page : totalPageCount - page - 1;

        logger.debug("Transaction requested for account {} page {} total page count {}",
                accountId, pageToLoad, totalPageCount);

        final TransactionDto apiResult = transactionApi.getTransactionsOfAccount(accountId, past, now, pageToLoad);
        final TransactionsEntity entity = new TransactionsEntity();
        entity.setAccountId(accountId);
        entity.setPageCountTotal(Integer.parseInt(apiResult.getMeta().getTotalPages()));

        if (apiResult.getData().getTransactions() != null) {
            for (TransactionDto.Transaction transaction : apiResult.getData().getTransactions()) {
                var txnDetails = entity.createAndAddTransactionDetailsEntity();
                // txn meta data
                txnDetails.setTransactionId(transaction.getTransactionId());
                txnDetails.setDescription(transaction.getTransactionInformation());
                txnDetails.setTimestamp(parseTransactionTimestamp(transaction.getBookingDateTime()));

                // transaction
                txnDetails.setAmount(NumericUtils.round(new BigDecimal(transaction.getAmount().getAmount())));
                txnDetails.setCurrency(transaction.getAmount().getCurrency());
                txnDetails.setType("CREDIT".equalsIgnoreCase(transaction.getCreditDebitIndicator()) ?
                        "CREDIT" : "DEBIT");

                // balance
                txnDetails.setBalanceAmount(NumericUtils.round(
                        new BigDecimal(transaction.getBalance().getAmount().getAmount())));
                txnDetails.setBalanceType("CREDIT".equalsIgnoreCase(transaction.getBalance().getCreditDebitIndicator()) ?
                        "CREDIT" : "DEBIT");
                txnDetails.setBalanceCurrency(transaction.getBalance().getAmount().getCurrency());
            }
        }

        // revers, assuming api always returns newest (most recent txn) first
        if (sortDirection == SortDirection.ASC) {
            entity.sortTransactions(SortDirection.ASC);
        }

        return entity;
    }

    private int getTotalPageCountOfAccount(final String accountId,
                                           final Instant startBookingDate,
                                           final Instant bookingDate) {
        return Integer.parseInt(this.transactionApi
                .getTransactionsOfAccount(accountId, startBookingDate, bookingDate, 0)
                .getMeta().getTotalPages());
    }

    private Instant parseTransactionTimestamp(final String timestamp) {
        return OffsetDateTime.parse(timestamp).toInstant();
    }
}
