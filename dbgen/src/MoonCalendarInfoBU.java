import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import swisseph.DblObj;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;
import swisseph.SwissLib;

public class MoonCalendarInfoBU {

	public static final int LAGNA = 0;
	public static final int SY = 1;
	public static final int CH = 2;
	public static final int MA = 3;
	public static final int BU = 4;
	public static final int GU = 5;
	public static final int SK = 6;
	public static final int SA = 7;
	public static final int RH = 8;
	public static final int KT = 9;

	private SweDate sweDate;
	private SwissEph sw;
	private double dres[] = new double[6];
	public double[] pos = new double[10];
	int[] rashiOfGraha = new int[10];
	boolean[] retr = new boolean[10];
	private StringBuffer serr = new StringBuffer();
	SGrahaPos spos = new SGrahaPos();
	private double cusp[] = new double[13];
	private double ac[] = new double[10];

	private String moonRashi = "";
	private String sunRashi = "";
	private String moonNakshatra = "";
	private int tithi = 0;

	static final int BIT_ROUND_SEC = 1;
	static final int BIT_ROUND_MIN = 2;
	static final int BIT_ZODIAC = 4;

	private static int pl[] = new int[] { -1, SweConst.SE_SUN,
			SweConst.SE_MOON, SweConst.SE_MARS, SweConst.SE_MERCURY,
			SweConst.SE_JUPITER, SweConst.SE_VENUS, SweConst.SE_SATURN,
			SweConst.SE_TRUE_NODE };

	private SweDate sunRiseDateTime;
	private SweDate sunSetDateTime;

	private SweDate moonRiseDateTime;
	private SweDate moonSetDateTime;
	private Calendar calendar;

	/**
	 * @param args
	 */
	public MoonCalendarInfoBU() {

		sw = new SwissEph(
				".:./ephe:/users/ephe2/:/users/ephe/:c\\:\\\\ephe:d\\:\\\\ephe:http\\://localhost/datafiles:http\\://www.th-mack.de/datafiles");

		SwissLib sl = new SwissLib();

		sw.setHttpBufSize(300);

		/*
		 * System.out.println("Hours of day: " + cal.get(Calendar.HOUR_OF_DAY));
		 * System.out.println("Minute of day: " + cal.get(Calendar.MINUTE) /0
		 * 60.); System.out.println("Seconds of day: " +
		 * cal.get(Calendar.SECOND) / 3600.);
		 */

		/*
		 * sweDate = new SweDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)
		 * + 1, cal.get(Calendar.DAY_OF_MONTH), 16.);
		 * 
		 * sweDate = new SweDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)
		 * + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) +
		 * cal.get(Calendar.MINUTE) / 60. + cal.get(Calendar.SECOND) / 3600.);
		 * 
		 * sweDate = getSweDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)
		 * + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
		 * cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		 * 
		 * int year = 2012; int month = 10; int day = 3; int hour = 10; int
		 * minutes = 00; int seconds = 00;
		 */

		/*
		 * hour = 6; minutes = 40;
		 * 
		 * hour = 2; minutes = 27;
		 * 
		 * day = 29; hour = 1; minutes = 57;
		 * 
		 * day = 30; hour = 3; minutes = 20;
		 */
		// hour = 3;
		// minutes = 54;
		// seconds = 45;

		/*
		 * sweDate = getSweDate(year, month, day, hour, minutes, seconds);
		 * 
		 * year = 2012; month = 10; day = 3; hour = 0; minutes = 00; seconds =
		 * 00;
		 */

		/*
		 * int flgs = SweConst.SEFLG_SWIEPH + SweConst.SEFLG_SIDEREAL +
		 * SweConst.SEFLG_SPEED;
		 * 
		 * sw.swe_set_topo(0., 0., 0);
		 * sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI, 0, 0); int ret; double
		 * jdET = date.getJulDay() + date.getDeltaT(); // retr[0] = false; for
		 * (int i = 1; i < 9; i++) { ret = sw.swe_calc(jdET, pl[i], flgs, dres,
		 * serr); }
		 */

		// boolean result = calc();

		// System.out.println("Result: " + result);

		// System.out.println("Lagna (pos): " + pos[LAGNA]);
		// System.out.println("Sun (pos): " + pos[SY]);
		// System.out.println("Moon (pos): " + pos[CH]);

		// System.out.println("Sun (spos): " + spos.pos[SY - 1]);
		// System.out.println("Moobn (spos): " + spos.pos[CH - 1]);

		// System.out.println("Moon k (mpos): " + (pos[CH] - (rashiOfGraha[CH] -
		// 1) * 30));

		// System.out.println("Lagna rashi: " + rashiOfGraha[LAGNA]);
		// System.out.println("Sun rashi: " + rashiOfGraha[SY]);
		// System.out.println("Moon rashi: " + rashiOfGraha[CH]);

		// System.out.println("Moon rashi: " + getRashiName(rashiOfGraha[CH]));
		// System.out.println("Sun rashi: " + getRashiName(rashiOfGraha[SY]));

		// System.out.println("Moon nakshatra: " + getNakshatraName(pos[CH]));

		// System.out.println("Tithi: " + calculateTithi(pos[CH], pos[SY]));
	}

	public String getMoonRashi() {
		return getRashiName(rashiOfGraha[CH]);
	}

	public String getSunRashi() {
		return getRashiName(rashiOfGraha[SY]);
	}

	public String getMoonNakshatra() {
		return getNakshatraName(pos[CH]);
	}

	public int getTithi() {

		double x = pos[CH] - pos[SY]; // sun-moon elongation
		if (x < 0) {
			x += 360;
		}
		x = x - Math.floor(x / 360) * 360; // normalize elongation
		// double elong = x;
		return (int) Math.floor(x / 12) + 1; // determine the current tithi
												// number
												// from
												// 0 to 29
	}

	public Date getSunRise() {
		return SweDate.getDate(sunRiseDateTime.getJulDay());
	}

	public Date getSunSet() {
		return SweDate.getDate(sunSetDateTime.getJulDay());
	}

	public Date getMoonRise() {
		return SweDate.getDate(moonRiseDateTime.getJulDay());
	}

	public Date getMoonSet() {
		return SweDate.getDate(moonSetDateTime.getJulDay());
	}

	private static SweDate getSweDate(int year, int month, int day, int hour,
			int minutes, int seconds) {

		return new SweDate(year, month, day, hour + minutes / 60. + seconds
				/ 3600.);
	}

	private static String getNakshatraName(double position) {

		HashMap<Double, String> nakshatrasMap = new HashMap<Double, String>();

		nakshatrasMap.put(13.20, "Ашвини");
		nakshatrasMap.put(26.40, "Бхарани");
		nakshatrasMap.put(40.0, "Криттика");
		nakshatrasMap.put(53.20, "Рохини");
		nakshatrasMap.put(66.40, "Мригшира");
		nakshatrasMap.put(80.0, "Аридра");
		nakshatrasMap.put(93.20, "Пунар Васу");
		nakshatrasMap.put(106.40, "Пушья");
		nakshatrasMap.put(120.0, "Ашлеша");
		nakshatrasMap.put(133.20, "Магха");
		nakshatrasMap.put(146.40, "Пурва-пхалгуни");
		nakshatrasMap.put(160.0, "Уттар-пхалгуни");
		nakshatrasMap.put(173.20, "Хаста");
		nakshatrasMap.put(186.40, "Читра");
		nakshatrasMap.put(200.0, "Свати");
		nakshatrasMap.put(213.20, "Вишакха");
		nakshatrasMap.put(226.40, "Анурадха");
		nakshatrasMap.put(240.0, "Джьештха");
		nakshatrasMap.put(253.20, "Мула");
		nakshatrasMap.put(266.40, "Пурва-асадха");
		nakshatrasMap.put(280.0, "Уттар-асадха");
		nakshatrasMap.put(293.20, "Шравана");
		nakshatrasMap.put(306.40, "Дханишта");
		nakshatrasMap.put(320.0, "Сатабхиша"); // 306.46
		nakshatrasMap.put(333.20, "Пурва-бхадрапад");
		nakshatrasMap.put(346.40, "Уттар-бхадрапад");
		nakshatrasMap.put(360.0, "Ревати");

		double nextPosition = position
				+ convertHourToDecimal(doubleToHour((13.20)));
		/*
		 * System.out.println("hourToDec: " +
		 * convertHourToDecimal(doubleToHour(13.20)));
		 */
		// System.out.println(convertHourToDecimal(doubleToHour(306.4)));
		// System.out.println("Next position: " + nextPosition);

		String result = new String();
		for (Double nakshatraPosition : nakshatrasMap.keySet()) {
			double nPosition = convertHourToDecimal(doubleToHour(nakshatraPosition));
			if (nakshatraPosition == 320.0) {
				// System.out.println("nPosition: " + nPosition);
			}
			if (position < nPosition && nextPosition > nPosition) {
				result = nakshatrasMap.get(nakshatraPosition);
				break;
			}
		}

		return result;
	}

	@SuppressWarnings("unused")
	private static double angleToDouble(double angle) {
		double cile = Math.floor(angle);
		double ost = angle - cile;
		return cile + ost / 60;
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

	/** answer=hour+minutes/60+seconds/3600 */
	public static double convertHourToDecimal(String degree) {
		if (!degree
				.matches("(-)?[0-9]{1,3}\"[0-6][0-9]\'[0-6][0-9](.[0-9]{1,5})?"))
			throw new IllegalArgumentException(degree);
		String[] strArray = degree.split("[\"']");
		return Double.parseDouble(strArray[0])
				+ Double.parseDouble(strArray[1]) / 60
				+ Double.parseDouble(strArray[2]) / 3600;
	}

	private String getRashiName(int index) {

		ArrayList<String> rashis = new ArrayList<String>();
		rashis.add("Овен");
		rashis.add("Телец");
		rashis.add("Близнецы");
		rashis.add("Рак");
		rashis.add("Лев");
		rashis.add("Дева");
		rashis.add("Весы");
		rashis.add("Скорпион");
		rashis.add("Стрелец");
		rashis.add("Козерог");
		rashis.add("Водолей");
		rashis.add("Рыбы");

		return rashis.get(index - 1);
	}

	public void calc() {

		int year = getCalendar().get(Calendar.YEAR);
		int month = getCalendar().get(Calendar.MONTH) + 1;
		int day = getCalendar().get(Calendar.DAY_OF_MONTH);
		int hour = getCalendar().get(Calendar.HOUR_OF_DAY);
		int minutes = getCalendar().get(Calendar.MINUTE);
		int seconds = getCalendar().get(Calendar.SECOND);

		sweDate = getSweDate(year, month, day, hour, minutes, seconds);

		int flgs = SweConst.SEFLG_SWIEPH + SweConst.SEFLG_SIDEREAL
				+ SweConst.SEFLG_SPEED;

		// flgs += SweConst.SEFLG_TOPOCTR;

		// sw.swe_set_topo(49.30, 24.00, 0);

		// SweDate dddd = getSweDate(285, 3, 22, 0, 0, 0);

		sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI, 0, 0); // dddd.getJulDay()
		// sw.swe_rise_trans(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
		// arg8, arg9);

		// System.out.println("AYANAMSA: " + swe_get_ayanamsa());

		int ret;
		double jdET = sweDate.getJulDay() + sweDate.getDeltaT();
		retr[0] = false;
		for (int i = 1; i < 9; i++) {
			ret = sw.swe_calc(jdET, pl[i], flgs, dres, serr);
			if (ret < 0) {
				System.err.println(serr.toString());
				return;
			}
			pos[i] = dres[0];
			spos.pos[i - 1] = dres[0];
			spos.graha[i - 1] = i - 1;
			rashiOfGraha[i] = (int) (dres[0] / 30.0) + 1;
			retr[i] = dres[3] < 0.0;
		}
		pos[KT] = (pos[RH] + 180.0) % 360.;
		spos.pos[KT - 1] = pos[KT];
		spos.graha[KT - 1] = KT - 1;
		rashiOfGraha[KT] = (int) (pos[KT] / 30.0) + 1;
		retr[KT] = retr[RH];

		spos.sort();

		sw.swe_houses(sweDate.getJulDay(), SweConst.SEFLG_SIDEREAL, 0., 0.,
				(int) 'A', cusp, ac);

		pos[LAGNA] = ac[0];
		rashiOfGraha[LAGNA] = (int) (ac[0] / 30.0) + 1;
	}

	public ArrayList<Calendar> searchNakshatraChanges() {

		ArrayList<Calendar> result = new ArrayList<Calendar>();

		Calendar originCalendar = getCalendar();
		Calendar modifiedCalendar = (Calendar) originCalendar.clone();

		String originNakshatra = getMoonNakshatra();

		modifiedCalendar.set(Calendar.SECOND, 0);
		modifiedCalendar.set(Calendar.MINUTE, 0);

		for (int h = 0, m = 0; h < 23; h++) {
			/*
			 * if (m == 60) { m = 0; h++; if (h == 24) { break; } }
			 */

			modifiedCalendar.set(Calendar.HOUR, h);

			setCalendar(modifiedCalendar);

			calc();

			String nakshatra = getMoonNakshatra();

			if (originNakshatra != nakshatra) {
				Calendar changedNakshatra = (Calendar) modifiedCalendar.clone();
				result.add(changedNakshatra);

				originNakshatra = nakshatra;
			}
		}

		setCalendar(originCalendar);

		return result;
	}

	public void calculateRisesAndSets() {

		sunRiseDateTime = calculateRiseAndSunset(SweConst.SE_SUN,
				SweConst.SE_CALC_RISE);
		sunSetDateTime = calculateRiseAndSunset(SweConst.SE_SUN,
				SweConst.SE_CALC_SET);

		moonRiseDateTime = calculateRiseAndSunset(SweConst.SE_MOON,
				SweConst.SE_CALC_RISE);
		moonSetDateTime = calculateRiseAndSunset(SweConst.SE_MOON,
				SweConst.SE_CALC_SET);
	}

	private SweDate calculateRiseAndSunset(int planet, int flag) {

		DblObj tret = new DblObj();
		StringBuffer serr = new StringBuffer();

		SwissEph se = new SwissEph();

		double geopos[] = new double[3];

		geopos[0] = 24.0; // getLocation().getLatitude(); // 24.0; // 37.0; //
		geopos[1] = 49.50; // getLocation().getLongitude(); // 49.50; // 55.0;
							// //

		// System.out.println(getLocation().getLatitude());
		// System.out.println(getLocation().getLongitude());

		geopos[2] = 200.0;

		// se.swe_set_topo(geopos[0], geopos[1], geopos[2]);

		// sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI, 0, 0);

		// int flags = SweConst.SEFLG_SIDEREAL;

		int flags = flag; // SweConst.SE_ECL_NUT;

		flags += SweConst.SE_BIT_NO_REFRACTION;
		flags += SweConst.SE_BIT_DISC_CENTER;

		tret.val = 0;

		// jdET = date_for_sun_rise.getJulDay(); //
		// date_for_sun_rise.getDeltaT();

		int year = getCalendar().get(Calendar.YEAR);
		int month = getCalendar().get(Calendar.MONTH);
		int day = getCalendar().get(Calendar.DAY_OF_MONTH);

		double dddd = SweDate.getJulDay(year, month, day, 0);

		se.swe_rise_trans(dddd, planet, null, SweConst.SEFLG_SWIEPH, flags,
				geopos, 2013.25, 20, tret, serr);

		// sw.swe_rise_tran

		SweDate date = new SweDate();
		// sunRiseDateTime.set
		date.setJulDay(tret.val);
		date.setCalendarType(SweDate.SE_GREG_CAL, SweDate.SE_KEEP_JD);

		return date;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
}

class SGrahaPos {
	double pos[] = new double[9];
	int graha[] = new int[9];

	SGrahaPos() {
		// #ifdef TRACE0
		// Trace.taCTrace(null, "SGrahaPos()");
		// #endif // TRACE0
		for (int i = 0; i < graha.length; i++) {
			graha[i] = i;
		}
	}

	public void sort() {
		// #ifdef TRACE0
		// Trace.taCTrace(null, "SGrahaPos.sort()");
		// #endif // TRACE0
		for (int i = graha.length; --i >= 0;) {
			boolean swapped = false;
			for (int j = 0; j < i; j++) {
				if (pos[j] > pos[j + 1]) {
					double d = pos[j];
					int g = graha[j];
					pos[j] = pos[j + 1];
					graha[j] = graha[j + 1];
					pos[j + 1] = d;
					graha[j + 1] = g;
					swapped = true;
				}
			}
			if (!swapped)
				return;
		}
	}
} // End of class SGrahaPos