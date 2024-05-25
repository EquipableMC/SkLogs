package me.blueyescat.skriptlogs.util;

import ch.njol.skript.util.SkriptColor;
import java.util.regex.Matcher;
import me.blueyescat.skriptlogs.SkriptLogs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

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
    public void append(LogEvent e) {
        if (!SkriptLogs.getInstance().isEnabled())
            return;
        String formattedTime = LocalDateTime.now().format(formatter);
        String formattedMsg = strip(e.getMessage().getFormattedMessage());
        String logMessage = "[" + formattedTime + "] " + "[" + e.getLoggerName() + "] " + formattedMsg;
        Bukkit.getPluginManager().callEvent(new BukkitLogEvent(e, logMessage));
        SkriptLogs.getInstance().lastMessage = logMessage;
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)&x((?:&\\p{XDigit}){6})");
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    /**
     * Strips all color codes, including hex color codes, from the input string.
     *
     * @param input the string to process
     * @return the color-stripped string
     */
    private static String strip(String input) {
        // hex
        Matcher hexMatcher = HEX_PATTERN.matcher(input);
        StringBuffer hexBuffer = new StringBuffer();
        while (hexMatcher.find()) {
            hexMatcher.appendReplacement(hexBuffer, "");
        }
        hexMatcher.appendTail(hexBuffer);

        // color codes
        Matcher colorCodeMatcher = COLOR_CODE_PATTERN.matcher(hexBuffer.toString());
        StringBuffer colorCodeBuffer = new StringBuffer();
        while (colorCodeMatcher.find()) {
            colorCodeMatcher.appendReplacement(colorCodeBuffer, "");
        }
        colorCodeMatcher.appendTail(colorCodeBuffer);

        // whitespace
        String stripped = colorCodeBuffer.toString().trim();
        stripped = WHITESPACE_PATTERN.matcher(stripped).replaceAll("");

        return stripped;
    }

}

