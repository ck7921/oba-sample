package sample.utils;

import lombok.NonNull;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class TimestampHelper {

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .withZone(ZoneOffset.UTC);

    private TimestampHelper() {
        throw new RuntimeException("utility class, don't instantiate");
    }

    public static String formatInstantToDateOnly(@NonNull final Instant instant) {
        return formatter.format(instant);
    }

}
