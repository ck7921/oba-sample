package sample.core.balances.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalancesEntity {
    private String accountId;
    private String creditDebitIndicator;
    private String type;
    private String currency;
    private BigDecimal amount;
}
