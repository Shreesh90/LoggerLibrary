package org.example.sinks;

import org.example.LoggerMessage;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBSink implements Sink {
    SinkConfiguration sinkConfiguration;
    private String dbHost;
    private String dbPort;

    // since we dont have actual connection, using a local list to store logs
    private final List<LoggerMessage> logs = new ArrayList<>();

    public DBSink(SinkConfiguration sinkConfiguration) {
        this.sinkConfiguration = sinkConfiguration;
    }

    @Override
    public synchronized void log(LoggerMessage message) {
        logs.add(message);
    }

    // specific to dbSink as we are not printing the logs anywhere, for the client to get logs
    public synchronized List<LoggerMessage> getLogs() {
        return new ArrayList<>(logs);
    }
}
