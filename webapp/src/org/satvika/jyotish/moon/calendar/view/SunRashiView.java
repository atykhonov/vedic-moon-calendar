package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;
import org.satvika.jyotish.Rashi;

public class SunRashiView extends RashiView {

	public SunRashiView(Rashi rashi, DateTime dateTime) {
		super(rashi, dateTime);
	}

	@Override
	public String render() {
		return render("Солнце в " + getDisplayName());
	}
}