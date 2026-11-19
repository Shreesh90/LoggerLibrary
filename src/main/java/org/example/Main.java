package org.example;

import org.example.configuration.DBSinkConfiguration;
import org.example.configuration.LoggerConfiguration;
import org.example.configuration.TextSinkConfiguration;
import org.example.interfaces.Sink;
import org.example.sinks.DBSink;
import org.example.utils.LogLevel;
import org.example.utils.SinkType;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // multiple sink, each tied to different log levels
        // Added file limit so we can rotate the log files when its size reached the limit added here
        Sink sink1 = SinkFactory.getSink(SinkType.TEXT,  new TextSinkConfiguration("src/main/logs/application.log",500));
        Sink sink2 = SinkFactory.getSink(SinkType.TEXT,  new TextSinkConfiguration("src/main/logs/error.log"));

        // in-memory DB sink (stores messages in a List) (requirement)
        Sink dbSink = SinkFactory.getSink(SinkType.DB, new DBSinkConfiguration("locahost", "8080"));

        // we defined multiple sinks above, now defining what log level wach sink handles
        // one or more message level can have the same sink (requirement)
        // A message level is tied to a sink. (requirement) [each log level has only one sink corresponding to it]
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration("dd-MM-yyyy HH:mm:ss")
                .addSink(sink1, LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARN)
                .addSink(sink2, LogLevel.ERROR)
                .addSink(dbSink, LogLevel.FATAL);

        // client only calls the log level, namespace and message
        Logger logger = new Logger(loggerConfiguration);

        // no trackingId set yet -> logged as "-"
        logger.log(LogLevel.DEBUG, "1", "debug message");
        logger.log(LogLevel.INFO, "2", "Empty txnIds Nothing to fetch");
        logger.log(LogLevel.WARN, "3", "No user found for the phone number");
        logger.log(LogLevel.ERROR, "4", "Payment gateway timed out");
        logger.log(LogLevel.FATAL, "5", "Connection pool exhausted");

        // read the DB Sink List
        for (LoggerMessage row : ((DBSink) dbSink).getLogs()) {
            System.out.println("DB row -> " + new LogFormatter().format(row));
        }
    }
}
