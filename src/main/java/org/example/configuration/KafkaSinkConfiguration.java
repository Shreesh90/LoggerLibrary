package org.example.configuration;

import org.example.interfaces.SinkConfiguration;

// (Extension 3) config for the new Kafka sink
public class KafkaSinkConfiguration implements SinkConfiguration {
    public final String brokers;
    public final String topic;

    public KafkaSinkConfiguration(String brokers, String topic) {
        this.brokers = brokers;
        this.topic = topic;
    }
}
