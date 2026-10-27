package org.howard.edu.lsp.midterm.inheritance;

/**
 * A full-time employee, paid the hourly rate for the first 40 hours of the
 * week and 1.5 times the hourly rate for every hour beyond 40.
 *
 * @author Jaila Walker
 */
public class FullTimeEmployee extends Employee {

    /** Weekly hours after which overtime applies. */
    private static final double OVERTIME_THRESHOLD = 40.0;

    /** Multiplier applied to the hourly rate for overtime hours. */
    private static final double OVERTIME_MULTIPLIER = 1.5;

    /**
     * Creates a full-time employee.
     *
     * @param hoursWorked the hours worked this week (assumed nonnegative)
     * @param hourlyRate  the hourly pay rate (assumed nonnegative)
     */
    public FullTimeEmployee(double hoursWorked, double hourlyRate) {
        super(hoursWorked, hourlyRate);
    }

    /**
     * Calculates pay with overtime: regular rate up to 40 hours, then
     * 1.5 times the rate for each additional hour.
     *
     * @return the weekly pay including any overtime
     */
    @Override
    public double calculatePay() {
        double hours = getHoursWorked();
        double rate = getHourlyRate();
        double regularHours = Math.min(hours, OVERTIME_THRESHOLD);
        double overtimeHours = Math.max(0.0, hours - OVERTIME_THRESHOLD);
        return regularHours * rate + overtimeHours * rate * OVERTIME_MULTIPLIER;
    }
}
