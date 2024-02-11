package me.blueyescat.skriptlogs.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.blueyescat.skriptlogs.SkriptLogs;

public class LogAppender extends AbstractAppender {

	public LogAppender() {
		super("skript-logs", null, null, false);
		Logger rootLogger = (Logger) LogManager.getRootLogger();
		rootLogger.addAppender(this);
	}

	@Override
	public void append(LogEvent e) {
		if (!SkriptLogs.getInstance().isEnabled())
			return;
		LogEvt logEvent = new LogEvt(e.toImmutable(), e.getMessage());
		var immutableEvent = e.toImmutable();
		new BukkitRunnable() {
			public void run() {
				if (!e.getMessage().getFormattedMessage().trim().equals("")) {
					SkriptLogs.getInstance().lastMessage = immutableEvent.getMessage().getFormattedMessage().replaceAll("\\u001B\\[[;\\d]*m", "");
					Bukkit.getServer().getPluginManager().callEvent(logEvent);
				}	
			}
		}.runTask(SkriptLogs.getInstance());
	}

}

