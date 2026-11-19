package org.example.configuration;

import org.example.interfaces.Sink;
import org.example.utils.LogLevel;

import java.util.EnumMap;
import java.util.Map;

public class LoggerConfiguration {
    private final String dateTimeFormat;

    // Sink is tied to one or more message level (document requirement)
    private final Map<LogLevel, Sink> sinkByLogLevel = new EnumMap<>(LogLevel.class);

    public LoggerConfiguration(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public LoggerConfiguration addSink(Sink sink, LogLevel... levels) {
        for (LogLevel level : levels) {
            sinkByLogLevel.put(level, sink);
        }
        return this;
    }

    public Sink getSink(LogLevel level) {
        return sinkByLogLevel.get(level);
    }
}
