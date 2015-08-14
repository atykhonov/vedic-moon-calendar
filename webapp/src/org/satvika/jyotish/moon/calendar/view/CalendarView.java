package org.satvika.jyotish.moon.calendar.view;

import java.util.LinkedList;
import java.util.Locale;

import org.joda.time.MutableDateTime;

public class CalendarView extends View {

	private LinkedList<CalendarDayView> dayViews = new LinkedList<CalendarDayView>();

	private int month;

	private int year;

	private Locale locale;

	private MutableDateTime dateTime = new MutableDateTime();

	public CalendarView(int month, int year) {
		this.month = month;
		this.year = year;

		dateTime.setMonthOfYear(this.month);
		dateTime.setYear(this.year);

		init();
	}

	public CalendarView(MutableDateTime dateTime) {

		this.dateTime = dateTime;

		this.month = dateTime.getMonthOfYear();
		this.year = dateTime.getYear();

		init();
	}

	private void init() {
		locale = new Locale("ru");
	}
	
	public int getMonth() {
		return this.month;
	}

	public String getDisplayYear() {
		return (new Integer(this.year)).toString();
	}

	public String[] getWeekDays() {
		String[] weekDays = { "Понедельник", "Вторник", "Среда", "Четверг",
				"Пятница", "Суббота", "Воскресенье" };
		return weekDays;
	}

	public void addCalendarDayView(CalendarDayView dayView) {
		dayView.setCalendarView(this);
		dayViews.add(dayView);
	}

	public LinkedList<CalendarDayView> getDayViews() {
		return this.dayViews;
	}

	public String getDisplayMonth(int offset) {
		MutableDateTime dt = (MutableDateTime) dateTime.clone();
		dt.addMonths(offset);
		return dt.monthOfYear().getAsText(locale);
	}

	public String getFilename() {
		return getFilename(0);
	}

	public String getFilename(int offset) {
		MutableDateTime mutableDateTime = (MutableDateTime) dateTime.clone();
		mutableDateTime.addMonths(offset);
		String month = mutableDateTime.monthOfYear().getAsText(Locale.ENGLISH);
		month = month.toLowerCase();
		return month + "-" + mutableDateTime.getYear() + ".html";
	}

	@Override
	public String render() {

		StringBuffer html = new StringBuffer();

		html.append("<html>");
		html.append("<head>");
		html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		html.append("</head>");
		html.append("<body>");

		html.append("<table width=\"100%\">");
		html.append("<tr>");
		html.append("<td align=\"center\" colspan=\"7\">");

		html.append("<table width=\"35%\">");
		html.append("<tr>");
		html.append("<td align=\"center\" style=\"vertical-align:top; padding-top: 10px; font-family: verdana;\">");

		html.append("<a href=\"" + getFilename(-1) + "\">");
		html.append("&lt;-&nbsp;" + getDisplayMonth(-1));
		html.append("</a>");

		html.append("</td>");
		html.append("<td align=\"center\">");

		html.append("<h1>");
		html.append(getDisplayMonth(0) + " " + this.year);
		html.append("</h2>");

		html.append("</td>");
		html.append("<td align=\"center\" style=\"vertical-align:top; padding-top: 10px; font-family: verdana;\">");

		html.append("<a href=\"" + getFilename(1) + "\">");
		html.append(getDisplayMonth(1) + "&nbsp;-&gt;");
		html.append("</a>");

		html.append("</td>");
		html.append("</tr>");
		html.append("</table>");

		html.append("</td>");
		html.append("</tr>");

		html.append("<tr>");

		String[] weekDays = { "Понедельник", "Вторник", "Среда", "Четверг",
				"Пятница", "Суббота", "Воскресенье" };

		for (int i = 0; i < 7; i++) {
			// CalendarDayView dayView = dayViews.get(i);
			// calendar.set(Calendar.DAY_OF_MONTH, dayView.getDayOfMonth());
			html.append("<td valign=\"top\">");
			html.append("<h3>");
			html.append(weekDays[i]);
			html.append("</h3>");
			html.append("</td>");
		}

		html.append("</tr>");

		int i = 1;
		for (CalendarDayView view : getDayViews()) {
			if (i == 1) {
				html.append("<tr>");
			}
			html.append(view.render());
			i++;
			if (i == 8) {
				html.append("</tr>");
				i = 1;
			}
		}

		html.append("</table>");

		html.append("</body>");
		html.append("</html>");

		return html.toString();
	}
}
