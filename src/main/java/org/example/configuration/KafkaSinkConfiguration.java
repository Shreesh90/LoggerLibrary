package org.example.configuration;

import org.example.LogFormatter;
import org.example.interfaces.Sink;
import org.example.interfaces.CustomSinkConfiguration;
import org.example.sinks.KafkaSink;

// (Extension 3) config for the new Kafka sink
public class KafkaSinkConfiguration implements CustomSinkConfiguration {
    public final String brokers;
    public final String topic;

    public KafkaSinkConfiguration(String brokers, String topic) {
        this.brokers = brokers;
        this.topic = topic;
    }

    // (Extension 3) this config builds its own sink
    @Override
    public Sink createSink(LogFormatter formatter) {
        return new KafkaSink(this, formatter);
    }
}
