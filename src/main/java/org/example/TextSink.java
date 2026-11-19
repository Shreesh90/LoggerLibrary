package org.example;

import org.example.configuration.LoggerConfiguration;
import org.example.configuration.TextSinkConfiguration;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextSink extends Sink {
    private SinkConfiguration textSinkConfiguration;

    public TextSink(SinkConfiguration textSinkConfiguration) {
        this.textSinkConfiguration = textSinkConfiguration;
    }

    @Override
    public void log(LoggerMessage loggerMessage) {
        System.out.println(loggerMessage.toString());
    }
}
