package sample.frontend.api.dto.auth;

import lombok.Data;

@Data
public class ConsentApprovalJson {
    private boolean success;
    private String errorMessage;
}
