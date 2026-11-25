package org.example.formatters;

import org.example.LogFormatter;
import org.example.LoggerMessage;

// (Extension 4) JSON output - useful for Kafka / ELK style sinks
// (hand-built string to avoid adding a JSON library dependency)
public class JsonFormatter implements LogFormatter {
    // timestamp uses Instant.toString() -> ISO-8601 in UTC, e.g. 2026-11-25T06:35:05.123Z
    // (sortable and understood by every log tool, so no custom pattern needed)
    @Override
    public String format(LoggerMessage m) {
        return "{\"time\":\"" + m.timestamp + "\""
                + ",\"level\":\"" + m.logLevel + "\""
                + ",\"namespace\":\"" + m.namespace + "\""
                + ",\"host\":\"" + m.hostName + "\""
                + ",\"correlationId\":\"" + m.correlationId + "\""
                + ",\"message\":\"" + m.message + "\"}";
    }
}
