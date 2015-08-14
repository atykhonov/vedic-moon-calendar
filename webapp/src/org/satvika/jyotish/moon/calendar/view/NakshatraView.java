package org.satvika.jyotish.moon.calendar.view;

import org.joda.time.DateTime;
import org.satvika.jyotish.Nakshatra;

public class NakshatraView extends SimpleEntityView {

	private Nakshatra nakshatra;

	public NakshatraView(Nakshatra nakshatra, DateTime dateTime) {
		super(dateTime);
		setNakshatra(nakshatra);
	}

	@Override
	public String render() {
		return render(getDisplayName());
	}

	public String getDisplayName() {
		return getTranslator().translate(getNakshatra().toString());
	}

	public void setNakshatra(Nakshatra nakshatra) {
		this.nakshatra = nakshatra;
	}

	public Nakshatra getNakshatra() {
		return this.nakshatra;
	}
}