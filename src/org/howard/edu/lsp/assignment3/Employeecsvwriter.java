package org.howard.edu.lsp.assignment3;
 
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
 
/**
 * Load step: writes the header and one CSV row per employee.
 * The output file is placed next to the input file when possible,
 * using the same fallback rules as Assignment #2.
 */
final class EmployeeCsvWriter {
    private static final String OUTPUT_FILE_NAME = "transformed_employees.csv";
    private static final String HEADER =
            "EmployeeID,Name,Department,HoursWorked,HourlyRate,GrossPay,PayLevel,EmploymentStatus";
 
    private final Path outputFile;
 
    private EmployeeCsvWriter(Path outputFile) {
        this.outputFile = outputFile;
    }
 
    static EmployeeCsvWriter besideInput(Path inputFile) {
        return new EmployeeCsvWriter(resolveOutputPath(inputFile.resolveSibling(OUTPUT_FILE_NAME).toString()));
    }
 
    Path outputFile() {
        return outputFile;
    }
 
    void write(List<Employee> employees) throws IOException {
        Path parent = outputFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
 
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Employee employee : employees) {
            lines.add(toCsvRow(employee));
        }
 
        Files.write(outputFile, lines, StandardCharsets.UTF_8);
    }
 
    private static String toCsvRow(Employee employee) {
        return String.join(",",
                String.valueOf(employee.employeeId()),
                employee.name(),
                employee.department(),
                formatTwoDecimalPlaces(employee.hoursWorked()),
                formatTwoDecimalPlaces(employee.hourlyRate()),
                formatTwoDecimalPlaces(employee.grossPay()),
                employee.payLevel(),
                employee.employmentStatus());
    }
 
    private static String formatTwoDecimalPlaces(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
 
    private static Path resolveOutputPath(String outputPath) {
        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        Path explicitRootPath = projectRoot.resolve(outputPath);
        if (Files.exists(explicitRootPath.getParent())) {
            return explicitRootPath;
        }
        return projectRoot.resolve("data").resolve(OUTPUT_FILE_NAME);
    }
}
 
