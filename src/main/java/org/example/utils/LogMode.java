package org.example.utils;

// (Extension 2)
// SYNC  -> caller thread writes to the sink itself (simple, log is on disk when log() returns, slower)
// ASYNC -> caller only hands the message to a background thread (fast, may lose logs on crash)
public enum LogMode {
    SYNC,
    ASYNC
}
