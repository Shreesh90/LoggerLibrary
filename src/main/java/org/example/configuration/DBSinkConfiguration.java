package org.example.configuration;

import org.example.interfaces.SinkConfiguration;

public class DBSinkConfiguration implements SinkConfiguration {
    private String dbHost;
    private String dbPort;

    public DBSinkConfiguration(String dbHost, String dbPort) {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
    }
}
