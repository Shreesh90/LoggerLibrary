package org.example;

import org.example.configuration.DBSinkConfiguration;
import org.example.configuration.KafkaSinkConfiguration;
import org.example.configuration.LoggerConfiguration;
import org.example.configuration.TextSinkConfiguration;
import org.example.formatters.JsonFormatter;
import org.example.formatters.PlainFormatter;
import org.example.interfaces.Sink;
import org.example.sinks.DBSink;
import org.example.sinks.KafkaSink;
import org.example.utils.LogLevel;
import org.example.utils.LogMode;
import org.example.utils.SinkType;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // (Extension 3) client registers a NEW sink type - SinkFactory is not edited
        SinkFactory.register("KAFKA", KafkaSink::new);

        // (Extension 4) client picks the formatter and injects it into the sink
        // date-time pattern is now part of the formatter
        LogFormatter plain = new PlainFormatter("dd-MM-yyyy HH:mm:ss");
        LogFormatter json = new JsonFormatter();

        // multiple sink, each tied to different log levels
        // Added file limit so we can rotate the log files when its size reached the limit added here
        Sink sink1 = SinkFactory.getSink(SinkType.TEXT, new TextSinkConfiguration("src/main/logs/application.log", 2000), plain);
        Sink sink2 = SinkFactory.getSink(SinkType.TEXT, new TextSinkConfiguration("src/main/logs/error.log"), json);

        // in-memory DB sink (stores messages in a List) (requirement)
        Sink dbSink = SinkFactory.getSink(SinkType.DB, new DBSinkConfiguration("locahost", "8080"), plain);

        // (Extension 3) new sink created through the same factory call
        Sink kafkaSink = SinkFactory.getSink("KAFKA", new KafkaSinkConfiguration("localhost:9092", "app-logs"), json);

        // one or more message level can have the same sink (requirement)
        // A message level is tied to a sink. (requirement)
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration()
                .addSink(sink1, LogLevel.DEBUG, LogLevel.INFO)
                .addSink(kafkaSink, LogLevel.WARN)
                .addSink(sink2, LogLevel.ERROR)
                .addSink(dbSink, LogLevel.FATAL)
                // (Extension 2) choose SYNC or ASYNC here - client code below is identical for both
                .setLogMode(LogMode.ASYNC);

        // client only calls the log level, namespace and message
        Logger logger = new Logger(loggerConfiguration);

        // (Extension 1 + 5) two "requests" running in parallel on different threads.
        // Each sets its own correlation id; locks in the sinks keep file writes safe.
        Runnable request = () -> {
            CorrelationContext.set(CorrelationContext.newId()); // start of request
            try {
                // Sink not being sent while sending the message to the library (requirement)
                // You specify message content, level and namespace while sending a message (requirement)
                logger.log(LogLevel.DEBUG, "1", "debug message");
                logger.log(LogLevel.INFO, "2", "Just a Info message");
                logger.log(LogLevel.WARN, "3", "No user found");
                logger.log(LogLevel.ERROR, "4", "Error in fetching credss");
                logger.log(LogLevel.FATAL, "5", "fatal error!!!");
            } finally {
                CorrelationContext.clear(); // end of request (important with thread pools)
            }
        };
        Thread t1 = new Thread(request);
        Thread t2 = new Thread(request);
        t1.start(); t2.start();
        t1.join(); t2.join();

        // (Extension 2) flush queued async logs before reading / exiting
        logger.shutdown();

        // read the DB Sink List
        for (LoggerMessage row : ((DBSink) dbSink).getLogs()) {
            System.out.println("DB row: " + plain.format(row));
        }
    }
}
