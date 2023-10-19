package sample.oba.api;

import lombok.NonNull;
import sample.oba.api.dto.TransactionDto;

import java.time.Instant;

public interface TransactionApi {
    TransactionDto getTransactionsOfAccount(@NonNull String accountId,
                                            Instant bookingDate,
                                            int page);
}
