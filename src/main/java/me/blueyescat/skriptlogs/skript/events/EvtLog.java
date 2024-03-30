package me.blueyescat.skriptlogs.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import me.blueyescat.skriptlogs.util.LogEvt;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * @author Blueyescat, Equipable
 */
public class EvtLog extends SkriptEvent {
  
  static {
    Skript.registerEvent("Log", EvtLog.class, LogEvt.class, "[console] log")
      .description("This is called when a message is sent in console. Note that using broadcast on the on log event will infinitely loop.")
      .examples("on log:")
      .since("1.0.0");
  }
  
  @Override
  public boolean init(final Literal<?> @NotNull [] args, final int matchedPattern, final @NotNull ParseResult parser) {
    return true;
  }
  
  @Override
  public boolean check(final @NotNull Event e) {
    return e instanceof LogEvt;
  }
  
  @Override
  public @NotNull String toString(final @Nullable Event e, final boolean debug) {
    return "on log";
  }
  
}
