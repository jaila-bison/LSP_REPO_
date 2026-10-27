package org.howard.edu.lsp.midterm.inheritance;

/**
 * A part-time employee, paid straight hourly pay for all hours worked
 * with no overtime.
 *
 * @author Jaila Walker
 */
public class PartTimeEmployee extends Employee {

    /**
     * Creates a part-time employee.
     *
     * @param hoursWorked the hours worked this week (assumed nonnegative)
     * @param hourlyRate  the hourly pay rate (assumed nonnegative)
     */
    public PartTimeEmployee(double hoursWorked, double hourlyRate) {
        super(hoursWorked, hourlyRate);
    }

    /**
     * Calculates straight hourly pay: hours worked times hourly rate.
     *
     * @return the weekly pay
     */
    @Override
    public double calculatePay() {
        return getHoursWorked() * getHourlyRate();
    }
}
