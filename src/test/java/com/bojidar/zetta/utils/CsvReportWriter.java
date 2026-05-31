package com.bojidar.zetta.utils;

import com.bojidar.zetta.models.LinkResult;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvReportWriter {

    public Path writeResults(List<LinkResult> results) {
        try {
            Path reportsDir = Path.of("target", "link-checker-results");
            Files.createDirectories(reportsDir);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

            Path filePath = reportsDir.resolve(timestamp + "_results.csv");

            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath.toFile()))) {
                writer.writeNext(new String[]{"url", "http_status", "page_title", "result"});

                for (LinkResult result : results) {
                    writer.writeNext(new String[]{
                            result.url(),
                            String.valueOf(result.httpStatus()),
                            result.pageTitle(),
                            result.result()
                    });
                }
            }

            return filePath;

        } catch (IOException e) {
            throw new RuntimeException("Could not write link checker CSV report.", e);
        }
    }
}
