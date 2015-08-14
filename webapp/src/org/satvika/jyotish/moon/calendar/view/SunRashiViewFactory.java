package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;
import org.satvika.jyotish.Rashi;
import org.satvika.jyotish.moon.calendar.pools.DatePool;

public class SunRashiViewFactory extends ViewFactory<SunRashiView> {

	public SunRashiViewFactory(DatePool datePool, DateTime dateTime) {
		super(datePool, dateTime);
	}

	@Override
	public SunRashiView createView(int value, DateTime dateTime) {
		return new SunRashiView(Rashi.getByNumber(value), dateTime);
	}

}
