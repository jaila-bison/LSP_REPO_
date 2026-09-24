package org.howard.edu.lsp.assignment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

final class EmployeeCsvReader {
    private static final int EXPECTED_FIELD_COUNT = 5;

    private final Path inputFile;

    EmployeeCsvReader(String requestedInputPath) {
        this.inputFile = resolveInputPath(requestedInputPath);
    }

    Path inputFile() {
        return inputFile;
    }

    List<String[]> readRows(RunSummary summary) throws IOException {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            reader.readLine(); // skip header row

            String line;
            while ((line = reader.readLine()) != null) {
                summary.recordRowRead();

                if (line.trim().isEmpty()) {
                    summary.recordSkippedRow();
                    continue;
                }

                String[] fields = line.split(",", -1);
                if (fields.length != EXPECTED_FIELD_COUNT) {
                    summary.recordSkippedRow();
                    continue;
                }

                rows.add(fields);
            }
        }

        return rows;
    }

    private static Path resolveInputPath(String inputPath) {
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
}