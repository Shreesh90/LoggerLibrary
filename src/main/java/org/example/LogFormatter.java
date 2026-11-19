package org.example;

// Keeping formatter separate from how data is stored (Single Responsibility)
public class LogFormatter {

    public String format(LoggerMessage loggerMessage) {
        return  loggerMessage.logLevel + " [" + loggerMessage.dateTimeFormat + ", " + loggerMessage.namespace  + "] " + loggerMessage.message;
    }
}
