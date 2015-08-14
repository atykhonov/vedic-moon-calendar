package org.satvika.jyotish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Nakshatra {

	private static ArrayList<Nakshatra> list = new ArrayList<Nakshatra>();

	private static HashMap<Double, Nakshatra> degreeNakshatrasMap = new HashMap<Double, Nakshatra>();

	public static final Nakshatra ASHWINI = new Nakshatra("Ashwini", 13.20);

	public static final Nakshatra BHARANI = new Nakshatra("Bharani", 26.40);

	public static final Nakshatra KRITTIKA = new Nakshatra("Krittika", 40.0);

	public static final Nakshatra ROHINI = new Nakshatra("Rohini", 53.20);

	public static final Nakshatra MRIGASHIRA = new Nakshatra("Mrigashira",
			66.40);

	public static final Nakshatra ARDRA = new Nakshatra("Ardra", 80.0);

	public static final Nakshatra PUNARVASU = new Nakshatra("Punarvasu", 93.20);

	public static final Nakshatra PUSHYA = new Nakshatra("Pushya", 106.40);

	public static final Nakshatra ASHLESHA = new Nakshatra("Ashlesha", 120.0);

	public static final Nakshatra MAGHA = new Nakshatra("Magha", 133.20);

	public static final Nakshatra PURVAPHALGUNI = new Nakshatra(
			"Purvaphalguni", 146.40);

	public static final Nakshatra UTTARAPHALGUNI = new Nakshatra(
			"Uttaraphalguni", 160.0);

	public static final Nakshatra HASTA = new Nakshatra("Hasta", 173.20);

	public static final Nakshatra CHITRA = new Nakshatra("Chitra", 186.40);

	public static final Nakshatra SWATI = new Nakshatra("Swati", 200.0);

	public static final Nakshatra VISAKHA = new Nakshatra("Visakha", 213.20);

	public static final Nakshatra ANURADHA = new Nakshatra("Anuradha", 226.40);

	public static final Nakshatra JYESTHA = new Nakshatra("Jyestha", 240.0);

	public static final Nakshatra MULA = new Nakshatra("Mula", 253.20);

	public static final Nakshatra PURVASHADHA = new Nakshatra("Purvashadha",
			266.40);

	public static final Nakshatra UTTARASHADHA = new Nakshatra("Uttarashadha",
			280.0);

	public static final Nakshatra SHRAVAN = new Nakshatra("Shravan", 293.20);

	public static final Nakshatra DHANISTHA = new Nakshatra("Dhanistha", 306.40);

	public static final Nakshatra SATABISHA = new Nakshatra("Satabisha", 320.0);

	public static final Nakshatra PURVABHADRAPADA = new Nakshatra(
			"Purvabhadrapada", 333.20);

	public static final Nakshatra UTTARABHADRAPAD = new Nakshatra(
			"Uttarabhadrapad", 346.40);

	public static final Nakshatra REVATI = new Nakshatra("Revati", 360.0);

	private String name = "";

	private double degree;

	private Nakshatra(String name, double degree) {
		this.name = name;
		this.degree = degree;

		list.add(this);
		degreeNakshatrasMap.put(degree, this);
	}

	public static List<Nakshatra> getList() {
		return list;
	}

	public static Nakshatra getByNumber(int number) {
		return list.get(number - 1);
	}

	public static Nakshatra getByPosition(double position) {

		double nextPosition = position
				+ convertHourToDecimal(doubleToHour((13.20)));

		Nakshatra result = null;
		for (Double nakshatraPosition : degreeNakshatrasMap.keySet()) {
			double nPosition = convertHourToDecimal(doubleToHour(nakshatraPosition));
			if (position < nPosition && nextPosition > nPosition) {
				result = degreeNakshatrasMap.get(nakshatraPosition);
				break;
			}
		}

		return result;
	}

	/** answer=hour+minutes/60+seconds/3600 */
	private static double convertHourToDecimal(String degree) {
		if (!degree
				.matches("(-)?[0-9]{1,3}\"[0-6][0-9]\'[0-6][0-9](.[0-9]{1,5})?"))
			throw new IllegalArgumentException(degree);
		String[] strArray = degree.split("[\"']");
		return Double.parseDouble(strArray[0])
				+ Double.parseDouble(strArray[1]) / 60
				+ Double.parseDouble(strArray[2]) / 3600;
	}

	private static String doubleToHour(double angle) {
		double cile = Math.floor(angle);
		double ost = Math.rint(angle * 100 - cile * 100);
		String ostacha = "";
		if (ost == 0.0) {
			ostacha = "00";
		} else {
			ostacha = "" + (int) ost;
		}
		return (int) cile + "\"" + ostacha + "'00";
	}

	@Override
	public String toString() {
		return this.name;
	}

	public int getNumber() {
		return getList().indexOf(this) + 1;
	}
}