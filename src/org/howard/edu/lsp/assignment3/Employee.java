package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An immutable employee that knows how to calculate its own pay information.
 * The rules it applies come from PayPolicy.
 */
final class Employee {
    private final int employeeId;
    private final String name;
    private final String department;
    private final BigDecimal hoursWorked;
    private final BigDecimal hourlyRate;

    Employee(int employeeId, String name, String department, BigDecimal hoursWorked, BigDecimal hourlyRate) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }

    int employeeId() {
        return employeeId;
    }

    String name() {
        return name;
    }

    String department() {
        return department;
    }

    BigDecimal hoursWorked() {
        return hoursWorked;
    }

    BigDecimal hourlyRate() {
        return hourlyRate;
    }

    /** Gross pay with overtime and any department bonus, rounded to cents. */
    BigDecimal grossPay() {
        BigDecimal pay = basePay();
        if (PayPolicy.BONUS_DEPARTMENT.equals(department)) {
            pay = pay.multiply(BigDecimal.ONE.add(PayPolicy.DEPARTMENT_BONUS_RATE));
        }
        return pay.setScale(2, RoundingMode.HALF_UP);
    }

    String payLevel() {
        BigDecimal grossPay = grossPay();
        if (grossPay.compareTo(PayPolicy.STANDARD_PAY_MINIMUM) < 0) {
            return "Low";
        }
        if (grossPay.compareTo(PayPolicy.HIGH_PAY_MINIMUM) < 0) {
            return "Standard";
        }
        if (grossPay.compareTo(PayPolicy.EXECUTIVE_PAY_MINIMUM) < 0) {
            return "High";
        }
        return "Executive";
    }

    String employmentStatus() {
        return hoursWorked.compareTo(PayPolicy.FULL_TIME_MINIMUM_HOURS) < 0 ? "Part-Time" : "Full-Time";
    }

    private BigDecimal basePay() {
        if (hoursWorked.compareTo(PayPolicy.REGULAR_HOURS_LIMIT) <= 0) {
            return hoursWorked.multiply(hourlyRate);
        }
        BigDecimal regularPay = PayPolicy.REGULAR_HOURS_LIMIT.multiply(hourlyRate);
        BigDecimal overtimeHours = hoursWorked.subtract(PayPolicy.REGULAR_HOURS_LIMIT);
        BigDecimal overtimeRate = hourlyRate.multiply(PayPolicy.OVERTIME_MULTIPLIER);
        return regularPay.add(overtimeHours.multiply(overtimeRate));
    }
}