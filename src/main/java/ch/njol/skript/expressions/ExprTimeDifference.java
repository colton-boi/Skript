/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.skript.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.util.Date;
import ch.njol.skript.util.Timespan;

@Name("Time Since/Until Dates")
@Description("The time since a date has passed or the time until a date will pass.")
@Examples({
	"send \"%time since 5 minecraft days ago% has passed since 5 minecraft days ago!\" to player",
	"send \"%time until {countdownEnd}% remaining of the countdown!\" to player"
})
@Since("2.5, INSERT VERSION (time until)")
public class ExprTimeDifference extends SimplePropertyExpression<Date, Timespan> {

	static {
		Skript.registerExpression(ExprTimeDifference.class, Timespan.class, ExpressionType.PROPERTY,
			"[the] time since %dates%",
			"[the] [remaining] time [remaining] until %dates%");
	}

	private boolean sinceDate;

	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
		sinceDate = matchedPattern == 0;
		return true;
	}

	@Override
	@Nullable
	public Timespan convert(Date date) {
		Date now = Date.now();

		/*
		 * This condition returns whether the date the player is using is
		 * before the current date, the same as the current date, or after the current date.
		 * A value less than 0 indicates that the new date is BEFORE the current date.
		 * A value of 0 indicates that the new date is the SAME as the current date.
		 * A value greater than 0 indicates that the new date is AFTER the current date.
		 */
		if (sinceDate ? (date.compareTo(now) < 1) : (date.compareTo(now) > -1))
			return date.difference(now);
		return null;
	}

	@Override
	public Class<? extends Timespan> getReturnType() {
		return Timespan.class;
	}

	@Override
	protected String getPropertyName() {
		return "time " + (sinceDate ? "since" : "until");
	}

	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "the time " + (sinceDate ? "since " : "until ") + getExpr().toString(e, debug);
	}

}
