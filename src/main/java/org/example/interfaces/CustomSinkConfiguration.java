package org.example.interfaces;

import org.example.LogFormatter;

// (Extension 3) For sinks written OUTSIDE the library (Kafka, Slack, ...).
// The config knows which Sink it builds (Factory Method), so SinkFactory never needs editing.
// Built-in TEXT/DB sinks don't need this - the factory's switch creates them.
public interface CustomSinkConfiguration extends SinkConfiguration {
    Sink createSink(LogFormatter formatter);
}
