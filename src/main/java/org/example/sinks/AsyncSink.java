package org.example.sinks;

import org.example.LoggerMessage;
import org.example.interfaces.Sink;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

// (Extension 2) Decorator: wraps ANY sink and makes it asynchronous.
// It is itself a Sink, so Logger doesn't know/care whether a sink is sync or async.
//
//   SYNC  : new TextSink(...)                    -> caller thread writes
//   ASYNC : new AsyncSink(new TextSink(...))     -> caller only queues, background thread writes
//
// Each AsyncSink has its own single-thread executor:
//  - order is preserved per sink (one thread = FIFO queue)
//  - a slow sink (Kafka/DB) only delays itself, not the other sinks
// Wrap a sink ONCE and reuse that wrapper (wrapping twice = two queues writing the same target).
public class AsyncSink implements Sink {
    private final Sink delegate;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public AsyncSink(Sink delegate) {
        this.delegate = delegate;
    }

    @Override
    public void log(LoggerMessage message) {
        // message is immutable & already has timestamp + correlation id (captured on caller thread)
        try {
            executor.execute(() -> {
                try {
                    delegate.log(message);
                } catch (RuntimeException e) {
                    // nobody is waiting for this task -> report, never kill the worker silently
                    System.err.println("async sink failed: " + e.getMessage());
                }
            });
        } catch (RejectedExecutionException e) {
            // already closed -> fall back to writing synchronously instead of losing the log
            delegate.log(message);
        }
    }

    // flush queued logs, then close the real sink.
    // must be called on app shutdown (Logger.shutdown() does it), otherwise queued logs can be lost
    @Override
    public void close() {
        executor.shutdown(); // stop accepting new logs, finish queued ones
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        delegate.close();
    }
}
