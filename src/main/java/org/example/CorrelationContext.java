package org.example;

import java.util.UUID;

// (Extension 5) Holds the correlation (tracking) id of the CURRENT request/thread.
// ThreadLocal = each thread sees its own value, so parallel requests don't mix ids.
//
// Client flow (e.g. at the start of an HTTP request / filter):
//   CorrelationContext.set(incomingHeaderOr(UUID));   // once per request
//   ... logger.log(...) any number of times ...
//   CorrelationContext.clear();                         // in finally (thread pools reuse threads!)
//
// UUID? Yes - random UUID (v4) is unique without any coordination between hosts,
// cheap to generate, and is the common choice. If an upstream service already sent
// an id (e.g. X-Correlation-Id header) reuse THAT, so the id spans services.
public final class CorrelationContext {
    private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();

    private CorrelationContext() {}

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    public static void set(String correlationId) {
        CORRELATION_ID.set(correlationId);
    }

    public static String get() {
        return CORRELATION_ID.get();
    }

    public static void clear() {
        CORRELATION_ID.remove();
    }
}
