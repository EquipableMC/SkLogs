package me.blueyescat.skriptlogs.util;

import org.apache.logging.log4j.core.LogEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Blueyescat, Equipable
 */
public class BukkitLogEvent extends Event {
  
  private static final HandlerList handlers = new HandlerList();
  private final LogEvent logEvent;
  private final String message;
  
  public BukkitLogEvent(final LogEvent e, final String message) {
    super(!Bukkit.isPrimaryThread());
    logEvent = e;
    this.message = message;
  }
  
  public static HandlerList getHandlerList() {
    return handlers;
  }
  
  public LogEvent getLogEvent() {
    return logEvent;
  }
  
  @Override
  public @NotNull HandlerList getHandlers() {
    return handlers;
  }
  
  public long getTimeMillis() {
    return System.currentTimeMillis();
  }

  public String getMessage() {
    return message;
  }

}
