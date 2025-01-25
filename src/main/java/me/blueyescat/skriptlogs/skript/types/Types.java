package me.blueyescat.skriptlogs.skript.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.EnumUtils;
import org.apache.logging.log4j.spi.StandardLevel;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.converter.Converters;

/**
 * @author Blueyescat, Equipable
 */
public class Types {
  
  static {
    final EnumUtils<StandardLevel> levels = new EnumUtils<>(StandardLevel.class, "log levels");
    Classes.registerClass(new ClassInfo<>(StandardLevel.class, "loglevel")
      .user("log ?levels?")
      .name("Log Level")
      .description("")
      .usage("info, warning, error, fatal, trace, debug, all")
      .examples("")
      .since("1.0.0")
      .defaultExpression(new EventValueExpression<>(StandardLevel.class))
      .parser(new Parser<>() {
        
        @Override
        @Nullable
        public StandardLevel parse(@NotNull String s, @NotNull ParseContext context) {
          return levels.parse(s);
        }
        
        @Override
        public @NotNull String toString(StandardLevel o, int flags) {
          return levels.toString(o, flags);
        }
        
        @Override
        @SuppressWarnings("null")
        public @NotNull String toVariableNameString(StandardLevel o) {
          return o.name();
        }
        
      }));
    
    // Log levels have a type just for enum values. They should act like strings.
    Converters.registerConverter(StandardLevel.class, String.class, logLevel ->
      levels.toString(logLevel, 0)
    );
    
  }
  
}
