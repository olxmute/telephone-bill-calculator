package com.phonecompany.billing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    private final PhoneCallCostCalculator phoneCallCostCalculator;
    private final CallInfoParser callInfoParser;

    public TelephoneBillCalculatorImpl(PhoneCallCostCalculator phoneCallCostCalculator, CallInfoParser callInfoParser) {
        this.phoneCallCostCalculator = phoneCallCostCalculator;
        this.callInfoParser = callInfoParser;
    }

    @Override
    public BigDecimal calculate(String phoneLog) {
        var calls = phoneLog.lines()
                .map(line -> line.split(","))
                .map(callInfoParser::parse)
                .toList();

        if (calls.isEmpty()) {
            return BigDecimal.ZERO;
        }

        var mostCalledPhone = getMostCalledPhone(calls);

        return calculateTotalBill(calls, mostCalledPhone);
    }

    private BigDecimal calculateTotalBill(List<CallInfo> calls, Long phoneToSkip) {
        return calls.stream()
                .filter(call -> call.phone() != phoneToSkip)
                .map(phoneCallCostCalculator::calculate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Long getMostCalledPhone(List<CallInfo> calls) {
        var phonesToNumberOfCalls = calls.stream().collect(Collectors.groupingBy(CallInfo::phone, Collectors.counting()));

        var maxNumberOfCalls = phonesToNumberOfCalls.values().stream().max(Long::compareTo).orElseThrow();
        return phonesToNumberOfCalls.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(maxNumberOfCalls))
                .map(Map.Entry::getKey)
                .max(Long::compareTo)
                .orElseThrow();
    }

}
