package org.example;

// (Extension 4) Formatter is now an interface -> pluggable (Strategy pattern)
// Keeping formatter separate from how data is stored (Single Responsibility)
// Implementations: formatters.PlainFormatter, formatters.JsonFormatter
public interface LogFormatter {
    String format(LoggerMessage loggerMessage);
}
