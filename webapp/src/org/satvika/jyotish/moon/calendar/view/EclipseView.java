package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;
import org.satvika.jyotish.Graha;

public class EclipseView extends SimpleEntityView {

	private Graha graha;

	public EclipseView(DateTime dateTime) {
		super(dateTime);
	}

	public EclipseView(DateTime dateTime, Graha graha) {
		this(dateTime);
		setGraha(graha);
	}

	public void setGraha(Graha graha) {
		this.graha = graha;
	}

	public Graha getGraha() {
		return this.graha;
	}

	@Override
	public String render() {
		return "";
	}

}
