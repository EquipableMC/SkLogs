package me.blueyescat.skriptlogs.util;

import me.blueyescat.skriptlogs.SkriptLogs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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
  
  private static final Pattern COLORED_MESSAGE_PATTERN = Pattern.compile("([§&])([0-9A-Fa-fk-orx]|#([0-9A-Fa-f]{6}))");
  
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  
  @Override
  public void append(LogEvent e) {
    if (!SkriptLogs.getInstance().isEnabled())
      return;
    LogEvt logEvent = new LogEvt(e.toImmutable(), e.getMessage());
    new BukkitRunnable() {
      @Override
      public void run() {
        LogEvt logEvent = (LogEvt) e;
        String formattedtime = LocalDateTime.now().format(formatter);
        String logname = logEvent.getLogEvent().getLoggerName();
        String formattedmsg = logEvent.getLogEvent().getMessage().getFormattedMessage();
        String cleanedmsg = COLORED_MESSAGE_PATTERN.matcher(formattedmsg).replaceAll("");
        String logMessage = "[" + formattedtime + "] " +
          "[" + logname + "] " +
          cleanedmsg;
        
        Bukkit.getServer().getPluginManager().callEvent(logEvent);
        SkriptLogs.getInstance().lastMessage = logMessage;
        
      }
    }.runTask(SkriptLogs.getInstance());
  }
  
}

