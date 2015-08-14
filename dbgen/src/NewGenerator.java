import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.satvika.jyotish.Nakshatra;
import org.satvika.jyotish.Rashi;
import org.satvika.jyotish.common.Translator;
import org.satvika.jyotish.moon.calendar.view.CalendarDayView;
import org.satvika.jyotish.moon.calendar.view.CalendarView;
import org.satvika.jyotish.moon.calendar.view.MoonRashiView;
import org.satvika.jyotish.moon.calendar.view.NakshatraView;
import org.satvika.jyotish.moon.calendar.view.SunRashiView;
import org.satvika.jyotish.moon.calendar.view.TithiView;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class NewGenerator {

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"dd-MMM-yyyy HH:mm:ss");

	private static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

	private SQLiteConnection connection;
	
	private String filename;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		NewGenerator generator = new NewGenerator();
		generator.generate(args[0]);
	}

	private void generate(String filepath) {

		StringBuffer html = new StringBuffer();

		connection = new SQLiteConnection(
				new File(filepath));
		try {
			connection.openReadonly();

			int givenMonth = 0;
			int givenYear = 2012;

			while (true) {

				Calendar calendar = Calendar.getInstance();

				calendar.set(Calendar.MONTH, givenMonth);
				calendar.set(Calendar.YEAR, givenYear);

				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.SECOND, 0);

				calendar.set(Calendar.DAY_OF_MONTH, 1);

				CalendarView calendarView = new CalendarView(givenMonth,
						givenYear);

				int dayWeeksOffset = 0;
				while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					dayWeeksOffset++;
				}

				int daysInMonth = calendar.getMaximum(Calendar.DAY_OF_MONTH);

				int maxIndex = 36;
				if (daysInMonth + dayWeeksOffset > 36) {
					maxIndex = 43;
				}

				for (int index = 1; index < maxIndex; index++) {

					CalendarDayView dayView = new CalendarDayView(
							calendar.get(Calendar.DAY_OF_MONTH));

					calendarView.addCalendarDayView(dayView);

					if (calendar.get(Calendar.MONTH) == givenMonth) {

						int year = calendar.get(Calendar.YEAR);
						int month = calendar.get(Calendar.MONTH);
						int day = calendar.get(Calendar.DAY_OF_MONTH);

						Calendar from = Calendar.getInstance();
						from.set(year, month, day, -3, 0, 0);
						Calendar to = Calendar.getInstance();
						to.set(year, month, day + 1, -3, 0, 0);

						List<TithiView> tithiViews = createTithiViews(from, to);
						for (TithiView view : tithiViews) {
							dayView.addTithiView(view);
						}

						List<MoonRashiView> moonRashiViews = createMoonRashiViews(
								from, to);
						for (MoonRashiView view : moonRashiViews) {
							dayView.addMoonRashiView(view);
						}

						List<SunRashiView> sunRashiViews = createSunRashiViews(
								from, to);
						for (SunRashiView view : sunRashiViews) {
							dayView.addSunRashiView(view);
						}

						List<NakshatraView> nakshatraViews = createNakshatraViews(
								from, to);
						for (NakshatraView view : nakshatraViews) {
							dayView.addNakshatraView(view);
						}
					} else {
						dayView.setEmpty(true);
					}

					calendar.add(Calendar.DAY_OF_MONTH, 1);

					/*
					 * LinkedHashMap<Date, Object[]> moonTithiValues =
					 * getValuesMap( "moon_tithi_changes", year, month, day);
					 * outputValuesMap("Tithi: ", moonTithiValues, 3, html);
					 * 
					 * LinkedHashMap<Date, Object[]> moonRashiValues =
					 * getValuesMap( "moon_rashi_changes", year, month, day);
					 * outputValuesMap("Moon rashi: ", moonRashiValues, 1,
					 * html);
					 * 
					 * LinkedHashMap<Date, Object[]> sunRashiValues =
					 * getValuesMap( "sun_rashi_changes", year, month, day);
					 * outputValuesMap("Sun rashi: ", sunRashiValues, 1, html);
					 * 
					 * LinkedHashMap<Date, Object[]> moonNakshatraValues =
					 * getValuesMap( "moon_nakshatra_changes", year, month,
					 * day); outputValuesMap("Nakshatra: ", moonNakshatraValues,
					 * 2, html);
					 */

					// html.append("</td>");
				}

				BufferedWriter bw = new BufferedWriter(new FileWriter(
						"/../../../output/"
								+ calendarView.getFilename()));

				bw.write(calendarView.render());

				bw.close();

				givenMonth++;
				if (givenMonth > 11) {
					givenMonth = 0;
					givenYear++;
				}

				if (givenMonth == 0 && givenYear == 2016) {
					break;
				}
			}

		} catch (SQLiteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connection.dispose();
	}

	public void outputValuesMap(String prefix,
			LinkedHashMap<Date, Object[]> values, int type, StringBuffer sb) {
		for (Date date : values.keySet()) {
			Object[] objects = values.get(date);
			Integer value = (Integer) objects[0];
			String str = "";
			if (type == 1) {
				str = Rashi.getByNumber(value).toString();
			} else if (type == 2) {
				str = Nakshatra.getByNumber(value).toString();
			} else if (type == 3) {
				str = value.toString();
			}
			boolean fixed = (Boolean) objects[1];

			if (fixed) {
				sb.append(prefix + str + " (" + sdf.format(date) + ")"
						+ "<br />");
			} else {
				sb.append(prefix + str + " (" + sdf2.format(date) + ")"
						+ "<br />");
			}
		}
	}

	public void createViews(String prefix,
			LinkedHashMap<Date, Object[]> values, int type, StringBuffer sb) {

		for (Date date : values.keySet()) {
			Object[] objects = values.get(date);
			Integer value = (Integer) objects[0];
			String str = "";
			if (type == 1) {
				str = Rashi.getByNumber(value).toString();
			} else if (type == 2) {
				str = Nakshatra.getByNumber(value).toString();
			} else if (type == 3) {
				str = value.toString();
			}
			boolean fixed = (Boolean) objects[1];

			if (fixed) {
				sb.append(prefix + str + " (" + sdf.format(date) + ")"
						+ "<br />");
			} else {
				sb.append(prefix + str + " (" + sdf2.format(date) + ")"
						+ "<br />");
			}
		}
	}

	/*
	 * public static void outputValuesMap(String prefix, LinkedHashMap<Date,
	 * Integer> values, int type) { for (Date date : values.keySet()) { Integer
	 * value = values.get(date); String str = ""; if (type == 1) { str =
	 * Rashi.getByNumber(value).toString(); } else if (type == 2) { str =
	 * Nakshatra.getByNumber(value).toString(); } else if (type == 3) { str =
	 * value.toString(); } System.out.println(prefix + str + " (" +
	 * sdf.format(date) + ")"); } }
	 */

	private List<TithiView> createTithiViews(Calendar from, Calendar to)
			throws SQLiteException {

		List<TithiView> views = new LinkedList<TithiView>();

		LinkedHashMap<Long, Integer> data = getData("moon_tithi_changes", from,
				to);

		for (Long time : data.keySet()) {
			Integer value = data.get(time);
			TithiView tithiView = new TithiView(value, time);
			views.add(tithiView);
		}

		return views;
	}

	private List<MoonRashiView> createMoonRashiViews(Calendar from, Calendar to)
			throws SQLiteException {

		List<MoonRashiView> views = new LinkedList<MoonRashiView>();

		LinkedHashMap<Long, Integer> data = getData("moon_rashi_changes", from,
				to);

		for (Long time : data.keySet()) {
			Integer value = data.get(time);
			MoonRashiView moonRashiView = new MoonRashiView(
					Rashi.getByNumber(value), time);

			views.add(moonRashiView);
		}

		return views;
	}

	private List<SunRashiView> createSunRashiViews(Calendar from, Calendar to)
			throws SQLiteException {

		List<SunRashiView> views = new LinkedList<SunRashiView>();

		LinkedHashMap<Long, Integer> data = getData("sun_rashi_changes", from,
				to);

		for (Long time : data.keySet()) {
			Integer value = data.get(time);
			SunRashiView sunRashiView = new SunRashiView(
					Rashi.getByNumber(value), time);

			views.add(sunRashiView);
		}

		return views;
	}

	private List<NakshatraView> createNakshatraViews(Calendar from, Calendar to)
			throws SQLiteException {

		List<NakshatraView> views = new LinkedList<NakshatraView>();

		LinkedHashMap<Long, Integer> data = getData("moon_nakshatra_changes",
				from, to);

		for (Long time : data.keySet()) {
			Integer value = data.get(time);
			NakshatraView nakshatraView = new NakshatraView(
					Nakshatra.getByNumber(value), time);

			views.add(nakshatraView);
		}

		return views;
	}

	private LinkedHashMap<Long, Integer> getData(String table, Calendar from,
			Calendar to) throws SQLiteException {

		LinkedHashMap<Long, Integer> map = new LinkedHashMap<Long, Integer>();

		SQLiteStatement st = connection.prepare(createSQLStatementForTable(
				table, from, to));

		while (st.step()) {
			map.put(st.columnLong(1), st.columnInt(2));
		}

		st.dispose();

		return map;
	}

	private String createSQLStatementForTable(String table, Calendar from,
			Calendar to) {

		return "SELECT * FROM " + table + " WHERE time >= "
				+ from.getTimeInMillis() + " AND time < "
				+ to.getTimeInMillis();
	}

	public LinkedHashMap<Date, Object[]> getValuesMap(String table, int year,
			int month, int day) throws SQLiteException {

		LinkedHashMap<Date, Object[]> values = new LinkedHashMap<Date, Object[]>();

		Calendar from = Calendar.getInstance();
		from.set(year, month, day, -3, 0, 0);
		// System.out.println("1: " + from.getTime());
		// from = convertToGmt(from);
		// System.out.println("2: " + from.getTime());
		Calendar to = Calendar.getInstance();
		to.set(year, month, day + 1, -3, 0, 0);
		// System.out.println("2: " + to.getTime());
		// to = convertToGmt(to);
		// System.out.println("4: " + to.getTime());

		SQLiteStatement st = connection.prepare("SELECT * FROM " + table
				+ " WHERE time >= " + from.getTimeInMillis() + " AND time < "
				+ to.getTimeInMillis());

		while (st.step()) {

			Calendar calendar = Calendar.getInstance();

			long moonRashiTime = st.columnLong(1);
			calendar.setTimeInMillis(moonRashiTime);
			calendar = convertToGmt(calendar);

			boolean fixed = false;
			if (st.columnInt(3) == 1) {
				fixed = true;
			}

			values.put(calendar.getTime(), new Object[] { st.columnInt(2),
					fixed });
		}

		st.dispose();

		return values;
	}

	public Calendar convertToGmt(Calendar cal) {

		Date date = cal.getTime();
		TimeZone tz = cal.getTimeZone();

		// log.debug("input calendar has date [" + date + "]");

		// Returns the number of milliseconds since January 1, 1970, 00:00:00
		// GMT
		long msFromEpochGmt = date.getTime();

		// gives you the current offset in ms from GMT at the current date
		int offsetFromUTC = tz.getOffset(msFromEpochGmt);
		// log.debug("offset is " + offsetFromUTC);

		// create a new calendar in GMT timezone, set to this date and add the
		// offset
		Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		gmtCal.setTime(date);
		gmtCal.add(Calendar.MILLISECOND, offsetFromUTC);

		// log.debug("Created GMT cal with date [" + gmtCal.getTime() + "]");

		return gmtCal;
	}
}
