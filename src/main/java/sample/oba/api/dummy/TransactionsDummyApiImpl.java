package sample.oba.api.dummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import sample.oba.api.TransactionApi;
import sample.oba.api.dto.TransactionDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Instant;

@Component
@ConditionalOnProperty(
        value = "app.data.transactions.dev",
        havingValue = "true")
public class TransactionsDummyApiImpl implements TransactionApi {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsDummyApiImpl.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${app.data.transactions.dev.pagination}")
    private boolean usePagination;

    @PostConstruct
    public void init() {
        logger.info("Running with DEVELOPMENT Api: {}", this.getClass().getSimpleName());
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public TransactionDto getTransactionsOfAccount(@NonNull String accountId,
                                                   Instant startBookingDate,
                                                   Instant endBookingDate,
                                                   int page) {
        final String fileName = "transactions" + (usePagination ? "-" + page : "") + ".json";
        logger.debug("Loading transaction data from file: {}", fileName);

        try (final InputStream is =
                     Files.newInputStream(ResourceUtils
                             .getFile("classpath:dummy/" + fileName).toPath())) {
            return mapper.readValue(is, TransactionDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
