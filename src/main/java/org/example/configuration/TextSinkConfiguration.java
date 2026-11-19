package org.example.configuration;

import org.example.interfaces.SinkConfiguration;

public class TextSinkConfiguration implements SinkConfiguration {
    private String fileLocation;

    public void setFile_location(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFile_location() {
        return fileLocation;
    }
}
