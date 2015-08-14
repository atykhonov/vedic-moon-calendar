import java.io.File;
import java.util.Date;

import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.satvika.jyotish.Graha;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;


public class GeneralMonitor {

	private SQLiteConnection db;
	
	private int startYear = 2012;
	
	private int endYear = 2023;

	public GeneralMonitor(String filename) throws SQLiteException {

		db = new SQLiteConnection(
				new File(filename));

		db.open(false);
	}

	public static void main(String[] args) throws Exception {

		GeneralMonitor generalMonitor = new GeneralMonitor(args[0]);
		generalMonitor.eclipsesMonitor();
		generalMonitor.generalMonitor();
		// generalMonitor.testMonitor();
		// generalMonitor.testView();
	}

	public void testMonitor() throws SQLiteException {

		MutableDateTime dateTime = new MutableDateTime(this.startYear, 1, 1, 0, 0, 0, 0);
		dateTime.setZoneRetainFields(DateTimeZone.UTC);

		MoonCalendarInfo info = new MoonCalendarInfo();

		MoonRashiMonitor moonRashiMonitor = new MoonRashiMonitor(db);
		TithiMonitor tithiMonitor = new TithiMonitor(db);
		NakshatraMonitor nakshatraMonitor = new NakshatraMonitor(db);

		while (true) {

			// info.setCalendar(calendar);
			info.setDateTime(dateTime.toDateTime());
			info.calc();

			moonRashiMonitor.setRashi(info.getMoonRashi());
			moonRashiMonitor.setTime(dateTime.getMillis());

			tithiMonitor.setTithi(info.getTithi());
			tithiMonitor.setTime(dateTime.getMillis());

			nakshatraMonitor.setNakshatra(info.getMoonNakshatra());
			nakshatraMonitor.setTime(dateTime.getMillis());

			dateTime.addMinutes(1);

			System.out.println(dateTime.toString());

			if (dateTime.getYear() == this.endYear && dateTime.getMonthOfYear() == 1
					&& dateTime.getDayOfMonth() == 1) {
				break;
			}
		}

		db.dispose();
	}

	public void eclipsesMonitor() throws SQLiteException {

		MoonCalendarInfo info = new MoonCalendarInfo();

		MutableDateTime dateTime = new MutableDateTime();

		EclipseMonitor eclipseMonitor = new EclipseMonitor(db);

		for (int num = 1; num < 3; num = num + 1) {

			dateTime.setYear(this.startYear);
			dateTime.setMonthOfYear(1);
			dateTime.setDayOfMonth(1);
			dateTime.setHourOfDay(0);
			dateTime.setMinuteOfHour(0);
			dateTime.setSecondOfMinute(0);

			while (true) {

				info.setDateTime(dateTime.toDateTime());

				Date date = null;
				if (num == 1) {
					date = info.calcSunEclipse();
				} else if (num == 2) {
					date = info.calcMoonEclipse();
				}

				dateTime.setMillis(date.getTime());

				if (dateTime.getYear() == this.endYear) {
					break;
				}

				eclipseMonitor.setGraha(Graha.getByNumber(num));
				eclipseMonitor.setTime(dateTime.getMillis());

				dateTime.addDays(1);
			}
		}
	}

	public void generalMonitor() throws Exception {

		SQLiteStatement st = null;
		try {

			MutableDateTime dateTime = new MutableDateTime(this.startYear, 1, 1, 0, 0,
					0, 0);

			dateTime.setZoneRetainFields(DateTimeZone.UTC);

			MoonCalendarInfo info = new MoonCalendarInfo();

			MoonRashiMonitor moonRashiMonitor = new MoonRashiMonitor(db);
			SunRashiMonitor sunRashiMonitor = new SunRashiMonitor(db);
			TithiMonitor tithiMonitor = new TithiMonitor(db);
			NakshatraMonitor nakshatraMonitor = new NakshatraMonitor(db);

			while (true) {

				// info.setCalendar(calendar);
				info.setDateTime(dateTime.toDateTime());
				info.calc();

				moonRashiMonitor.setRashi(info.getMoonRashi());
				moonRashiMonitor.setTime(dateTime.getMillis());

				sunRashiMonitor.setRashi(info.getSunRashi());
				sunRashiMonitor.setTime(dateTime.getMillis());

				tithiMonitor.setTithi(info.getTithi());
				tithiMonitor.setTime(dateTime.getMillis());

				nakshatraMonitor.setNakshatra(info.getMoonNakshatra());
				nakshatraMonitor.setTime(dateTime.getMillis());

				dateTime.addMinutes(1);

				System.out.println(dateTime.toString());

				if (dateTime.getYear() == this.endYear
						&& dateTime.getMonthOfYear() == 1
						&& dateTime.getDayOfMonth() == 1) {
					break;
				}
			}

		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			st.dispose();
		}
		db.dispose();
	}
}