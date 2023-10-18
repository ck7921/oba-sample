package experiments.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import experiments.dto.RiskJson;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class AccountRequestJson {

    @JsonProperty("Data")
    private AccountRequestDataJson data = new AccountRequestDataJson();

    @JsonProperty("Risk")
    private RiskJson risk = new RiskJson();

    @Data
    public static class AccountRequestDataJson {

        @JsonProperty("Permissions")
        private List<String> Permissions = new ArrayList<>();
    }

    public AccountRequestJson addPermissions(@NonNull final String... permissions) {
        this.data.getPermissions().addAll(Arrays.asList(permissions));
        return this;
    }

}
