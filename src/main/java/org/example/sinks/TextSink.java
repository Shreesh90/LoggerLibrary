package org.example.sinks;

import org.example.LogFormatter;
import org.example.LoggerMessage;
import org.example.configuration.TextSinkConfiguration;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TextSink implements Sink {
    private final Path logFile;
    LogFormatter logFormatter;

    public TextSink(SinkConfiguration textSinkConfiguration) {
        this.logFile = Path.of(((TextSinkConfiguration) textSinkConfiguration).fileLocation);
        this.logFormatter = new LogFormatter();
    }

    @Override
    public void log(LoggerMessage loggerMessage) {
        String line = logFormatter.format(loggerMessage) + System.lineSeparator();
        try {
            Files.createDirectories(logFile.toAbsolutePath().getParent());
            Files.writeString(logFile, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            // avoiding logger library to throw error
            System.err.println("not able to log");
        }
    }
}
