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
import java.util.concurrent.locks.ReentrantLock;

public class TextSink implements Sink {
    private final Path logFile;
    // (Extension 4) formatter is injected by the client, not created here
    private final LogFormatter logFormatter;

    // utility class to compress log files
    private final FileRotator fileRotator;

    // (Extension 1) one lock per sink (per file). Many threads can log at the same time,
    // but "check size -> rotate -> append" must happen as ONE step, otherwise two threads
    // could both rotate, or a line could be written into a file that is being compressed.
    private final ReentrantLock lock = new ReentrantLock();

    public TextSink(SinkConfiguration textSinkConfiguration, LogFormatter logFormatter) {
        TextSinkConfiguration config = (TextSinkConfiguration) textSinkConfiguration;
        this.logFile = Path.of(config.fileLocation);
        this.logFormatter = logFormatter;
        this.fileRotator = new FileRotator(config.maxFileSizeInBytes);
    }

    @Override
    public void log(LoggerMessage loggerMessage) {
        // formatting touches no shared state -> done OUTSIDE the lock (keeps lock short)
        String line = logFormatter.format(loggerMessage) + System.lineSeparator();

        lock.lock(); // (Extension 1) critical section starts
        try {
            Files.createDirectories(logFile.toAbsolutePath().getParent());

            long lineSizeInBytes = line.getBytes(StandardCharsets.UTF_8).length;
            if (fileRotator.shouldRotate(logFile, lineSizeInBytes)) {
                fileRotator.rotate(logFile);
            }

            Files.writeString(logFile, line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("not able to log");
        } finally {
            lock.unlock(); // always release, even on exception
        }
    }
}
