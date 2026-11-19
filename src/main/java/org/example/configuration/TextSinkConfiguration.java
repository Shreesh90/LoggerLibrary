package org.example.configuration;

import org.example.interfaces.SinkConfiguration;

public class TextSinkConfiguration implements SinkConfiguration {
    // 1 KB default log file configuration
    private static final long DEFAULT_MAX_FILE_SIZE_IN_BYTES = 1024;

    public String fileLocation;
    public long maxFileSizeInBytes;

    public TextSinkConfiguration(String fileLocation) {
        this(fileLocation, DEFAULT_MAX_FILE_SIZE_IN_BYTES);
    }

    public TextSinkConfiguration(String fileLocation, long maxFileSizeInBytes) {
        this.fileLocation = fileLocation;
        this.maxFileSizeInBytes = maxFileSizeInBytes;
    }
}
