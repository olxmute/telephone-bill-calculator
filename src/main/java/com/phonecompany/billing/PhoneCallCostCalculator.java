package com.phonecompany.billing;

import java.math.BigDecimal;

public interface PhoneCallCostCalculator {
    BigDecimal calculate(CallInfo callInfo);
}
