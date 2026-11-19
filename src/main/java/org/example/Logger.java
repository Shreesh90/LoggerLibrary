package org.example;

import org.example.configuration.LoggerConfiguration;
import org.example.interfaces.Sink;
import org.example.utils.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private LoggerConfiguration loggerConfiguration;
    private Sink sink;
    private DateTimeFormatter formatter;

    public Logger(LoggerConfiguration loggerConfiguration) throws Exception {
        this.loggerConfiguration = loggerConfiguration;

        // added here, not in log method below to avoid initialiazztion of sink on each log request
        this.sink = loggerConfiguration.getSink();
        this.formatter = DateTimeFormatter.ofPattern(loggerConfiguration.getDateTimeFormat());
    }

    public void log(LogLevel logLevel, String namespace, String message) {
        LocalDateTime now = LocalDateTime.now(); // keeping time as the first thing as soon as logger logs
        LoggerMessage loggerMessage = new LoggerMessage(
                now.format(formatter),
                logLevel,
                message,
                namespace
        );

        sink.log(loggerMessage);
    }


}
