package org.example;

import org.example.utils.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerMessage {
    private String dateTimeFormat;
    private LogLevel logLevel;
    private String message;

    public LoggerMessage(String dateTimeFormat, LogLevel logLevel, String message) {
        this.dateTimeFormat = dateTimeFormat;
        this.logLevel = logLevel;
        this.message = message;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        return now.format(formatter) + ' ' + logLevel + ' ' + message;
    }
}
