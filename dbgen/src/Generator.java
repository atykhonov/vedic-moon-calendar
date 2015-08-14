import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.satvika.jyotish.Nakshatra;
import org.satvika.jyotish.Rashi;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class Generator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SQLiteConnection db = new SQLiteConnection(
				new File(args[0]));

		SQLiteStatement st = null;
		try {
			db.openReadonly();

			st = db.prepare("SELECT * FROM moon_calendar WHERE day = 9 AND month = 10 AND year = 2012");

			while (st.step()) {

				for (String string : TimeZone.getAvailableIDs(TimeZone
						.getTimeZone("UTC+03:00").getRawOffset())) {
					// System.out.println(string);
				}

				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd-MMM-yyyy HH:mm:ss");

				// TimeZone tz = TimeZone.getTimeZone("Europe/Moscow");
				// sdf.setTimeZone(tz);

				Rashi moonRashi = Rashi.getByNumber(st.columnInt(4));
				System.out.println("Moon rashi: " + moonRashi.toString());

				int tithi = st.columnInt(6);
				Calendar calendar = Calendar.getInstance();

				long tithiTime = st.columnLong(7);
				calendar.setTimeInMillis(tithiTime);
				calendar = convertToGmt(calendar);
				System.out.println("Tithi: " + tithi + " ("
						+ sdf.format(calendar.getTime()) + ")");

				Nakshatra nakshatra = Nakshatra.getByNumber(st.columnInt(10));
				long nakshatraTime = st.columnLong(11);
				calendar = Calendar.getInstance();
				calendar.setTimeInMillis(nakshatraTime);
				calendar = convertToGmt(calendar);
				System.out.println("Nakshatra: " + nakshatra.toString() + " ("
						+ sdf.format(calendar.getTime()) + ")");
			}

		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			st.dispose();
		}
		db.dispose();
	}

	public static Calendar convertToGmt(Calendar cal) {

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
