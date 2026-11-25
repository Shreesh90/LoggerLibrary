package org.example.formatters;

import org.example.LogFormatter;
import org.example.LoggerMessage;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// (Extension 4) The old LogFormatter logic, now one of the pluggable formatters
public class PlainFormatter implements LogFormatter {
    private static final String DEFAULT_PATTERN = "dd-MM-yyyy HH:mm:ss";

    // DateTimeFormatter is immutable -> thread safe, created once and shared by all threads.
    // An Instant has no timezone, so we attach one (server's zone) to turn it into a readable date.
    private final DateTimeFormatter dateTimeFormatter;

    public PlainFormatter() {
        this(DEFAULT_PATTERN);
    }

    public PlainFormatter(String pattern) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
    }

    @Override
    public String format(LoggerMessage loggerMessage) {
        return loggerMessage.logLevel + " [" + dateTimeFormatter.format(loggerMessage.timestamp) + ", " + loggerMessage.namespace + "]"
                + " [host=" + loggerMessage.hostName + "]"
                // (Extension 5) correlation id printed with every line
                + " [cid=" + loggerMessage.correlationId + "] "
                + loggerMessage.message;
    }
}
