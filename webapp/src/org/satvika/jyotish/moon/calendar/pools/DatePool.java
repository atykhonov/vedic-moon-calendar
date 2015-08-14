package org.satvika.jyotish.moon.calendar.pools;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

public class DatePool {

	private LinkedList<DatePoolEntry> entries = new LinkedList<DatePoolEntry>();

	private DatePoolEntry currentEntry;

	public void add(DatePoolEntry entry) {
		entries.add(entry);
	}

	public List<DatePoolEntry> get(DateTime dateTime) {

		List<DatePoolEntry> result = new LinkedList<DatePoolEntry>();

		for (DatePoolEntry entry : entries) {

			DateTime dt = entry.getDateTime();

			if (dt.getYear() == dateTime.getYear()
					&& dt.getMonthOfYear() == dateTime.getMonthOfYear()
					&& dt.getDayOfMonth() == dateTime.getDayOfMonth()) {

				result.add(entry);

				currentEntry = entry;
			}
		}

		return result;
	}

	public DatePoolEntry getPrevious(DateTime dateTime) {
		DatePoolEntry result = null;
		DatePoolEntry previousEntry = null;
		for (DatePoolEntry entry : entries) {
			if (dateTime.isBefore(entry.getDateTime().getMillis())) {
				result = previousEntry;
				break;
			}
			previousEntry = entry;
		}
		if (result == null) {
			result = entries.getLast();
		}
		return result;
	}

	public DatePoolEntry getNext(DateTime dateTime) {
		DatePoolEntry result = null;
		for (DatePoolEntry entry : entries) {
			if (dateTime.isBefore(entry.getDateTime().getMillis())) {
				result = entry;
				break;
			}
		}
		return result;
	}
}
