package me.blueyescat.skriptlogs.skript.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.ScriptLoader;
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

import me.blueyescat.skriptlogs.SkriptLogs;

import me.blueyescat.skriptlogs.util.LogEvt;

/**
 * @author Blueyescat, Equipable
 */
@Name("Last Logged Message")
@Description("Returns the last logged message")
@Examples({"set {_message} to the last logged message"})
@Since("1.1.0")
public class ExprLastLogMessage extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprLastLogMessage.class, String.class, ExpressionType.SIMPLE, "[the] last log[ged] message");
	}

	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		return new String[] { SkriptLogs.getInstance().lastMessage };
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
		return "the last logged message";
	}

}
