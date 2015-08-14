package org.satvika.jyotish.moon.calendar.view;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.satvika.jyotish.moon.calendar.pools.DatePool;
import org.satvika.jyotish.moon.calendar.pools.DatePoolEntry;

public abstract class ViewFactory<E> {

	private DatePool datePool;

	private DateTime dateTime;

	public ViewFactory(DatePool datePool, DateTime dateTime) {
		this.datePool = datePool;
		this.dateTime = dateTime;
	}

	public List<E> createViews() {

		List<E> result = new LinkedList<E>();

		DateTime dateTime = getDateTime();

		List<DatePoolEntry> entries = getDatePool().get(dateTime);
		if (entries.isEmpty()) {

			DatePoolEntry previousEntry = getDatePool().getPrevious(dateTime);
			if (previousEntry != null) {
				result.add(createView(previousEntry.getValue(), null));
			} else {
				throw new IllegalAccessError();
			}
		} else {
			for (DatePoolEntry entry : entries) {
				E tithiView = createView(entry.getValue(), entry.getDateTime());
				result.add(tithiView);
			}
		}

		return result;
	}

	public abstract E createView(int value, DateTime dateTime);

	public DatePool getDatePool() {
		return datePool;
	}

	public void setDatePool(DatePool datePool) {
		this.datePool = datePool;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
}
