package org.example;

import org.example.configuration.LoggerConfiguration;
import org.example.configuration.TextSinkConfiguration;
import org.example.interfaces.Sink;
import org.example.interfaces.SinkConfiguration;
import org.example.utils.LogLevel;
import org.example.utils.SinkType;

public class Main {
    public static void main(String[] args) throws Exception {
        TextSinkConfiguration textSinkConfiguration = new TextSinkConfiguration();
        textSinkConfiguration.setFile_location("");

        LoggerConfiguration loggerConfiguration =
                new LoggerConfiguration("dd-MM-yyyy HH:mm:ss", LogLevel.DEBUG, SinkType.TEXT, textSinkConfiguration);

        Logger logger = new Logger(loggerConfiguration);
        logger.log("Hello");
    }
}