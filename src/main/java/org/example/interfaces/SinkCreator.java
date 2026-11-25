package org.example.interfaces;

import org.example.LogFormatter;

// (Extension 3) "recipe" for building a sink. Usually passed as a constructor reference,
// e.g. KafkaSink::new  (matches constructor (SinkConfiguration, LogFormatter))
@FunctionalInterface
public interface SinkCreator {
    Sink create(SinkConfiguration configuration, LogFormatter formatter);
}
