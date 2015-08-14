package org.satvika.jyotish.moon.calendar.view;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class SimpleEntityView extends View {

	private DateTime dateTime;

	public SimpleEntityView(DateTime dateTime) {
		setDateTime(dateTime);
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public DateTime getDateTime() {
		return this.dateTime;
	}

	public String getFormattedTime() {

		DateTimeFormatter dateTimeFormatter = DateTimeFormat
				.forPattern("HH:mm");
		TimeZone timeZone = TimeZone.getTimeZone("Europe/Moscow");
		DateTimeZone dateTimeZone = DateTimeZone.forTimeZone(timeZone);
		dateTimeFormatter = dateTimeFormatter.withZone(dateTimeZone);

		return dateTimeFormatter.print(getDateTime());
	}

	public String render(String entity) {

		StringBuffer html = new StringBuffer();

		html.append(entity);

		if (getDateTime() != null) {
			html.append(" (" + getFormattedTime() + ")");
		}

		html.append("<br />");

		return html.toString();
	}
}
