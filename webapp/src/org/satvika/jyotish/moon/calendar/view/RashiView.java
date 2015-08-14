package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;
import org.satvika.jyotish.Rashi;

public abstract class RashiView extends SimpleEntityView {

	private Rashi rashi;

	public RashiView(Rashi rashi, DateTime dateTime) {
		super(dateTime);
		this.rashi = rashi;
	}

	public Rashi getRashi() {
		return this.rashi;
	}

	public String getDisplayName() {
		return getTranslator().translate(getRashi().toString());
	}
}
