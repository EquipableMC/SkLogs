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
@Examples({"log a warning \"test\"",
        "log errors \"test\" and \"error\"",
        "send a fatal error \"test\" to the console",
        "print in \"test\""})
@Since("1.0.0")
public class EffLogMessage extends Effect {

  static {
    Skript.registerEffect(EffLogMessage.class,
            "(log|print [in]) %loglevel% [[with [the]] message[s]] %strings%",
            "send %loglevel% [[with [the]] message[s]] %strings% to [the] console",
            "print [in] %strings%");
  }

  private Expression<StandardLevel> logLevel;
  private Expression<String> messages;
  private boolean usedPrint;

  @Override
  public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final @NotNull ParseResult parser) {
    usedPrint = matchedPattern == 2;
    if (!usedPrint) {
      logLevel = (Expression<StandardLevel>) exprs[0];
    }
    messages = (Expression<String>) exprs[usedPrint ? 0 : 1];
    return true;
  }

  @Override
  protected void execute(final @NotNull Event event) {
    if ((logLevel == null && !usedPrint) || messages == null) return;

    Logger logger = LogManager.getRootLogger();
    Level level = usedPrint ? Level.INFO : Level.valueOf(logLevel.getSingle(event).toString());
    for (String message : messages.getArray(event)) {
      logger.log(level, message);
    }
  }

  @Override
  public @NotNull String toString(final @Nullable Event event, final boolean debug) {
    if (usedPrint) {
      return "print " + messages.toString(event, debug);
    } else {
      return "log " + logLevel.toString(event, debug) + " message " + messages.toString(event, debug);
    }
  }
}
