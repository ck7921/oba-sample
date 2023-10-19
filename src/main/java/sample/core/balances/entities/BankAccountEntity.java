package sample.core.balances.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountEntity {
    private String accountId;
    private String description;
    private String creditDebitIndicator;
    private String type;
    private String currency;
    private BigDecimal amount;
}
