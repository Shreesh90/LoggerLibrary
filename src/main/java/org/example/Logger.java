package org.example;

import org.example.configuration.LoggerConfiguration;
import org.example.interfaces.Sink;
import org.example.utils.LogLevel;
import org.example.utils.LogMode;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Logger {
    private final LoggerConfiguration loggerConfiguration;
    // hostName (extension)
    private final String hostName="us-host-1";

    // (Extension 2) only created in ASYNC mode.
    // Single thread executor -> logs are written in the SAME order they were submitted,
    // and only one thread touches the sinks (sink locks still protect SYNC callers).
    private final ExecutorService executor;

    public Logger(LoggerConfiguration loggerConfiguration) {
        this.loggerConfiguration = loggerConfiguration;
        this.executor = loggerConfiguration.getLogMode() == LogMode.ASYNC
                ? Executors.newSingleThreadExecutor()
                : null;
    }

    public void log(LogLevel logLevel, String namespace, String message) {
        Objects.requireNonNull(logLevel);
        // keeping time as the first thing as soon as logger logs
        // Instant = exact UTC moment, no timezone/format baked in (formatter decides how to show it)
        Instant now = Instant.now();

        // fetching the correct sink corresponding the the log level the client want to log at
        // in case client doesnt add any sink for a log level, lets
        // ignore it, this prevents the logger library from stop functioning
        Sink sink = loggerConfiguration.getSink(logLevel);
        if (sink == null) return;

        // creating log message, our main data object
        // Everything here is built on the CALLER thread (time + correlation id),
        // so the values are correct even if the write happens later on another thread.
        LoggerMessage loggerMessage = new LoggerMessage(
                now,
                logLevel,
                message,
                namespace,
                hostName,
                // (Extension 5) read from caller's ThreadLocal NOW - the executor thread
                // has its own (empty) ThreadLocal, so reading it inside the task would give null
                CorrelationContext.get()
        );

        // (Extension 2) sync vs async
        if (executor == null) {
            sink.log(loggerMessage);                     // SYNC: caller thread writes
        } else {
            executor.submit(() -> sink.log(loggerMessage)); // ASYNC: queued, caller returns immediately
        }
    }

    // (Extension 2) client must call this on app shutdown in ASYNC mode,
    // otherwise messages still in the queue can be lost
    public void shutdown() {
        if (executor == null) return;
        executor.shutdown(); // stop accepting new logs, finish queued ones
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
