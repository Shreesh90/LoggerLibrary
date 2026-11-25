package org.example;

import org.example.formatters.PlainFormatter;
import org.example.interfaces.CustomSinkConfiguration;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;
import org.example.sinks.DBSink;
import org.example.sinks.TextSink;
import org.example.utils.SinkType;

public class SinkFactory {
    // Built-in sinks: simple factory pattern to get sink corresponding to sinkType
    // (Extension 4) formatter is injected from the client side
    public static Sink getSink(SinkType sinkType, SinkConfiguration config, LogFormatter formatter) {
        return switch (sinkType) {
            case TEXT -> new TextSink(config, formatter);
            case DB -> new DBSink(config, formatter);
        };
    }

    // convenience overload for built-in sinks (default = plain text)
    public static Sink getSink(SinkType sinkType, SinkConfiguration config) {
        return getSink(sinkType, config, new PlainFormatter());
    }

    // (Extension 3) Custom sinks: no switch case, no enum value needed.
    // The config builds its own sink (polymorphism), so adding a new sink never edits this class.
    public static Sink getSink(CustomSinkConfiguration config, LogFormatter formatter) {
        return config.createSink(formatter);
    }
}
