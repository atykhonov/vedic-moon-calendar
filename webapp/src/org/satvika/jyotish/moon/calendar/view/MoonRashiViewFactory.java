package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;
import org.satvika.jyotish.Rashi;
import org.satvika.jyotish.moon.calendar.pools.DatePool;

public class MoonRashiViewFactory extends ViewFactory<MoonRashiView> {

	public MoonRashiViewFactory(DatePool datePool, DateTime dateTime) {
		super(datePool, dateTime);
	}

	@Override
	public MoonRashiView createView(int value, DateTime dateTime) {
		return new MoonRashiView(Rashi.getByNumber(value), dateTime);
	}

}
