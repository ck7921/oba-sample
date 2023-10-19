package sample.frontend.api.dto.auth;

import lombok.Data;
import lombok.NonNull;

@Data
public class GenericActionResultJson {
    private boolean success;
    private String errorMessage;

    public static GenericActionResultJson success() {
        var json = new GenericActionResultJson();
        json.setSuccess(true);
        return json;
    }

    /*
    public static GenericActionResultJson failure(@NonNull final String errorMessage) {
        var json = new GenericActionResultJson();
        json.setSuccess(false);
        json.setErrorMessage(errorMessage);
        return json;
    }
    */
}
