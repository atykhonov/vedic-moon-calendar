package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;
import org.satvika.jyotish.Nakshatra;
import org.satvika.jyotish.moon.calendar.pools.DatePool;

public class NakshatraViewFactory extends ViewFactory<NakshatraView> {

	public NakshatraViewFactory(DatePool datePool, DateTime dateTime) {
		super(datePool, dateTime);
	}

	@Override
	public NakshatraView createView(int value, DateTime dateTime) {
		return new NakshatraView(Nakshatra.getByNumber(value), dateTime);
	}
}