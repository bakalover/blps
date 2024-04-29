package com.example.blps.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LogService {

    @Scheduled(fixedRate = 10000)
    @Async
    public void cleanLogs() {
        try {
            var logFilePath = "./app_logs.log";
            File logFile = new File(logFilePath);
            if (logFile.exists() && logFile.length() > 0) {
                List<String> lastLines = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (lastLines.size() >= 10) {
                            lastLines.remove(0); // Remove the first line to keep only the last 10
                        }
                        lastLines.add(line);
                    }
                }

                // Write the last 10 lines back to the file
                try (FileWriter fileWriter = new FileWriter(logFile, false)) {
                    for (String lastLine : lastLines) {
                        fileWriter.write(lastLine + System.lineSeparator());
                    }
                }

                log.info("Logs successfully cleaned!");
            } else {
                log.info("Log file does not exist or is empty.");
            }
        } catch (IOException e) {
            log.error("An error occurred while cleaning logs", e);
        }
    }
}