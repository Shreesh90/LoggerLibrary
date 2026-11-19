package org.example;

import org.example.configuration.LoggerConfiguration;
import org.example.interfaces.Sink;
import org.example.utils.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Logger {
    private final LoggerConfiguration loggerConfiguration;
    private final DateTimeFormatter formatter;
    // hostName (extension)
    private final String hostName="us-host-1";

    public Logger(LoggerConfiguration loggerConfiguration) {
        this.loggerConfiguration = loggerConfiguration;
        // formatter gets created only once, instead of in multiple log calls below
        this.formatter = DateTimeFormatter.ofPattern(loggerConfiguration.getDateTimeFormat());
    }

    public void log(LogLevel logLevel, String namespace, String message) {
        Objects.requireNonNull(logLevel);
        // keeping time as the first thing as soon as logger logs
        LocalDateTime now = LocalDateTime.now();

        // fetching the correct sink corresponding the the log level the client want to log at
        // in case client doesnt add any sink for a log level, lets
        // ignore it, this prevents the logger library from stop functioning
        Sink sink = loggerConfiguration.getSink(logLevel);
        if (sink == null) return;

        // creating log message, our main data object
        LoggerMessage loggerMessage = new LoggerMessage(
                // We format the date-time here itself, as soon as the logs come
                now.format(formatter),
                logLevel,
                message,
                namespace,
                hostName
        );
        sink.log(loggerMessage);
    }
}
