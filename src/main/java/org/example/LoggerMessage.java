package org.example;

import org.example.utils.LogLevel;

import java.time.Instant;

// Class to contain only the data of log, no formatting here
// Every Sink receives the same object and decides how to store it themselves
//
// Immutable: all fields are final and set once in the constructor.
//  - nobody (a sink, a formatter) can change a message another sink is still using
//  - final fields are "safely published" by the Java Memory Model, so when the message
//    is handed to the async executor thread, that thread is guaranteed to see the
//    fully built values (no half-initialised object), without any extra locking
public final class LoggerMessage {
    // raw point in time (UTC, no format/timezone). Formatting is the formatter's job,
    // so plain text can show "25-11-2026 12:05:05" while JSON shows ISO-8601
    public final Instant timestamp;
    public final LogLevel logLevel;
    public final String namespace;
    public final String message;
    // hostname (Extensions)
    public final String hostName;
    // (Extension 5) tracking id - captured on the CALLER thread so it survives async hand-off
    public final String correlationId;

    public LoggerMessage(Instant timestamp, LogLevel logLevel, String message, String namespace,
                         String hostName, String correlationId) {
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.message = message;
        this.namespace = namespace;
        this.hostName = hostName;
        this.correlationId = correlationId;
    }
}
