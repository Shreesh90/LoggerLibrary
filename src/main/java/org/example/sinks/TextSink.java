package org.example.sinks;

import org.example.FileRotator;
import org.example.LogFormatter;
import org.example.LoggerMessage;
import org.example.configuration.TextSinkConfiguration;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TextSink implements Sink {
    private final Path logFile;
    LogFormatter logFormatter;

    // ROTATION: handles "file too big -> compress it and start a new one".
    private final FileRotator fileRotator;

    public TextSink(SinkConfiguration textSinkConfiguration) {
        TextSinkConfiguration config = (TextSinkConfiguration) textSinkConfiguration;
        this.logFile = Path.of(config.fileLocation);
        this.logFormatter = new LogFormatter();
        this.fileRotator = new FileRotator(config.maxFileSizeInBytes);
    }

    @Override
    public void log(LoggerMessage loggerMessage) {
        String line = logFormatter.format(loggerMessage) + System.lineSeparator();
        try {
            Files.createDirectories(logFile.toAbsolutePath().getParent());

            // ROTATION: size check uses bytes (not characters), since file size is in bytes.
            long lineSizeInBytes = line.getBytes(StandardCharsets.UTF_8).length;
            if (fileRotator.shouldRotate(logFile, lineSizeInBytes)) {
                fileRotator.rotate(logFile);
            }

            Files.writeString(logFile, line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("not able to log");
        }
    }
}
