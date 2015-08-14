package org.satvika.jyotish.moon.calendar.pools;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TithiDatePoolEntry {

	private int tithi;

	private DateTime dateTime;

	public TithiDatePoolEntry(int tithi, long time, DateTimeZone dateTimeZone) {
		setTithi(tithi);

		setDateTime(new DateTime(time, dateTimeZone));
	}

	public int getTithi() {
		return tithi;
	}

	private void setTithi(int tithi) {
		this.tithi = tithi;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	private void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
}