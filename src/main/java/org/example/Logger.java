package org.example;

import org.example.configuration.LoggerConfiguration;
import org.example.interfaces.Sink;

public class Logger {
    private LoggerConfiguration loggerConfiguration;
    Sink sink;

    public Logger(LoggerConfiguration loggerConfiguration) throws Exception {
        this.loggerConfiguration = loggerConfiguration;

        // added here, not in log method below to avoid initialiazztion of sink on each log request
        this.sink = loggerConfiguration.getSink();
    }

    public void log(String message) {
        LoggerMessage loggerMessage = new LoggerMessage(
                loggerConfiguration.getDateTimeFormat(),
                loggerConfiguration.getLogLevel(),
                message
        );

        sink.log(loggerMessage);
    }


}
