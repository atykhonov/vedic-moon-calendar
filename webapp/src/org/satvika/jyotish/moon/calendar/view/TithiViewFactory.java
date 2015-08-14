package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;
import org.satvika.jyotish.moon.calendar.pools.DatePool;

public class TithiViewFactory extends ViewFactory<TithiView> {

	public TithiViewFactory(DatePool datePool, DateTime dateTime) {
		super(datePool, dateTime);
	}

	@Override
	public TithiView createView(int value, DateTime dateTime) {
		return new TithiView(value, dateTime);
	}
}
