package sample.oba.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import experiments.dto.request.AccountRequestJson;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class AccountRequestDto {

    @JsonProperty("Data")
    private AccountRequestJson.AccountRequestDataJson data = new AccountRequestJson.AccountRequestDataJson();

    @JsonProperty("Risk")
    private RiskDto risk = new RiskDto();

    public AccountRequestDto addPermissions(@NonNull final String... permissions) {
        this.data.getPermissions().addAll(Arrays.asList(permissions));
        return this;
    }

    @Data
    public static class AccountRequestDataJson {

        @JsonProperty("Permissions")
        private List<String> Permissions = new ArrayList<>();
    }

}
