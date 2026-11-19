package org.example.configuration;

import org.example.interfaces.SinkConfiguration;

public class TextSinkConfiguration implements SinkConfiguration {
    private String file_location;

    public void setFile_location(String file_location) {
        this.file_location = file_location;
    }

    public String getFile_location() {
        return file_location;
    }
}
