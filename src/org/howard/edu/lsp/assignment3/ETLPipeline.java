package org.howard.edu.lsp.assignment3;

import java.io.IOException;
import java.util.List;


public class ETLPipeline {

    public static void main(String[] args) throws IOException {
        RunSummary summary = new RunSummary();
        EmployeeCsvReader reader = new EmployeeCsvReader("data/employees.csv");
        EmployeeTransformer transformer = new EmployeeTransformer();
        EmployeeCsvWriter writer = EmployeeCsvWriter.besideInput(reader.inputFile());

        List<String[]> rawRows = reader.readRows(summary);                    // Extract
        List<Employee> employees = transformer.transformAll(rawRows, summary); // Transform
        writer.write(employees);                                              // Load

        summary.print(writer.outputFile());
    }
}