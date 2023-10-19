package sample.utils;

import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class NumericUtils {

    private static DecimalFormat SWISS_DECIMAL_FORMAT = new DecimalFormat("#,##0.00",
            new DecimalFormatSymbols(new Locale("de", "CH")));

    private NumericUtils() {
        throw new RuntimeException("utility class, don't instantiate");
    }

    public static BigDecimal round(@NonNull BigDecimal value) {
        final int lastDigit = value
                .setScale(2, RoundingMode.HALF_UP)
                .movePointRight(2)
                .remainder(BigDecimal.TEN)
                .intValue();

        // 3,4,5, -> + 5 - digit
        // 6,7 -> 5 -> + 5 - digit
        int delta;
        if (lastDigit < 3 || lastDigit > 7) {
            delta = lastDigit < 3 ? -lastDigit : 10 - lastDigit;
        } else {
            // 0, 1, 2, -> - digit
            // 8, 9 -> 0 -> + (10-digit)
            delta = 5 - lastDigit;
        }

        return value
                .setScale(2, RoundingMode.HALF_UP)
                .movePointRight(2)
                .add(BigDecimal.valueOf(delta))
                .movePointLeft(2);
    }

    public static String currencyFormatted(@NonNull BigDecimal value) {
        if(value.scale()>2) throw new IllegalArgumentException("scale>2 not supported");
        return SWISS_DECIMAL_FORMAT.format(value);
    }

}
