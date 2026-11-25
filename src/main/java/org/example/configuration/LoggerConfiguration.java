package org.example.configuration;

import org.example.interfaces.Sink;
import org.example.utils.LogLevel;
import org.example.utils.LogMode;

import java.util.EnumMap;
import java.util.Map;

public class LoggerConfiguration {
    // (Extension 2) default SYNC so existing behaviour does not change
    private LogMode logMode = LogMode.SYNC;

    // Sink is tied to one or more message level (document requirement)
    private final Map<LogLevel, Sink> sinkByLogLevel = new EnumMap<>(LogLevel.class);

    // date-time pattern moved out of here -> it now belongs to the formatter (PlainFormatter)

    public LoggerConfiguration addSink(Sink sink, LogLevel... levels) {
        for (LogLevel level : levels) {
            sinkByLogLevel.put(level, sink);
        }
        return this;
    }

    public LoggerConfiguration setLogMode(LogMode logMode) {
        this.logMode = logMode;
        return this;
    }

    public LogMode getLogMode() {
        return logMode;
    }

    public Sink getSink(LogLevel level) {
        return sinkByLogLevel.get(level);
    }
}
