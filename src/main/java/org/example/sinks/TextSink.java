package org.example.sinks;

import org.example.LogFormatter;
import org.example.LoggerMessage;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;

public class TextSink implements Sink {
    private SinkConfiguration textSinkConfiguration;
    LogFormatter logFormatter;
    public TextSink(SinkConfiguration textSinkConfiguration) {
        this.textSinkConfiguration = textSinkConfiguration;
        this.logFormatter = new LogFormatter();
    }

    @Override
    public void log(LoggerMessage loggerMessage) {
        System.out.println(logFormatter.format(loggerMessage));
    }
}
