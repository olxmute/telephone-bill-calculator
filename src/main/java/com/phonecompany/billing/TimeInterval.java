package com.phonecompany.billing;

import java.time.LocalTime;

public record TimeInterval(LocalTime start, LocalTime end) {
    public boolean isInterval(LocalTime time) {
        return time.isAfter(start) && time.isBefore(end);
    }
}
