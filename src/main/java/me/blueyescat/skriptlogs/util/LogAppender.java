package me.blueyescat.skriptlogs.util;

import me.blueyescat.skriptlogs.SkriptLogs;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
* @author Blueyescat, Equipable
*/
public class LogAppender extends AbstractAppender {

  public LogAppender() {
      super("skript-logs", null, null, false, Property.EMPTY_ARRAY);
      Logger rootLogger = (Logger) LogManager.getRootLogger();
      rootLogger.addAppender(this);
  }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void append(LogEvent logEvent) {

        String formattedTime = LocalDateTime.now().format(formatter);
        String formattedMsg = logEvent.getMessage().getFormattedMessage();
        String cleanedLogMsg = COLOR_CODE_PATTERN.matcher(formattedMsg).replaceAll("");
        cleanedLogMsg = HEX_PATTERN.matcher(cleanedLogMsg).replaceAll("");
        cleanedLogMsg = ANSI_ESCAPE_PATTERN.matcher(cleanedLogMsg).replaceAll("");
        cleanedLogMsg = WHITESPACE_PATTERN.matcher(cleanedLogMsg).replaceAll(" ");
        String loggerName = logEvent.getLoggerName() == null || logEvent.getLoggerName().isEmpty() ? "Server" : logEvent.getLoggerName();
        var split = loggerName.split("\\.");
        if (split.length != 0) {
            loggerName = split[split.length - 1];
        }
        Level loglevel = logEvent.getLevel();
        String logMessage = "[" + formattedTime + " " + loglevel + "] " + "[" + loggerName + "] " + cleanedLogMsg;

        Bukkit.getPluginManager().callEvent(new BukkitLogEvent(logEvent, logMessage));
        SkriptLogs.getInstance().lastMessage = logMessage;


    }

    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("[&§][0-9A-FK-ORX]", Pattern.CASE_INSENSITIVE);
    private static final Pattern HEX_PATTERN = Pattern.compile("&x((?:&\\p{XDigit}){6})", Pattern.CASE_INSENSITIVE);
    private static final Pattern ANSI_ESCAPE_PATTERN = Pattern.compile("\u001B\\[[;\\d]*m", Pattern.CASE_INSENSITIVE);
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
}


