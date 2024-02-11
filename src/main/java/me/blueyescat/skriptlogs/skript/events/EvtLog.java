package me.blueyescat.skriptlogs.skript.events;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.blueyescat.skriptlogs.util.LogEvt;

/**
 * 
 */
public class EvtLog extends SkriptEvent {

	static {
		Skript.registerEvent("Log", EvtLog.class, LogEvt.class, "[console] log")
				.description("This is called when a message is sent in console. Note that using broadcast on the on log event will infinitely loop.")
				.examples("on log:")
				.since("1.0.0");
	}

	@Override
	public boolean init(final Literal<?>[] args, final int matchedPattern, final ParseResult parser) {
		return true;
	}

	@Override
	public boolean check(final Event e) {
		return e instanceof LogEvt;
	}

	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "on log";
	}

}
