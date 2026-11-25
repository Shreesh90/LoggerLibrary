package org.example.sinks;

import org.example.LogFormatter;
import org.example.LoggerMessage;
import org.example.configuration.KafkaSinkConfiguration;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;

// (Extension 3) A brand new sink added WITHOUT touching SinkFactory.
// It only implements Sink; the client registers it with SinkFactory.register(...)
public class KafkaSink implements Sink {
    private final KafkaSinkConfiguration config;
    private final LogFormatter logFormatter;

    public KafkaSink(SinkConfiguration sinkConfiguration, LogFormatter logFormatter) {
        this.config = (KafkaSinkConfiguration) sinkConfiguration;
        this.logFormatter = logFormatter;
    }

    @Override
    public void log(LoggerMessage message) {
        // real impl: kafkaProducer.send(new ProducerRecord<>(topic, correlationId, payload))
        // KafkaProducer is itself thread safe, so no lock needed here
        System.out.println("[kafka -> " + config.topic + "] " + logFormatter.format(message));
    }
}
