package com.phonecompany.billing;

import java.time.Duration;
import java.time.LocalDateTime;

public record CallInfo(long phone, LocalDateTime timeStarted, LocalDateTime timeFinished, Duration duration) {
}
