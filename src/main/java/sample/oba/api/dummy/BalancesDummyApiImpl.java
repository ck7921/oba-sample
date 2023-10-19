package sample.oba.api.dummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import sample.oba.api.BalancesApi;
import sample.oba.api.dto.AccountListDto;
import sample.oba.api.dto.BalancesDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Component
@ConditionalOnProperty(
        value = "app.data.dev",
        havingValue = "true")
public class BalancesDummyApiImpl implements BalancesApi {

    private static final Logger logger = LoggerFactory.getLogger(BalancesDummyApiImpl.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        logger.info("Running with DEVELOPMENT Api: {}", this.getClass().getSimpleName());
    }

    @Override
    public AccountListDto getAccountList() {
        try (final InputStream is =
                     Files.newInputStream(ResourceUtils
                             .getFile("classpath:dummy/account-list.json").toPath())) {
            return mapper.readValue(is, AccountListDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BalancesDto getBalances(@NonNull String accountId) {
        try (final InputStream is =
                     Files.newInputStream(ResourceUtils
                             .getFile("classpath:dummy/balances.json").toPath())) {
            return mapper.readValue(is, BalancesDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
