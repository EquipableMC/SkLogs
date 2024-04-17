package me.blueyescat.skriptlogs.util;

import ch.njol.skript.util.SkriptColor;
import ch.njol.util.StringUtils;
import me.blueyescat.skriptlogs.SkriptLogs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
* @author Blueyescat, Equipable
*/
public class LogAppender extends AbstractAppender {

  public LogAppender() {
      super("skript-logs", null, null, false);
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

    /**
     * @param input string
     * @return the color stripped string
     * @author <a href="https://github.com/SkriptLang/Skript/blob/master/src/main/java/ch/njol/skript/expressions/ExprRawString.java">Skript's ExprRawString</a>
     */
    private static String strip(String input) {

        String raw = SkriptColor.replaceColorChar(input);
        if (raw.toLowerCase().contains("&x")) {
            raw = StringUtils.replaceAll(raw, HEX_PATTERN, matchResult ->
                    "<#" + matchResult.group(1).replace("&", "") + '>');
        }
        return raw;
    }

}

