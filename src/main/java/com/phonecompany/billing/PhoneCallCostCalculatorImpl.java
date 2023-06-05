package com.phonecompany.billing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class PhoneCallCostCalculatorImpl implements PhoneCallCostCalculator {

    private final TimeInterval maxChargeInterval;

    public PhoneCallCostCalculatorImpl() {
        this(null, null);
    }

    public PhoneCallCostCalculatorImpl(LocalTime maxChargeStart, LocalTime maxChargeEnd) {
        this.maxChargeInterval = new TimeInterval(
                defaultIfNull(maxChargeStart, LocalTime.of(8, 0)),
                defaultIfNull(maxChargeEnd, LocalTime.of(16, 0))
        );
    }

    @Override
    public BigDecimal calculate(CallInfo callInfo) {
        int minutes = (int) Math.ceil((double) callInfo.duration().toSeconds() / 60);

        return IntStream.rangeClosed(1, minutes)
                .mapToObj(minute -> getRateForMinute(callInfo.timeStarted().plusMinutes(minute - 1), minute))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getRateForMinute(LocalDateTime callStart, int minute) {
        if (minute >= 5) {
            return new BigDecimal("0.20");
        }

        if (maxChargeInterval.isInterval(callStart.toLocalTime())) {
            return new BigDecimal("1.00");
        } else {
            return new BigDecimal("0.50");
        }
    }

}
