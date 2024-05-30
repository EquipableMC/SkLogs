package me.blueyescat.skriptlogs.util;

import java.util.regex.Matcher;
import me.blueyescat.skriptlogs.SkriptLogs;
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
    public void append(LogEvent e) {
        if (!SkriptLogs.getInstance().isEnabled())
            return;
        String formattedTime = LocalDateTime.now().format(formatter);
        String formattedMsg = strip(e.getMessage().getFormattedMessage());
        String logMessage = "[" + formattedTime + "] " + "[" + e.getLoggerName() + "] " + formattedMsg;
        Bukkit.getPluginManager().callEvent(new BukkitLogEvent(e, logMessage));
        SkriptLogs.getInstance().lastMessage = logMessage;
    }
    private static final Pattern HEX_PATTERN = Pattern.compile("&x((?:&\\p{XDigit}){6})");
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("[&§][0-9A-FK-ORX]");
    private static final Pattern ANSI_ESCAPE_PATTERN = Pattern.compile("\\u001B\\[[;\\d]*m");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Pattern BUNGEE_CODE_PATTERN = Pattern.compile("(?i)" + String.valueOf('§'));

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

        // Color Codes
        Matcher colorCodeMatcher = COLOR_CODE_PATTERN.matcher(hexBuffer.toString());
        StringBuffer colorCodeBuffer = new StringBuffer();
        while (colorCodeMatcher.find()) {
            colorCodeMatcher.appendReplacement(colorCodeBuffer, "");
        }
        colorCodeMatcher.appendTail(colorCodeBuffer);

        // Bungee Color Codes
        Matcher bungeeColorCodeMatcher = BUNGEE_CODE_PATTERN.matcher(colorCodeBuffer.toString());
        StringBuffer bungeeColorCodeBuffer = new StringBuffer();
        while (bungeeColorCodeMatcher.find()) {
            bungeeColorCodeMatcher.appendReplacement(bungeeColorCodeBuffer, "");
        }
        colorCodeMatcher.appendTail(bungeeColorCodeBuffer);

        // ANSI
        Matcher ansiEscapeMatcher = ANSI_ESCAPE_PATTERN.matcher(bungeeColorCodeBuffer.toString());
        StringBuffer ansiBuffer = new StringBuffer();
        while (ansiEscapeMatcher.find()) {
            ansiEscapeMatcher.appendReplacement(ansiBuffer, "");
        }
        ansiEscapeMatcher.appendTail(ansiBuffer);

        // whitespace
        String stripped = ansiBuffer.toString().trim();
        stripped = WHITESPACE_PATTERN.matcher(stripped).replaceAll(" ");

        return stripped;
    }

}

