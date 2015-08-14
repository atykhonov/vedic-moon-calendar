package org.satvika.jyotish.moon.calendar.pools;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DatePoolEntry {

	private int value;

	private DateTime dateTime;

	public DatePoolEntry(int value, long time, DateTimeZone dateTimeZone) {
		setValue(value);
		setDateTime(new DateTime(time, dateTimeZone));
	}

	public int getValue() {
		return value;
	}

	private void setValue(int value) {
		this.value = value;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	private void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
}
