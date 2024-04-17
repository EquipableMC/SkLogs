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
import me.blueyescat.skriptlogs.util.BukkitLogEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

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
  public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
    if (!getParser().isCurrentEvent(BukkitLogEvent.class)) {
      Skript.error("The 'logged message' expression can't be used outside of an on log event! Use 'last logged message' if you want to use logged messages outside of an on log event!");
      return false;
    }
    return true;
  }
  
  @Override
  protected String [] get(final Event e) {
    return CollectionUtils.array(((BukkitLogEvent) e).getMessage());
  }
  
  @Override
  public boolean isSingle() {
    return true;
  }
  
  @Override
  public Class<? extends String> getReturnType() {
    return String.class;
  }
  
  @Override
  public String toString(final @Nullable Event e, final boolean debug) {
    return "logged message";
  }
  
}
