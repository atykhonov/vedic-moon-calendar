package org.satvika.jyotish.moon.calendar.pools;

import java.util.LinkedList;
import java.util.List;

public class TithiDatePool {

	private List<TithiDatePoolEntry> entries = new LinkedList<TithiDatePoolEntry>();

	public void add(TithiDatePoolEntry entry) {
		entries.add(entry);
	}
}