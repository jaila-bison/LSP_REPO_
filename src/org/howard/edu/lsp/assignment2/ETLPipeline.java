package org.howard.edu.lsp.assignment2;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ETLPipeline {
    private static final BigDecimal FORTY_HOURS = new BigDecimal("40.00");
    private static final BigDecimal THIRTY_HOURS = new BigDecimal("30.00");
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal FIVE_PERCENT = new BigDecimal("0.05");
    private static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");

    public static void main(String[] args) throws Exception {
        Path inputFile = resolveCsvPath("data/employees.csv");
        ExtractionResult extractionResult = extractWithSummary(inputFile.toString());
        List<EmployeeRecord> processedEmployees = extractionResult.processedEmployees;
        String outputPath = inputFile.resolveSibling("transformed_employees.csv").toString();
        Path outputFile = resolveOutputPath(outputPath);
        writeTransformedEmployees(outputPath, processedEmployees);

        System.out.printf("Rows read: %d%n", extractionResult.rowsRead);
        System.out.printf("Rows transformed: %d%n", processedEmployees.size());
        System.out.printf("Skipped rows: %d%n", extractionResult.skippedRows);
        System.out.printf("Output file: %s%n", outputFile);
    }

    public static List<EmployeeRecord> extractAndTransform(String inputPath) throws IOException {
        return extractWithSummary(inputPath).processedEmployees;
    }

    private static ExtractionResult extractWithSummary(String inputPath) throws IOException {
        Path csvPath = resolveCsvPath(inputPath);
        List<EmployeeRecord> processedEmployees = new ArrayList<>();
        int rowsRead = 0;
        int skippedRows = 0;

        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                rowsRead++;
                if (line.trim().isEmpty()) {
                    skippedRows++;
                    continue;
                }

                String[] fields = line.split(",", -1);
                if (fields.length != 5) {
                    skippedRows++;
                    continue;
                }

                EmployeeRecord transformed = transformRow(fields);
                if (transformed != null) {
                    processedEmployees.add(transformed);
                } else {
                    skippedRows++;
                }
            }
        }

        return new ExtractionResult(processedEmployees, rowsRead, skippedRows);
    }

    public static void writeTransformedEmployees(String outputPath, List<EmployeeRecord> employees) throws IOException {
        Path outputFile = resolveOutputPath(outputPath);
        Path parent = outputFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        List<String> lines = new ArrayList<>();
        lines.add("EmployeeID,Name,Department,HoursWorked,HourlyRate,GrossPay,PayLevel,EmploymentStatus");

        for (EmployeeRecord employee : employees) {
            lines.add(String.join(",",
                    String.valueOf(employee.employeeId),
                    employee.name,
                    employee.department,
                    formatTwoDecimalPlaces(employee.hoursWorked),
                    formatTwoDecimalPlaces(employee.hourlyRate),
                    formatTwoDecimalPlaces(employee.grossPay),
                    employee.payLevel,
                    employee.employmentStatus
            ));
        }

        Files.write(outputFile, lines, StandardCharsets.UTF_8);
    }

    private static Path resolveOutputPath(String outputPath) {
        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        Path explicitRootPath = projectRoot.resolve(outputPath);
        if (Files.exists(explicitRootPath.getParent())) {
            return explicitRootPath;
        }

        return projectRoot.resolve("data").resolve("transformed_employees.csv");
    }

    private static Path resolveCsvPath(String inputPath) {
        Path directPath = Paths.get(inputPath);
        if (Files.exists(directPath)) {
            return directPath;
        }

        Path nestedPath = Paths.get("lsp2", inputPath);
        if (Files.exists(nestedPath)) {
            return nestedPath;
        }

        Path workingDirectoryPath = Paths.get(System.getProperty("user.dir"), inputPath);
        if (Files.exists(workingDirectoryPath)) {
            return workingDirectoryPath;
        }

        Path rootDataPath = Paths.get(System.getProperty("user.dir"), "data", inputPath.replaceFirst("^data/", ""));
        if (Files.exists(rootDataPath)) {
            return rootDataPath;
        }

        return directPath;
    }

    private static EmployeeRecord transformRow(String[] rawFields) {
        String employeeIdText = rawFields[0].trim();
        String nameText = rawFields[1].trim();
        String departmentText = rawFields[2].trim();
        String hoursWorkedText = rawFields[3].trim();
        String hourlyRateText = rawFields[4].trim();

        int employeeId;
        BigDecimal hoursWorked;
        BigDecimal hourlyRate;

        try {
            employeeId = Integer.parseInt(employeeIdText);
        } catch (NumberFormatException e) {
            return null;
        }

        try {
            hoursWorked = new BigDecimal(hoursWorkedText);
            hourlyRate = new BigDecimal(hourlyRateText);
        } catch (NumberFormatException e) {
            return null;
        }

        if (hoursWorked.compareTo(ZERO) < 0 || hourlyRate.compareTo(ZERO) < 0) {
            return null;
        }

        String normalizedName = nameText.toUpperCase();
        String normalizedDepartment = departmentText;

        BigDecimal grossPay = calculateGrossPay(hoursWorked, hourlyRate);
        if (normalizedDepartment.equals("IT")) {
            grossPay = grossPay.multiply(BigDecimal.ONE.add(FIVE_PERCENT));
        }

        BigDecimal roundedGrossPay = grossPay.setScale(2, RoundingMode.HALF_UP);
        String payLevel = determinePayLevel(roundedGrossPay);
        String employmentStatus = determineEmploymentStatus(hoursWorked);

        return new EmployeeRecord(
                employeeId,
                normalizedName,
                normalizedDepartment,
                hoursWorked,
                hourlyRate,
                roundedGrossPay,
                payLevel,
                employmentStatus
        );
    }

    private static BigDecimal calculateGrossPay(BigDecimal hoursWorked, BigDecimal hourlyRate) {
        if (hoursWorked.compareTo(FORTY_HOURS) <= 0) {
            return hoursWorked.multiply(hourlyRate);
        }

        BigDecimal regularPay = FORTY_HOURS.multiply(hourlyRate);
        BigDecimal overtimeHours = hoursWorked.subtract(FORTY_HOURS);
        BigDecimal overtimeRate = hourlyRate.multiply(OVERTIME_MULTIPLIER);
        BigDecimal overtimePay = overtimeHours.multiply(overtimeRate);

        return regularPay.add(overtimePay);
    }

    private static String determinePayLevel(BigDecimal grossPay) {
        if (grossPay.compareTo(new BigDecimal("500.00")) < 0) {
            return "Low";
        }
        if (grossPay.compareTo(new BigDecimal("1000.00")) < 0) {
            return "Standard";
        }
        if (grossPay.compareTo(new BigDecimal("2000.00")) < 0) {
            return "High";
        }
        return "Executive";
    }

    private static String determineEmploymentStatus(BigDecimal hoursWorked) {
        return hoursWorked.compareTo(THIRTY_HOURS) < 0 ? "Part-Time" : "Full-Time";
    }

    private static String formatTwoDecimalPlaces(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static class ExtractionResult {
        private final List<EmployeeRecord> processedEmployees;
        private final int rowsRead;
        private final int skippedRows;

        private ExtractionResult(List<EmployeeRecord> processedEmployees, int rowsRead, int skippedRows) {
            this.processedEmployees = processedEmployees;
            this.rowsRead = rowsRead;
            this.skippedRows = skippedRows;
        }
    }

    private static class EmployeeRecord {
        private final int employeeId;
        private final String name;
        private final String department;
        private final BigDecimal hoursWorked;
        private final BigDecimal hourlyRate;
        private final BigDecimal grossPay;
        private final String payLevel;
        private final String employmentStatus;

        private EmployeeRecord(
                int employeeId,
                String name,
                String department,
                BigDecimal hoursWorked,
                BigDecimal hourlyRate,
                BigDecimal grossPay,
                String payLevel,
                String employmentStatus) {
            this.employeeId = employeeId;
            this.name = name;
            this.department = department;
            this.hoursWorked = hoursWorked;
            this.hourlyRate = hourlyRate;
            this.grossPay = grossPay;
            this.payLevel = payLevel;
            this.employmentStatus = employmentStatus;
        }
    }
}
