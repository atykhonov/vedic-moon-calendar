package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;

public class TithiView extends SimpleEntityView {

	private int tithi;

	public TithiView(int tithi, DateTime dateTime) {
		super(dateTime);
		this.tithi = tithi;
	}

	public int getTithiNum() {
		return this.tithi;
	}

	public String getTithi() {
		return (new Integer(tithi)).toString();
	}

	@Override
	public String render() {
		return render((new Integer(tithi)).toString() + " л. с.");
	}
}
