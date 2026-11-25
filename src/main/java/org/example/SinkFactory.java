package org.example;

import org.example.formatters.PlainFormatter;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;
import org.example.interfaces.SinkCreator;
import org.example.sinks.DBSink;
import org.example.sinks.TextSink;
import org.example.utils.SinkType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SinkFactory {
    // (Extension 3) Registry of creators instead of a switch statement.
    // Old switch: every new sink = edit this class (violates Open/Closed).
    // Now: new sink = SinkFactory.register("KAFKA", KafkaSink::new) from anywhere.
    // ConcurrentHashMap -> safe if registration happens from multiple threads.
    private static final Map<String, SinkCreator> REGISTRY = new ConcurrentHashMap<>();

    // built-in sinks are registered once
    static {
        register(SinkType.TEXT.name(), TextSink::new);
        register(SinkType.DB.name(), DBSink::new);
    }

    public static void register(String sinkType, SinkCreator creator) {
        REGISTRY.put(sinkType, creator);
    }

    // (Extension 4) formatter is injected from the client side
    public static Sink getSink(String sinkType, SinkConfiguration config, LogFormatter formatter) {
        SinkCreator creator = REGISTRY.get(sinkType);
        if (creator == null) throw new IllegalArgumentException("No sink registered for " + sinkType);
        return creator.create(config, formatter);
    }

    // convenience overloads so existing client code keeps working (default = plain text)
    public static Sink getSink(SinkType sinkType, SinkConfiguration config, LogFormatter formatter) {
        return getSink(sinkType.name(), config, formatter);
    }

    public static Sink getSink(SinkType sinkType, SinkConfiguration config) {
        return getSink(sinkType.name(), config, new PlainFormatter());
    }
}
