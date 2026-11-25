package org.example.configuration;

import org.example.interfaces.Sink;
import org.example.utils.LogLevel;

import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class LoggerConfiguration {
    // Sink is tied to one or more message level (document requirement)
    private final Map<LogLevel, Sink> sinkByLogLevel = new EnumMap<>(LogLevel.class);

    // date-time pattern moved out of here -> it now belongs to the formatter (PlainFormatter)

    public LoggerConfiguration addSink(Sink sink, LogLevel... levels) {
        for (LogLevel level : levels) {
            sinkByLogLevel.put(level, sink);
        }
        return this;
    }

    public Sink getSink(LogLevel level) {
        return sinkByLogLevel.get(level);
    }

    // (Extension 2) each sink only once (same sink can serve several levels) - used for shutdown
    public Set<Sink> getAllSinks() {
        Set<Sink> sinks = Collections.newSetFromMap(new IdentityHashMap<>());
        sinks.addAll(sinkByLogLevel.values());
        return sinks;
    }
}
