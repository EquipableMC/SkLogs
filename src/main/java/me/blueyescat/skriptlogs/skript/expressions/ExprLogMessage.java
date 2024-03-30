package me.blueyescat.skriptlogs.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import me.blueyescat.skriptlogs.util.LogEvt;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Blueyescat, Equipable
 */
@Name("Logged Message")
@Description("Returns the logged message in a log event.")
@Examples({"set {_message} to the logged message"})
@Since("1.0.0")
public class ExprLogMessage extends SimpleExpression<String> {
  
  static {
    Skript.registerExpression(ExprLogMessage.class, String.class, ExpressionType.SIMPLE, "[the] log[ged] message");
  }
  
  @Override
  public boolean init(final Expression<?> @NotNull [] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final @NotNull ParseResult parseResult) {
    if (!getParser().isCurrentEvent(LogEvt.class)) {
      Skript.error("The logged message expression can't be used outside of an on log event! Use last logged message if you want to use logged messages outside of an on log event!");
      return false;
    }
    return true;
  }
  
  private String CleanedUpMsg(String msg) {
    return msg.replaceAll("\\s*§[0-9A-Fa-fKkLlMmNnOoRrXx]\\s*", "");
  }
  
  @Override
  protected String @NotNull [] get(final @NotNull Event e) {
    LogEvt event = (LogEvt) e;
    long timestampMillis;
    timestampMillis = event.getTimeMillis();
    LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillis), ZoneId.systemDefault());
    String formattedtime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateTime);
    String logname = ((LogEvt) e).getLogEvent().getLoggerName();
    String formattedmsg = ((LogEvt) e).getLogEvent().getMessage().getFormattedMessage();
    String cleanedmsg = CleanedUpMsg(formattedmsg);
    String logmsg = "[" + formattedtime + "] " +
      "[" + logname + "] " +
      cleanedmsg;
    if (Bukkit.getPluginManager().isPluginEnabled("Disky")) {
      if (logmsg.length() >= 2000) {
        System.out.println("§cCould not log this" + logname + "§c's message as it exceeds 2000 characters! (Prevents a server crash if using with DiSky!)");
        throw new IllegalArgumentException("Message exceeds 2000 characters!");
      }
    }
    
		if (logmsg.isBlank())
			return new String[]{};
		
    return CollectionUtils.array(logmsg);
  }
  
  @Override
  public boolean isSingle() {
    return true;
  }
  
  @Override
  public @NotNull Class<? extends String> getReturnType() {
    return String.class;
  }
  
  @Override
  public @NotNull String toString(final @Nullable Event e, final boolean debug) {
    return "logged message";
  }
  
}
