package org.example;

import org.example.configuration.LoggerConfiguration;
import org.example.interfaces.Sink;
import org.example.utils.LogLevel;

import java.time.Instant;
import java.util.Objects;

public class Logger {
    private final LoggerConfiguration loggerConfiguration;
    // hostName (extension)
    private final String hostName="us-host-1";

    // (Extension 2) Logger has NO threading logic. Sync vs async is decided per sink by the
    // client: a plain sink is sync, a sink wrapped in AsyncSink is async (Decorator pattern).

    public Logger(LoggerConfiguration loggerConfiguration) {
        this.loggerConfiguration = loggerConfiguration;
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
                // (Extension 5) read from caller's ThreadLocal NOW - an AsyncSink's worker thread
                // has its own (empty) ThreadLocal, so reading it there would give null
                CorrelationContext.get()
        );

        // (Extension 2) same call for both: a sync sink writes now, an AsyncSink just queues it
        sink.log(loggerMessage);
    }

    // (Extension 2) client calls this on app shutdown: closes every sink once.
    // For an AsyncSink this flushes its queue, so no logs are lost.
    public void shutdown() {
        for (Sink sink : loggerConfiguration.getAllSinks()) {
            sink.close();
        }
    }
}
