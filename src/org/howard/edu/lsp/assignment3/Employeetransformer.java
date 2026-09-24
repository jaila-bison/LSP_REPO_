package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Transform step: parses and validates each raw row into an Employee,
 * or rejects it. Rejected rows are counted as skipped.
 */
final class EmployeeTransformer {

    List<Employee> transformAll(List<String[]> rawRows, RunSummary summary) {
        List<Employee> employees = new ArrayList<>();
        for (String[] row : rawRows) {
            Optional<Employee> employee = transform(row);
            if (employee.isPresent()) {
                employees.add(employee.get());
                summary.recordTransformedRow();
            } else {
                summary.recordSkippedRow();
            }
        }
        return employees;
    }

    private Optional<Employee> transform(String[] fields) {
        int employeeId;
        BigDecimal hoursWorked;
        BigDecimal hourlyRate;

        try {
            employeeId = Integer.parseInt(fields[0].trim());
            hoursWorked = new BigDecimal(fields[3].trim());
            hourlyRate = new BigDecimal(fields[4].trim());
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        if (hoursWorked.signum() < 0 || hourlyRate.signum() < 0) {
            return Optional.empty();
        }

        String name = fields[1].trim().toUpperCase();
        String department = fields[2].trim();

        return Optional.of(new Employee(employeeId, name, department, hoursWorked, hourlyRate));
    }
}