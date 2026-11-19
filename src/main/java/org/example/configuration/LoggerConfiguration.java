package org.example.configuration;

import org.example.TextSink;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;
import org.example.utils.DBSink;
import org.example.utils.LogLevel;
import org.example.utils.SinkType;

public class LoggerConfiguration {
    private String dateTimeFormat;
    private LogLevel logLevel;
    private SinkType sinkType;
    private SinkConfiguration sinkConfiguration;


    public LoggerConfiguration(String dateTimeFormat, LogLevel logLevel, SinkType sinkType, SinkConfiguration sinkConfiguration) {
        this.dateTimeFormat = dateTimeFormat;
        this.logLevel = logLevel;
        this.sinkType = sinkType;
        this.sinkConfiguration = sinkConfiguration;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public Sink getSink() throws Exception {
        return switch (sinkType) {
            case TEXT -> new TextSink(sinkConfiguration);
            case DB -> new DBSink(sinkConfiguration);
            default -> throw new Exception("Sink not supported");
        };
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }
}
