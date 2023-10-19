package sample.oba.api;

import lombok.NonNull;
import sample.oba.api.dto.AccountListDto;
import sample.oba.api.dto.BalancesDto;

public interface BalancesApi {
    AccountListDto getAccountList();

    BalancesDto getBalances(@NonNull String accountId);
}
