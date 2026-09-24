package org.howard.edu.lsp.assignment3;

import java.nio.file.Path;

final class RunSummary {
    private int rowsRead;
    private int rowsTransformed;
    private int skippedRows;

    void recordRowRead() {
        rowsRead++;
    }

    void recordTransformedRow() {
        rowsTransformed++;
    }

    void recordSkippedRow() {
        skippedRows++;
    }

    void print(Path outputFile) {
        System.out.printf("Rows read: %d%n", rowsRead);
        System.out.printf("Rows transformed: %d%n", rowsTransformed);
        System.out.printf("Skipped rows: %d%n", skippedRows);
        System.out.printf("Output file: %s%n", outputFile);
    }
}