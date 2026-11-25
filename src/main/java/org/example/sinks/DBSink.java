package org.example.sinks;

import org.example.LogFormatter;
import org.example.LoggerMessage;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DBSink implements Sink {
    SinkConfiguration sinkConfiguration;
    // (Extension 4) injected - a real DB sink could store formatter.format(msg) as a JSON column
    private final LogFormatter logFormatter;

    // since we dont have actual connection, using a local list to store logs
    private final List<LoggerMessage> logs = new ArrayList<>();

    // (Extension 1) ArrayList is not thread safe -> guard add/read with a lock
    private final ReentrantLock lock = new ReentrantLock();

    public DBSink(SinkConfiguration sinkConfiguration, LogFormatter logFormatter) {
        this.sinkConfiguration = sinkConfiguration;
        this.logFormatter = logFormatter;
    }

    @Override
    public void log(LoggerMessage message) {
        lock.lock();
        try {
            logs.add(message);
        } finally {
            lock.unlock();
        }
    }

    // specific to dbSink as we are not printing the logs anywhere, for the client to get logs
    public List<LoggerMessage> getLogs() {
        lock.lock();
        try {
            return new ArrayList<>(logs);
        } finally {
            lock.unlock();
        }
    }
}
