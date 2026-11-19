package org.example.utils;

import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;

public class DBSink extends Sink {
    SinkConfiguration sinkConfiguration;

    public DBSink(SinkConfiguration sinkConfiguration) {
        this.sinkConfiguration = sinkConfiguration;
    }
}
