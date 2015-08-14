import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.satvika.jyotish.Nakshatra;
import org.satvika.jyotish.Rashi;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class Main {

	private static int startYear = 2011;
	
	private static int endYear = 2023;
	
	public static void main(String[] args) throws Exception {
		SQLiteConnection db = new SQLiteConnection(
				new File(args[0]));

		SQLiteStatement st = null;
		try {
			db.open(false);

			for (Rashi rasi : Rashi.getList()) {
				st = db.prepare("INSERT INTO rasi VALUES(" + rasi.getNumber()
						+ ", '" + rasi.toString() + "')");
				st.step();
			}

			for (Nakshatra nakshatra : Nakshatra.getList()) {
				st = db.prepare("INSERT INTO nakshatra VALUES("
						+ nakshatra.getNumber() + ", '" + nakshatra.toString()
						+ "')");
				st.step();
			}

			Calendar calendar = Calendar.getInstance();
			calendar.set(startYear, 11, 30, 0, 0, 0);

			MoonCalendarInfo info = new MoonCalendarInfo();

			int lastDay = calendar.get(Calendar.DAY_OF_MONTH);
			int lastTithi = 0;
			long lastTithiTime = 0;
			Nakshatra lastNakshatra = null;
			long lastNakshatraTime = 0;
			Rashi lastMoonRashi = null;
			long lastMoonRashiTime = 0;
			LinkedList<Change> tithiChanges = new LinkedList<Change>();
			LinkedList<Change> moonRashiChanges = new LinkedList<Change>();
			LinkedList<Change> nakshatraChange = new LinkedList<Change>();

			int index = 0;
			while (true) {

				TimeZone tz = calendar.getTimeZone();
				DateTimeZone jodaTz = DateTimeZone.forID(tz.getID());
				DateTime dateTime = new DateTime(calendar.getTimeInMillis(), jodaTz);
				
				info.setDateTime(dateTime);
				info.calc();

				Nakshatra nakshatra = info.getMoonNakshatra();
				Rashi moonRashi = info.getMoonRashi();
				Rashi sunRashi = info.getSunRashi();
				int tithi = info.getTithi();

				if (lastTithi == 0) {
					lastTithi = tithi;
					lastTithiTime = calendar.getTimeInMillis();
				}

				if (lastTithi != tithi) {
					tithiChanges.add(new Change(tithi, calendar
							.getTimeInMillis()));
					lastTithi = tithi;
					lastTithiTime = calendar.getTimeInMillis();
				}

				if (lastNakshatra == null) {
					lastNakshatra = nakshatra;
				}

				if (!lastNakshatra.equals(nakshatra)) {
					nakshatraChange.add(new Change(nakshatra, calendar
							.getTimeInMillis()));
					lastNakshatra = nakshatra;
					lastNakshatraTime = calendar.getTimeInMillis();
				}

				if (lastMoonRashi == null) {
					lastMoonRashi = moonRashi;
				}

				if (!lastMoonRashi.equals(moonRashi)) {
					moonRashiChanges.add(new Change(moonRashi, calendar
							.getTimeInMillis()));
					lastMoonRashi = moonRashi;
					lastMoonRashiTime = calendar.getTimeInMillis();
				}

				calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);

				int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

				if (lastDay != currentDay) {

					int tithi1 = lastTithi;
					long tithiTime1 = lastTithiTime;
					if (tithiChanges.size() > 0) {
						Change tc = tithiChanges.get(0);
						tithi1 = tc.getNum();
						tithiTime1 = tc.getTime();
					}
					int tithi2 = 0;
					long tithiTime2 = 0;
					if (tithiChanges.size() > 1) {
						Change tc = tithiChanges.get(1);
						tithi2 = tc.getNum();
						tithiTime2 = tc.getTime();
					}
					if (tithiChanges.size() > 2) {
						throw new Exception(
								"Tithi has been changed more then twice!");
					}

					int nakshatra1 = lastNakshatra.getNumber();
					long nakshatraTime1 = lastNakshatraTime;
					if (nakshatraChange.size() > 0) {
						Change tc = nakshatraChange.get(0);
						nakshatra1 = tc.getNakshatra().getNumber();
						nakshatraTime1 = tc.getTime();
					}
					int nakshatra2 = lastNakshatra.getNumber();
					long nakshatraTime2 = lastNakshatraTime;
					if (nakshatraChange.size() > 1) {
						Change tc = nakshatraChange.get(1);
						nakshatra2 = tc.getNakshatra().getNumber();
						nakshatraTime2 = tc.getTime();
					}
					if (nakshatraChange.size() > 2) {
						throw new Exception(
								"Nakshatra has been changed more then twice!");
					}

					int moonRashi1 = lastMoonRashi.getNumber();
					long moonRashiTime1 = lastMoonRashiTime;
					if (moonRashiChanges.size() > 0) {
						Change tc = moonRashiChanges.get(0);
						moonRashi1 = tc.getNum();
						moonRashiTime1 = tc.getTime();
					}
					if (moonRashiChanges.size() > 1) {
						throw new Exception(
								"Moon rashi has been changed more then once");
					}

					Calendar cal = (Calendar) calendar.clone();
					cal.add(Calendar.DAY_OF_MONTH, -1);

					System.out.println(cal.getTime());

					st = db.prepare("INSERT INTO moon_calendar VALUES("
							+ (index + 1) + ","
							+ cal.get(Calendar.DAY_OF_MONTH) + ","
							+ (cal.get(Calendar.MONTH) + 1) + ","
							+ cal.get(Calendar.YEAR) + "," + moonRashi1 + ","
							+ moonRashiTime1 + "," + sunRashi.getNumber() + ","
							+ tithi1 + "," + tithiTime1 + "," + tithi2 + ","
							+ tithiTime2 + "," + nakshatra1 + ","
							+ nakshatraTime1 + "," + nakshatra2 + ","
							+ nakshatraTime2 + ")");

					st.step();

					tithiChanges.clear();
					nakshatraChange.clear();
					moonRashiChanges.clear();

					index++;

					lastDay = currentDay;
				}

				if (calendar.get(Calendar.YEAR) == endYear
						&& calendar.get(Calendar.MONTH) == 1) {
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