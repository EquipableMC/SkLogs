package me.blueyescat.skriptlogs.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.StandardLevel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * Logs a message with the given log level. The last pattern uses 'info' level.
 */
@Name("Log Message")
@Description({"Logs a message with the given log level. The last pattern uses 'info' level."})
@Examples({"log \"test\""})
@Since("1.0.0")
public class EffLogMessage extends Effect {

  static {
    Skript.registerEffect(EffLogMessage.class,
            "(print|send|log) %strings% with [the|a] severity [of] %loglevel% to [the] console");
  }

  private Expression<String> messages;
  private Expression<StandardLevel> logLevel;

  @Override
  public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final @NotNull ParseResult parser) {
    messages = (Expression<String>) exprs[0];
    logLevel = (Expression<StandardLevel>) exprs[1];
    return true;
  }

  @Override
  protected void execute(final @NotNull Event event) {
    if ((logLevel == null) || messages == null) return;

    Logger logger = LogManager.getRootLogger();
    Level level = Level.valueOf(logLevel.getSingle(event).toString());
    for (String message : messages.getArray(event)) {
      logger.log(level, message);
    }
  }

  @Override
  public @NotNull String toString(final @Nullable Event event, final boolean debug) {
      return "log " + messages.toString(event, debug) + " with severity " + logLevel.toString(event, debug) + " to the console";
  }
}
