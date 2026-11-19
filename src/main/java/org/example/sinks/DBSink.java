package org.example.sinks;

import org.example.LoggerMessage;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;

public class DBSink implements Sink {
    SinkConfiguration sinkConfiguration;

    public DBSink(SinkConfiguration sinkConfiguration) {
        this.sinkConfiguration = sinkConfiguration;
    }

    @Override
    public void log(LoggerMessage message) {
        throw new UnsupportedOperationException();
    }
}
