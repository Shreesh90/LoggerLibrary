package org.example;

import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;
import org.example.sinks.DBSink;
import org.example.sinks.TextSink;
import org.example.utils.SinkType;

public class SinkFactory {
    // simple factory pattern to get sink corresponding to sinkType
    public static Sink getSink(SinkType sinkType, SinkConfiguration sinkConfiguration) {
        return switch (sinkType) {
            case TEXT -> new TextSink(sinkConfiguration);
            case DB -> new DBSink(sinkConfiguration);
        };
    }
}
