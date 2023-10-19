package smaple.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;
import sample.oba.api.dto.AccountListDto;
import sample.oba.api.dto.BalancesDto;
import sample.utils.NumericUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumericUtilsTest {

    @Test
    @Description("rounding 2 places test")
    void rounding2Places() {

        final Map<BigDecimal,BigDecimal> numberMap = new HashMap<>();
        numberMap.put(of("1.50"),of("1.50"));
        numberMap.put(of("1.51"),of("1.50"));
        numberMap.put(of("1.52"),of("1.50"));
        numberMap.put(of("1.53"),of("1.55"));
        numberMap.put(of("1.54"),of("1.55"));
        numberMap.put(of("1.55"),of("1.55"));
        numberMap.put(of("1.56"),of("1.55"));
        numberMap.put(of("1.57"),of("1.55"));
        numberMap.put(of("1.58"),of("1.60"));
        numberMap.put(of("1.59"),of("1.60"));
        numberMap.put(of("1.60"),of("1.60"));
        numberMap.put(of("1"),of("1.00"));
        numberMap.put(of("1.1"),of("1.10"));
        numberMap.put(of("1.5"),of("1.50"));

        for (Map.Entry<BigDecimal, BigDecimal> entry : numberMap.entrySet()) {
            final BigDecimal result = NumericUtils.round(entry.getKey());
            assertEquals(entry.getValue(), result);
        }

    }

    @Test
    @Description("rounding multiple places test")
    void roundingMultiplePlaces() {
        final Map<BigDecimal,BigDecimal> numberMap = new HashMap<>();
        for(int i=0;i<=10;++i) numberMap.put(of("1.50"+i),of("1.50"));
        for(int i=0;i<=10;++i) numberMap.put(of("1.53"+i),of("1.55"));
        for(int i=0;i<=10;++i) numberMap.put(of("1.55"+i),of("1.55"));
        for(int i=0;i<=10;++i) numberMap.put(of("1.56"+i),of("1.55"));
        for(int i=0;i<=10;++i) numberMap.put(of("1.59"+i),of("1.60"));
        for(int i=0;i<=10;++i) numberMap.put(of("1.60"+i),of("1.60"));

        for (Map.Entry<BigDecimal, BigDecimal> entry : numberMap.entrySet()) {
            final BigDecimal result = NumericUtils.round(entry.getKey());
            assertEquals(entry.getValue(), result);
        }

    }

    @Test
    @Description("format monetary amount")
    void formatSwissMonetaryAmount() {
        final Map<BigDecimal,String> numberMap = new HashMap<>();
        numberMap.put(of("0"),"0.00");
        numberMap.put(of("-0"),"0.00");
        numberMap.put(of("1"),"1.00");
        numberMap.put(of("-1"),"-1.00");
        numberMap.put(of("1.99"),"1.99");
        numberMap.put(of("1000.00"),"1’000.00");
        numberMap.put(of("10000.00"),"10’000.00");
        numberMap.put(of("1000000.00"),"1’000’000.00");
        numberMap.put(of("1999999.99"),"1’999’999.99");
        numberMap.put(of("100234567890123.99"),"100’234’567’890’123.99");

        for (Map.Entry<BigDecimal, String> entry : numberMap.entrySet()) {
            final String result = NumericUtils.currencyFormatted(entry.getKey());
            assertEquals(entry.getValue(), result);
        }
    }

    @Test
    void test() {
        ObjectMapper mapper = new ObjectMapper();
        try(InputStream is = Files.newInputStream(Paths.get("C:\\Users\\Chris\\IdeaProjects\\oba-sample\\src\\main\\resources\\dummy\\balances.json"))) {
            BalancesDto s = mapper.readValue(is, BalancesDto.class);
            System.out.println("done");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ob.sandbox.natwest.com")
                .path("/open-banking/v3.1/aisp/accounts/{accountId}/balances")
                .buildAndExpand("123").toUri().toString());
    }

    private BigDecimal of(final String s) {
        return new BigDecimal(s);
    }
}
