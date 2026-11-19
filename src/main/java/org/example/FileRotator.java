package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

public class FileRotator {
    private final long maxFileSizeInBytes;

    public FileRotator(long maxFileSizeInBytes) {
        this.maxFileSizeInBytes = maxFileSizeInBytes;
    }

    public boolean shouldRotate(Path logFile, long incomingBytes) throws IOException {
        if (!Files.exists(logFile)) return false;
        long currentSize = Files.size(logFile);
        return currentSize > 0 && currentSize + incomingBytes > maxFileSizeInBytes;
    }

    public void rotate(Path logFile) throws IOException {
        // finding next available index
        int index = 1;
        while (Files.exists(backupPath(logFile, index))) {
            index++;
        }
        // index found - putting existing log filr into new compresssed file
        compress(logFile, backupPath(logFile, index));

        Files.delete(logFile);
    }

    private void compress(Path source, Path target) throws IOException {
        // using try with resouce here so it closes both the streams being opened
        try (InputStream in = Files.newInputStream(source);
             OutputStream out = new GZIPOutputStream(Files.newOutputStream(target))) {
            // transfers all the input to the zip file
            in.transferTo(out);
        }
    }

    // application.log.2.gz (in the same folder)
    private Path backupPath(Path logFile, int index) {
        return logFile.resolveSibling(logFile.getFileName() + "." + index + ".gz");
    }
}
