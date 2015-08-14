package org.satvika.jyotish;

import java.util.ArrayList;
import java.util.List;

public class Graha {

	private static ArrayList<Graha> list = new ArrayList<Graha>();

	public static final Graha SUN = new Graha("Sun");

	public static final Graha MOON = new Graha("Moon");

	private String name;

	private Graha(String name) {
		this.name = name;

		list.add(this);
	}

	public String getName() {
		return this.name;
	}

	public static ArrayList<Graha> getList() {
		return list;
	}

	public String toString() {
		return this.name;
	}

	public static Graha getByNumber(int number) {
		return getList().get(number - 1);
	}

	public int getNumber() {
		return list.indexOf(this);
	}
}
