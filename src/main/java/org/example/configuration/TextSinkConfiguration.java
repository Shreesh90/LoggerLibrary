package org.example.configuration;

import org.example.interfaces.SinkConfiguration;

public class TextSinkConfiguration implements SinkConfiguration {
    public String fileLocation;

    public TextSinkConfiguration(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
