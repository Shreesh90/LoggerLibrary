package org.example;

import org.example.utils.LogLevel;

// Class to contain only the data of log, no formatting here
public class LoggerMessage {
    public String dateTimeFormat;
    public LogLevel logLevel;
    public String namespace;
    public String message;
    // hostname (Extensions)
    public String hostName;

    public LoggerMessage(String dateTimeFormat, LogLevel logLevel, String message, String namespace,
                         String hostName) {
        this.dateTimeFormat = dateTimeFormat;
        this.logLevel = logLevel;
        this.message = message;
        this.namespace = namespace;
        this.hostName = hostName;
    }
}
