package com.phonecompany.billing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CallInfoParser {
    private final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public CallInfo parse(String[] line) {
        var phone = Long.parseLong(line[0]);
        var timeStarted = LocalDateTime.parse(line[1], defaultFormatter);
        var timeFinished = LocalDateTime.parse(line[2], defaultFormatter);
        var callDuration = Duration.between(timeStarted, timeFinished);

        return new CallInfo(phone, timeStarted, timeFinished, callDuration);
    }

}
