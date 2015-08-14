package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;
import org.satvika.jyotish.Rashi;

public class MoonRashiView extends RashiView {

	public MoonRashiView(Rashi rashi, DateTime dateTime) {
		super(rashi, dateTime);
	}

	@Override
	public String render() {
		return render("Луна в " + getDisplayName());
	}
}
