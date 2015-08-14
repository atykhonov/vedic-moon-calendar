package org.satvika.jyotish.moon.calendar.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.satvika.jyotish.common.Translator;

public abstract class View {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd-MMM-yyyy HH:mm:ss");

	private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");

	public abstract String render();

	protected SimpleDateFormat getDateFormat() {
		// System.out.println(dateFormat2.getTimeZone());
		// dateFormat2.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
		return dateFormat2;
	}

	private Translator translator = new Translator();

	protected DateTime timeToCalendarGmt(long time) {

		MutableDateTime mutableDateTime = new MutableDateTime();
		mutableDateTime.setZone(DateTimeZone.UTC);
		mutableDateTime.setMillis(time);
		return mutableDateTime.toDateTime();

		// DateTime dateTime = new DateTime(time);
		// dateTime = dateTime.withZone(DateTimeZone.UTC).to

		/*
		 * Calendar calendar = Calendar.getInstance();
		 * 
		 * calendar = convertToGmt(calendar);
		 * 
		 * TimeZone tz = TimeZone.getTimeZone("Europe/Moscow");
		 * calendar.setTimeInMillis(time + tz.getOffset(time));
		 */

		// return dateTime;
	}

	protected Calendar convertToGmt(Calendar cal) {

		Date date = cal.getTime();
		TimeZone tz = cal.getTimeZone();
		// tz = TimeZone.getTimeZone("Europe/Moscow");

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
		gmtCal.add(Calendar.MILLISECOND, -offsetFromUTC);

		// log.debug("Created GMT cal with date [" + gmtCal.getTime() + "]");

		return gmtCal;
	}

	protected Translator getTranslator() {
		return this.translator;
	}

	protected void setTranslator(Translator translator) {
		this.translator = translator;
	}
}