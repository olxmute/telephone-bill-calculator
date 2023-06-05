package com.phonecompany.billing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TelephoneBillCalculatorIntegrationTest {

    private final TelephoneBillCalculator calculator = new TelephoneBillCalculatorImpl(
            new PhoneCallCostCalculatorImpl(),
            new CallInfoParser()
    );

    @Test
    void testNoCalls() {
        // GIVEN
        var phoneLog = "";
        BigDecimal expectedResult = BigDecimal.ZERO;

        // WHEN
        BigDecimal actualResult = calculator.calculate(phoneLog);

        // THEN
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testCallsTheHighestRate() {
        // GIVEN
        var phoneLog = """
                420774567453,13-01-2020 08:10:15,13-01-2020 08:12:57
                420776562353,13-01-2020 08:10:30,13-01-2020 08:12:45
                420777891234,13-01-2020 08:09:00,13-01-2020 08:11:30
                420778901234,13-01-2020 08:11:00,13-01-2020 08:15:00
                420779876543,13-01-2020 08:13:00,13-01-2020 08:15:30
                """;
        BigDecimal expectedResult = new BigDecimal("13.00");

        // WHEN
        BigDecimal actualResult = calculator.calculate(phoneLog);

        // THEN
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testCallsTheLowestRate() {
        // GIVEN
        var phoneLog = """
                420774567453,13-01-2020 06:10:15,13-01-2020 06:12:57
                420776562353,13-01-2020 06:10:30,13-01-2020 06:12:45
                420777891234,13-01-2020 16:09:00,13-01-2020 16:11:30
                420778901234,13-01-2020 16:11:00,13-01-2020 16:15:00
                420779876543,13-01-2020 06:13:00,13-01-2020 06:15:30
                """;
        BigDecimal expectedResult = new BigDecimal("6.50");

        // WHEN
        BigDecimal actualResult = calculator.calculate(phoneLog);

        // THEN
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testCallsMixedRate() {
        // GIVEN
        var phoneLog = """
                420774567453,13-01-2020 07:59:15,13-01-2020 08:12:57
                420776562353,13-01-2020 07:50:30,13-01-2020 08:02:45
                420777891234,13-01-2020 15:56:00,13-01-2020 16:11:30
                420778901234,13-01-2020 15:57:00,13-01-2020 16:13:00
                420779876543,13-01-2020 15:57:00,13-01-2020 16:01:00
                """;
        BigDecimal expectedResult = new BigDecimal("21.60");

        // WHEN
        BigDecimal actualResult = calculator.calculate(phoneLog);

        // THEN
        assertEquals(expectedResult, actualResult);
    }
}
