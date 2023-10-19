package sample.frontend.api;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;
import sample.authentication.UserContext;
import sample.frontend.api.dto.auth.AuthResultJson;
import sample.frontend.api.dto.auth.ConsentApprovalJson;
import sample.frontend.api.dto.auth.GenericActionResultJson;
import sample.frontend.api.dto.auth.UserCredentialsJson;
import sample.oba.api.AuthApi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this is a dummy authentication controller.
 * auth not handled with spring security in this sample project
 */
@RestController
public class DummyAuthenticationController {

    private Map<String, String> userDetails;

    private final AuthApi authApiImpl;

    private final UserContext userContext;

    public DummyAuthenticationController(@NonNull AuthApi authApiImpl, UserContext userContext) {
        this.authApiImpl = authApiImpl;
        this.userContext = userContext;
    }

    @PostConstruct
    public void init() {
        this.userDetails = new ConcurrentHashMap<>(
                Map.of("simple.user", System.getenv("authorizationUserName"),
                        "sample.user", System.getenv("authorizationUserName")));
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public AuthResultJson requestConsent(@RequestBody UserCredentialsJson userCredentials) {
        final AuthResultJson result = new AuthResultJson();
        if (userDetails.containsKey(userCredentials.getUserName()) &&
                "123456".equals(userCredentials.getPassword())) {

            try {
                var token = authApiImpl.requestAccessToken();
                var consentId = authApiImpl.requestConsentId(token);

                // this would be the redirect url, in this sample just the plain consent id
                result.setConsentId(consentId.getData().getConsentId());
                result.setUserName(userCredentials.getUserName());
                result.setSuccess(true);

                userContext.setUserId(userDetails.get(userCredentials.getUserName()));
                userContext.setAuthorizationToken(null);
            } catch (final Exception e) { // no catch-all in prod systems
                result.setErrorMessage(e.getMessage());
                result.setSuccess(false);
            }

        } else {
            result.setErrorMessage("Invalid password. The password is '123456'");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/approveConsent/{consentId}", method = RequestMethod.GET)
    public ConsentApprovalJson consentIdApproved(@PathVariable("consentId") final String consentId) {
        final ConsentApprovalJson result = new ConsentApprovalJson();
        try {
            final String tokenRequestCode = authApiImpl.approveConsent(consentId, userContext.getUserId());
            final String userAuthToken = authApiImpl.requestUserAccessToken(tokenRequestCode)
                    .getAccessToken();
            userContext.setAuthorizationToken(userAuthToken);

            result.setSuccess(true);
        } catch (final Exception e) { // no catch-all in prod systems
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/goodbye", method = RequestMethod.GET)
    public GenericActionResultJson logout() {
        userContext.setAuthorizationToken(null);
        userContext.setUserId(null);
        return GenericActionResultJson.success();
    }

}
