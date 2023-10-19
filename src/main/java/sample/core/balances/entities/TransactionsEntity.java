package sample.core.balances.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class TransactionsEntity {

    private String accountId;
    private int pageCountTotal;
    private List<TransactionDetailsEntity> transactionDetails = new ArrayList<>();

    @Data
    public static class TransactionDetailsEntity {
        private String transactionId;
        private Instant timestamp;
        private String description;
        private BigDecimal amount;
        private String type;
        private String currency;

        private BigDecimal balanceAmount;
        private String balanceType;
        private String balanceCurrency;

    }

    public TransactionDetailsEntity createAndAddTransactionDetailsEntity() {
        var transactionDetailsEntity = new TransactionDetailsEntity();
        this.transactionDetails.add(transactionDetailsEntity);
        return transactionDetailsEntity;
    }

    public void sortTransactions(final SortDirection direction) {
        if(this.transactionDetails==null) {
            return;
        }

        Comparator<TransactionDetailsEntity> comparator;
        if(direction == SortDirection.ASC) {
            // DESC, oldest first (1999) [0], newest last (2001) [size-1]
            comparator = Comparator.comparing(TransactionDetailsEntity::getTimestamp);
        } else {
            // DESC, newest first (2001) [0], oldest last (1999) [size-1]
            comparator = (a,b) -> (a.getTimestamp().compareTo(b.getTimestamp()))*-1;
        }
        this.transactionDetails.sort(comparator);
    }
}
