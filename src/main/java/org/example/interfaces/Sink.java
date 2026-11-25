package org.example.interfaces;

import org.example.LoggerMessage;

public interface Sink {
    void log(LoggerMessage message) ;

    // (Extension 2) lifecycle hook: release resources / flush pending logs.
    // default = do nothing, so existing and custom sinks don't have to implement it
    default void close() {}
}
