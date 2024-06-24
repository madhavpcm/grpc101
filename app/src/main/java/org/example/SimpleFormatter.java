package org.example;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * {@summary Custom Formatter class}
 */
public class SimpleFormatter extends Formatter {
    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public String format(LogRecord record) {
        return String.format("[%s] [%s]: %s%n",
                dtFormatter.format(record.getInstant().atZone(ZoneId.systemDefault())),
                record.getLevel().getName(),
                record.getMessage());
    }
}
