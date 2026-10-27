package org.howard.edu.lsp.midterm.inheritance;

/**
 * Abstract base class for all employee types.
 * <p>
 * Stores the hours worked and hourly rate shared by every employee and
 * declares {@link #calculatePay()}, which each subclass overrides with its
 * own pay rule. Callers work with the {@code Employee} type and get the
 * correct behavior through polymorphism, replacing the original
 * type-string conditional.
 * </p>
 *
 * @author Jaila Walker
 */
public abstract class Employee {

    private final double hoursWorked;
    private final double hourlyRate;

    /**
     * Creates an employee with the given hours and rate.
     *
     * @param hoursWorked the hours worked this week (assumed nonnegative)
     * @param hourlyRate  the hourly pay rate (assumed nonnegative)
     */
    public Employee(double hoursWorked, double hourlyRate) {
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }

    /**
     * Returns the hours worked this week.
     *
     * @return the hours worked
     */
    public double getHoursWorked() {
        return hoursWorked;
    }

    /**
     * Returns the hourly pay rate.
     *
     * @return the hourly rate
     */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Calculates this employee's pay for the week.
     *
     * @return the pay amount
     */
    public abstract double calculatePay();
}
