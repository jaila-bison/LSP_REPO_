package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;

final class PayPolicy {
    static final BigDecimal REGULAR_HOURS_LIMIT = new BigDecimal("40.00");
    static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");

    static final String BONUS_DEPARTMENT = "IT";
    static final BigDecimal DEPARTMENT_BONUS_RATE = new BigDecimal("0.05");

    static final BigDecimal FULL_TIME_MINIMUM_HOURS = new BigDecimal("30.00");

    static final BigDecimal STANDARD_PAY_MINIMUM = new BigDecimal("500.00");
    static final BigDecimal HIGH_PAY_MINIMUM = new BigDecimal("1000.00");
    static final BigDecimal EXECUTIVE_PAY_MINIMUM = new BigDecimal("2000.00");

    private PayPolicy() {
        // constants only; not meant to be instantiated
    }
}