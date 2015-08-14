package org.satvika.jyotish;

import java.util.ArrayList;
import java.util.List;

public class Rashi {

	private static ArrayList<Rashi> list = new ArrayList<Rashi>();

	public static final Rashi ARIES = new Rashi("Aries");

	public static final Rashi TAURUS = new Rashi("Taurus");

	public static final Rashi GEMINI = new Rashi("Gemini");

	public static final Rashi CANCER = new Rashi("Cancer");

	public static final Rashi LEO = new Rashi("Leo");

	public static final Rashi VIRGO = new Rashi("Virgo");

	public static final Rashi LIBRA = new Rashi("Libra");

	public static final Rashi SCORPIO = new Rashi("Scorpio");

	public static final Rashi SAGITTARIUS = new Rashi("Sagittarius");

	public static final Rashi CAPRICORN = new Rashi("Capricorn");

	public static final Rashi AQUARIUS = new Rashi("Aquarius");

	public static final Rashi PISCES = new Rashi("Pisces");

	private String name;

	public Rashi(String name) {
		this.name = name;

		list.add(this);
	}

	public static List<Rashi> getList() {
		return list;
	}

	public static Rashi getByNumber(int number) {
		return getList().get(number - 1);
	}

	@Override
	public String toString() {
		return this.name;
	}

	public int getNumber() {
		return getList().indexOf(this) + 1;
	}
}