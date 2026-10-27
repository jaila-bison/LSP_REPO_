package org.howard.edu.lsp.midterm.inheritance;

/**
 * A contractor, paid the hourly rate plus a 15% premium on all hours worked.
 *
 * @author Jaila Walker
 */
public class Contractor extends Employee {

    /** Premium multiplier applied to all contractor hours (15% premium). */
    private static final double PREMIUM_MULTIPLIER = 1.15;

    /**
     * Creates a contractor.
     *
     * @param hoursWorked the hours worked this week (assumed nonnegative)
     * @param hourlyRate  the hourly pay rate (assumed nonnegative)
     */
    public Contractor(double hoursWorked, double hourlyRate) {
        super(hoursWorked, hourlyRate);
    }

    /**
     * Calculates pay with a 15% premium on all hours worked.
     *
     * @return the weekly pay including the premium
     */
    @Override
    public double calculatePay() {
        return getHoursWorked() * getHourlyRate() * PREMIUM_MULTIPLIER;
    }
}
