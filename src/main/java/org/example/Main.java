package org.example;

import org.example.configuration.LoggerConfiguration;
import org.example.configuration.TextSinkConfiguration;
import org.example.interfaces.Sink;
import org.example.utils.LogLevel;
import org.example.utils.SinkType;

public class Main {
    public static void main(String[] args) {
        // multiple sink, each tied to different log levels
        // DB sink not implemented so created two sinks for text itself.
        Sink sink1 = SinkFactory.getSink(SinkType.TEXT,  new TextSinkConfiguration("src/main/logs/application.log"));
        Sink sink2 = SinkFactory.getSink(SinkType.TEXT,  new TextSinkConfiguration("src/main/logs/error.log"));

        // we defined multiple sinks above, now defining what log level wach sink handles
        // one or more message level can have the same sink (requirement)
        // one or more message level can have the same sink (requirement)
        // A message level is tied to a sink. (requirement) [each log level has only one sink corresponding to it]
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration("dd-MM-yyyy HH:mm:ss")
                .addSink(sink1, LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARN)
                .addSink(sink2, LogLevel.ERROR);

        // client only calls the log level, namespace and message
        Logger logger = new Logger(loggerConfiguration);
        logger.log(LogLevel.DEBUG, "startup", "debug message");
        logger.log(LogLevel.INFO, "payments", "Empty txnIds Nothing to fetch");
        logger.log(LogLevel.WARN, "users", "No user found for the phone number");
        logger.log(LogLevel.ERROR, "payments", "Payment gateway timed out");
        logger.log(LogLevel.FATAL, "db", "Connection pool exhausted");
    }
}
