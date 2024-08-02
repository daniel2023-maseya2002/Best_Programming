package registration.auca.student;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class LoggerConfig {
    public static final Logger logger = Logger.getLogger(LoggerConfig.class.getName());

    static {
        try {
            setupLogger();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to setup logger", e);
        }
    }

    private static void setupLogger() throws IOException {
        // Remove the default console handler
        Logger rootLogger = Logger.getLogger("");
        rootLogger.removeHandler(rootLogger.getHandlers()[0]);

        // Add custom console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new CustomFormatter());
        rootLogger.addHandler(consoleHandler);

        // Add file handler
        FileHandler fileHandler = new FileHandler("application.log", true);
        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(new CustomFormatter());
        rootLogger.addHandler(fileHandler);

        // Set the logger level
        rootLogger.setLevel(Level.ALL);
    }

    static class CustomFormatter extends Formatter {
        private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

        @Override
        public String format(LogRecord record) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
            String timestamp = simpleDateFormat.format(new Date(record.getMillis()));
            return String.format("%s [%s] %s %n", timestamp, record.getLevel(), record.getMessage());
        }
    }
}
