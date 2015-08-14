package org.satvika.moon.calendar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.satvika.jyotish.Graha;
import org.satvika.jyotish.Nakshatra;
import org.satvika.jyotish.Rashi;
import org.satvika.jyotish.moon.calendar.pools.DatePool;
import org.satvika.jyotish.moon.calendar.pools.DatePoolEntry;
import org.satvika.jyotish.moon.calendar.view.CalendarDayView;
import org.satvika.jyotish.moon.calendar.view.CalendarView;
import org.satvika.jyotish.moon.calendar.view.EclipseView;
import org.satvika.jyotish.moon.calendar.view.MoonRashiView;
import org.satvika.jyotish.moon.calendar.view.MoonRashiViewFactory;
import org.satvika.jyotish.moon.calendar.view.NakshatraView;
import org.satvika.jyotish.moon.calendar.view.NakshatraViewFactory;
import org.satvika.jyotish.moon.calendar.view.SimpleEntityView;
import org.satvika.jyotish.moon.calendar.view.SunRashiView;
import org.satvika.jyotish.moon.calendar.view.SunRashiViewFactory;
import org.satvika.jyotish.moon.calendar.view.TithiView;
import org.satvika.jyotish.moon.calendar.view.TithiViewFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;


class WeekDay {

    private int phase = 0;

    private int dayOfMonth = 0;

    private boolean moonEclipse = false;

    private boolean sunEclipse = false;

    private String eclipseTime = "";

    private String[] tithi = null;
    
    public int getPhase() {
        return this.phase;        
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getDayOfMonth() {
        return this.dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public boolean isMoonEclipse() {
        return this.moonEclipse;
    }

    public void setMoonEclipse(boolean moonEclipse) {
        this.moonEclipse = moonEclipse;
    }

    public boolean isSunEclipse() {
        return this.sunEclipse;
    }

    public void setSunEclipse(boolean sunEclipse) {
        this.sunEclipse = sunEclipse;
    }

    public String getEclipseTime() {
        return this.eclipseTime;
    }

    public void setEclipseView(String eclipseTime) {
        this.eclipseTime = eclipseTime;
    }

    public String[] getTithi() {
        return this.tithi;
    }
}

@Controller
public class MainController {

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"dd-MMM-yyyy HH:mm:ss");

	private static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

	private SQLiteConnection connection;

	private DateTimeZone currentDateTimeZone = null;

	public MainController() {
		TimeZone timeZone = TimeZone.getTimeZone("Europe/Moscow");
		currentDateTimeZone = DateTimeZone.forTimeZone(timeZone);
		DateTimeZone.setDefault(currentDateTimeZone);
		TimeZone.setDefault(timeZone);
	}

	@RequestMapping(value = "/")
	public ModelAndView home() {

		DateTime dateTime = new DateTime();

		return home(dateTime.getMonthOfYear(), dateTime.getYear());
	}

	@RequestMapping(value = "/{month}-{year}.html", method = RequestMethod.GET)
	public ModelAndView home(@PathVariable String month, @PathVariable int year) {

		int givenMonth = 0;
		try {
			givenMonth = getDateByMonthName(month).getMonthOfYear();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return home(givenMonth, year);
	}

    @RequestMapping(value = "/api/v1/{year}/{month}", method = RequestMethod.GET)
    public @ResponseBody String api(@PathVariable String month, @PathVariable int year) {
        int givenMonth = 0;
        try {
            givenMonth = getDateByMonthName(month).getMonthOfYear();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ModelAndView mv = new ModelAndView();

        // if (year >= 2012 && givenMonth <= 2015) {

        //     CalendarView calendarView = new CalendarView(givenMonth, year);

        //     mv.addObject("weeks", generate(calendarView, givenMonth, year));
        // } else {
        //     mv.setViewName("page-not-found");
        // }

        CalendarView calendarView = new CalendarView(givenMonth, year);
        // List<ArrayList<CalendarDayView>> = generate(calendarView, givenMonth, year);
        return generateJson(calendarView, givenMonth, year);
    }

	public ModelAndView home(int month, int year) {

		ModelAndView mv = new ModelAndView();

		int givenMonth = month;
		int givenYear = year;

		if (givenYear >= 2012 && givenYear <= 2022) {

			CalendarView calendarView = new CalendarView(givenMonth, givenYear);

			mv.addObject("weeks", generate(calendarView, givenMonth, givenYear));

			mv.addObject("calendar", calendarView);

			DateTime dateTime = new DateTime();
			if (dateTime.getMonthOfYear() == month
					&& dateTime.getYear() == year) {
				mv.addObject("current_day", dateTime.getDayOfMonth());
				mv.addObject("current_month", dateTime.getMonthOfYear());
			}

			if (givenMonth == 1 && givenYear == 2012) {
				mv.addObject("hidePrevious", true);
			}

			if (givenMonth == 12 && givenYear == 2022) {
				mv.addObject("hideNext", true);
			}

			mv.setViewName("main");
		} else {
			mv.setViewName("page-not-found");
		}

		return mv;
	}

	private DateTime getDateByMonthName(String name) throws ParseException {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
		return formatter.parseDateTime(name);

		/*
		 * SimpleDateFormat formatter = new SimpleDateFormat("MMMMM",
		 * Locale.ENGLISH); return formatter.parse(name);
		 */
	}

	private List<ArrayList<CalendarDayView>> generate(
			CalendarView calendarView, int givenMonth, int givenYear) {

		List<ArrayList<CalendarDayView>> weeks = new ArrayList<ArrayList<CalendarDayView>>();

		ClassPathResource classPathResource = new ClassPathResource(
				"calendar_newest.sqlite");

		try {
			connection = new SQLiteConnection(classPathResource.getFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			connection.openReadonly();

			DatePool tithiDatePool = createDatePoolForTable(
					"moon_tithi_changes", givenMonth, givenYear);

			DatePool moonRashiDatePool = createDatePoolForTable(
					"moon_rashi_changes", givenMonth, givenYear);

			DatePool sunRashiDatePool = createDatePoolForTable(
					"sun_rashi_changes", givenMonth, givenYear);

			DatePool nakshatraDatePool = createDatePoolForTable(
					"moon_nakshatra_changes", givenMonth, givenYear);

			DatePool eclipseDatePool = createDatePoolForTable("eclipses",
					givenMonth, givenYear);

			MutableDateTime dateTime = new MutableDateTime(givenYear,
					givenMonth, 1, 0, 0, 0, 0, currentDateTimeZone);

			DateTime givenDateTime = dateTime.toDateTime();

			int dayWeeksOffset = 0;
			while (dateTime.getDayOfWeek() != DateTimeConstants.MONDAY) {
				dateTime.addDays(-1);
				dayWeeksOffset++;
			}

			int daysInMonth = givenDateTime.dayOfMonth().getMaximumValue(); // calendar.getMaximum(Calendar.DAY_OF_MONTH);

			int maxIndex = 36;
			if (daysInMonth + dayWeeksOffset > 35) {
				maxIndex = 43;
			}

			int currentWeekNumber = 0;

			ArrayList<CalendarDayView> daysInWeek = new ArrayList<CalendarDayView>();

			for (int index = 1; index < maxIndex; index++) {

				CalendarDayView dayView = new CalendarDayView(
						dateTime.getDayOfMonth(), dateTime.getMonthOfYear());

				calendarView.addCalendarDayView(dayView);

				daysInWeek.add(dayView);

				if (daysInWeek.size() == 7) {

					weeks.add(daysInWeek);

					daysInWeek = new ArrayList<CalendarDayView>();
				}

				if (dateTime.getMonthOfYear() == givenMonth) {

					/*
					 * if (currentWeekNumber == dateTime.getWeekyear() calendar
					 * .get(Calendar.WEEK_OF_MONTH)) {
					 * 
					 * currentWeekNumber = calendar
					 * .get(Calendar.WEEK_OF_MONTH); }
					 */

					TithiViewFactory tithiViewFactory = new TithiViewFactory(
							tithiDatePool, dateTime.toDateTime());

					dayView.addTithiViews(tithiViewFactory.createViews());

					MoonRashiViewFactory moonRashiViewFactory = new MoonRashiViewFactory(
							moonRashiDatePool, dateTime.toDateTime());

					dayView.addMoonRashiViews(moonRashiViewFactory
							.createViews());

					SunRashiViewFactory sunRashiViewFactory = new SunRashiViewFactory(
							sunRashiDatePool, dateTime.toDateTime());

					dayView.addSunRashiViews(sunRashiViewFactory.createViews());

					NakshatraViewFactory nakshatraViewFactory = new NakshatraViewFactory(
							nakshatraDatePool, dateTime.toDateTime());

					dayView.addNakshatraViews(nakshatraViewFactory
							.createViews());

					dayView.setEclipseView(retrieveEclipseViewFromPool(
							eclipseDatePool, dateTime.toDateTime()));

				} else {
					dayView.setEmpty(true);
				}

				dateTime.addDays(1);
			}

		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		return weeks;
	}

        private String generateJson(
            CalendarView calendarView, int givenMonth, int givenYear) {

            ArrayList<CalendarDayView> daysInWeek = new ArrayList<CalendarDayView>();
            // List<CalendarDayView> days = List<CalendarDayView>();

            // List<ArrayList<CalendarDayView>> weeks = new ArrayList<ArrayList<CalendarDayView>>();

            ClassPathResource classPathResource = new ClassPathResource(
                "calendar_newest.sqlite");

            try {
                connection = new SQLiteConnection(classPathResource.getFile());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                connection.openReadonly();

                DatePool tithiDatePool = createDatePoolForTable(
                    "moon_tithi_changes", givenMonth, givenYear);

                DatePool moonRashiDatePool = createDatePoolForTable(
                    "moon_rashi_changes", givenMonth, givenYear);

                DatePool sunRashiDatePool = createDatePoolForTable(
                    "sun_rashi_changes", givenMonth, givenYear);

                DatePool nakshatraDatePool = createDatePoolForTable(
                    "moon_nakshatra_changes", givenMonth, givenYear);

                DatePool eclipseDatePool = createDatePoolForTable("eclipses",
                                                                  givenMonth, givenYear);

                MutableDateTime dateTime = new MutableDateTime(givenYear,
                                                               givenMonth, 1, 0, 0, 0, 0, currentDateTimeZone);

                DateTime givenDateTime = dateTime.toDateTime();

                int dayWeeksOffset = 0;
                while (dateTime.getDayOfWeek() != DateTimeConstants.MONDAY) {
                    dateTime.addDays(-1);
                    dayWeeksOffset++;
                }

                int daysInMonth = givenDateTime.dayOfMonth().getMaximumValue(); // calendar.getMaximum(Calendar.DAY_OF_MONTH);

                int maxIndex = 36;
                if (daysInMonth + dayWeeksOffset > 35) {
                    maxIndex = 43;
                }

                int currentWeekNumber = 0;
                
                // maxIndex = 30;
                                
                for (int index = 1; index < maxIndex; index++) {

                    CalendarDayView dayView = new CalendarDayView(
                        dateTime.getDayOfMonth(), dateTime.getMonthOfYear());

                    calendarView.addCalendarDayView(dayView);

                    daysInWeek.add(dayView);

                    // if (daysInWeek.size() == 7) {

                    //     weeks.add(daysInWeek);

                    //     daysInWeek = new ArrayList<CalendarDayView>();
                    // }

                    if (dateTime.getMonthOfYear() == givenMonth) {

                        /*
                         * if (currentWeekNumber == dateTime.getWeekyear() calendar
                         * .get(Calendar.WEEK_OF_MONTH)) {
                         * 
                         * currentWeekNumber = calendar
                         * .get(Calendar.WEEK_OF_MONTH); }
                         */

                        TithiViewFactory tithiViewFactory = new TithiViewFactory(
                            tithiDatePool, dateTime.toDateTime());

                        dayView.addTithiViews(tithiViewFactory.createViews());

                        MoonRashiViewFactory moonRashiViewFactory = new MoonRashiViewFactory(
                            moonRashiDatePool, dateTime.toDateTime());

                        dayView.addMoonRashiViews(moonRashiViewFactory
                                                  .createViews());

                        SunRashiViewFactory sunRashiViewFactory = new SunRashiViewFactory(
                            sunRashiDatePool, dateTime.toDateTime());

                        dayView.addSunRashiViews(sunRashiViewFactory.createViews());

                        NakshatraViewFactory nakshatraViewFactory = new NakshatraViewFactory(
                            nakshatraDatePool, dateTime.toDateTime());

                        dayView.addNakshatraViews(nakshatraViewFactory
                                                  .createViews());

                        dayView.setEclipseView(retrieveEclipseViewFromPool(
                                                   eclipseDatePool, dateTime.toDateTime()));

                    } else {
                        dayView.setEmpty(true);
                    }

                    dateTime.addDays(1);
                }

            } catch (SQLiteException e) {
                e.printStackTrace();
            }

            return daysInWeekToJsonArray(daysInWeek, givenMonth, givenYear);
	}

    private String daysInWeekToJsonArray(ArrayList<CalendarDayView> daysInWeek, int month, int year) {
    	JSONArray result = new JSONArray();
    	for (CalendarDayView day: daysInWeek) {
    		
    		JSONArray jDays = new JSONArray();
    		
    		JSONObject jDay = new JSONObject();
    		jDay.put("day", day.getDayOfMonth());
    		jDay.put("month", day.getMonth());
    		jDay.put("year", new Integer(year).toString());

    		if (day.getTithiViews().isEmpty()) {
    			jDay.put("dummy", true);
    		} else {
    			jDay.put("dummy", false);
    			
    			jDay.put("phaze", day.getPhase());
    			
        		JSONArray jTithis = new JSONArray();
        		for (SimpleEntityView view: day.getTithiViews()) {
        			JSONObject jTithi = new JSONObject();
        			jTithi.put("time", view.getFormattedTime());
        			jTithi.put("tithi", ((TithiView) view).getTithiNum());
        			jTithis.put(jTithi);
        		}
        		jDay.put("tithi", jTithis);
        		
        		JSONObject jMoonRashi = new JSONObject();
        		for (SimpleEntityView view: day.getMoonRashiViews()) {
        			jMoonRashi.put("time", view.getFormattedTime());
        			jMoonRashi.put("name", ((MoonRashiView) view).getRashi());
        		}
        		jDay.put("moon", jMoonRashi);
        		
        		JSONObject jNakshatra = new JSONObject();
        		for (SimpleEntityView view: day.getNakshatraViews()) {
        			jNakshatra.put("number", ((NakshatraView) view).getNakshatra().getNumber());
        			jNakshatra.put("name", ((NakshatraView) view).getNakshatra());
        			jNakshatra.put("time", ((NakshatraView) view).getFormattedTime());
        		}
        		jDay.put("nakshatra", jNakshatra);
        		
        		JSONObject jSunRashi = new JSONObject();
        		for (SimpleEntityView view: day.getSunRashiViews()) {
        			jSunRashi.put("rashi", ((SunRashiView) view).getRashi());
        		}
        		jDay.put("sun", jSunRashi);	
    		}

    		jDays.put(jDay);
    		
    		result.put(jDays);
    	}
    	return result.toString();
    }

	private SimpleEntityView retrieveEclipseViewFromPool(
			DatePool eclipseDatePool, DateTime dateTime) {

		SimpleEntityView result = null;

		List<DatePoolEntry> entries = eclipseDatePool.get(dateTime);
		for (DatePoolEntry entry : entries) {
			EclipseView eclipseView = new EclipseView(entry.getDateTime(),
					Graha.getByNumber(entry.getValue() + 1));
			result = eclipseView;
		}

		return result;
	}

	private List<NakshatraView> retrieveNakshatraViewsFromPool(
			DatePool nakshatraDatePool, DateTime dateTime) {

		List<NakshatraView> result = new LinkedList<NakshatraView>();

		List<DatePoolEntry> entries = nakshatraDatePool.get(dateTime);
		for (DatePoolEntry entry : entries) {
			NakshatraView nakshatraView = new NakshatraView(
					Nakshatra.getByNumber(entry.getValue()),
					entry.getDateTime());
			result.add(nakshatraView);
		}

		return result;
	}

	private List<SunRashiView> retrieveSunRashiViewsFromPool(
			DatePool sunRashiDatePool, DateTime dateTime) {

		List<SunRashiView> result = new LinkedList<SunRashiView>();

		List<DatePoolEntry> entries = sunRashiDatePool.get(dateTime);
		for (DatePoolEntry entry : entries) {
			SunRashiView rashiView = new SunRashiView(Rashi.getByNumber(entry
					.getValue()), entry.getDateTime());
			result.add(rashiView);
		}

		return result;
	}

	private List<MoonRashiView> retrieveMoonRashiViewsFromPool(
			DatePool moonRashiDatePool, DateTime dateTime) {

		List<MoonRashiView> result = new LinkedList<MoonRashiView>();

		List<DatePoolEntry> entries = moonRashiDatePool.get(dateTime);
		for (DatePoolEntry entry : entries) {
			MoonRashiView rashiView = new MoonRashiView(Rashi.getByNumber(entry
					.getValue()), entry.getDateTime());
			result.add(rashiView);
		}

		return result;
	}

	private List<TithiView> retrieveTithiViewsFromPool(DatePool tithiDatePool,
			DateTime dateTime) {

		List<TithiView> result = new LinkedList<TithiView>();

		List<DatePoolEntry> entries = tithiDatePool.get(dateTime);
		if (entries.isEmpty()) {
			while (entries.isEmpty()) {
				dateTime = dateTime.minusDays(1);
				entries = tithiDatePool.get(dateTime);
			}
			DatePoolEntry entry = entries.get(entries.size() - 1);
			result.add(new TithiView(entry.getValue(), null));
		} else {
			for (DatePoolEntry entry : entries) {
				TithiView tithiView = new TithiView(entry.getValue(),
						entry.getDateTime());
				result.add(tithiView);
			}
		}

		return result;
	}

	private DatePool createDatePoolForTable(String table, int givenMonth,
			int givenYear) throws SQLiteException {

		DatePool datePool = new DatePool();

		MutableDateTime dateTime = new MutableDateTime(givenYear, givenMonth,
				1, 0, 0, 0, 0, currentDateTimeZone);

		dateTime.addDays(-20);

		DateTime from = dateTime.toDateTime();

		dateTime.addDays(55);
		DateTime to = dateTime.toDateTime();

		LinkedHashMap<Long, Integer> data = getData(table, from, to);

		for (Long time : data.keySet()) {
			Integer value = data.get(time);
			DatePoolEntry entry = new DatePoolEntry(value, time,
					currentDateTimeZone);
			datePool.add(entry);
		}

		return datePool;
	}

	private List<ArrayList<CalendarDayView>> generate2(
			CalendarView calendarView, int givenMonth, int givenYear) {

		List<ArrayList<CalendarDayView>> weeks = new ArrayList<ArrayList<CalendarDayView>>();

		ClassPathResource classPathResource = new ClassPathResource(
				"calendar_newest.sqlite");

		try {
			connection = new SQLiteConnection(classPathResource.getFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {

			connection.openReadonly();

			Calendar calendar = Calendar.getInstance();

			calendar.set(Calendar.MONTH, givenMonth);
			calendar.set(Calendar.YEAR, givenYear);

			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);

			calendar.set(Calendar.DAY_OF_MONTH, 1);

			// CalendarView calendarView = new CalendarView(givenMonth,
			// givenYear);

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

			int currentWeekNumber = 0;

			ArrayList<CalendarDayView> daysInWeek = new ArrayList<CalendarDayView>();

			for (int index = 1; index < maxIndex; index++) {

				CalendarDayView dayView = new CalendarDayView(
						calendar.get(Calendar.DAY_OF_MONTH),
						calendar.get(Calendar.MONTH));

				calendarView.addCalendarDayView(dayView);

				daysInWeek.add(dayView);

				if (daysInWeek.size() == 7) {

					weeks.add(daysInWeek);

					daysInWeek = new ArrayList<CalendarDayView>();
				}

				if (calendar.get(Calendar.MONTH) == givenMonth) {

					if (currentWeekNumber == calendar
							.get(Calendar.WEEK_OF_MONTH)) {

						currentWeekNumber = calendar
								.get(Calendar.WEEK_OF_MONTH);
					}

					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH);
					int day = calendar.get(Calendar.DAY_OF_MONTH);

					Calendar from = Calendar.getInstance();

					int offset = -4;

					from.set(year, month, day, offset, 0, 0);
					Calendar to = Calendar.getInstance();
					to.set(year, month, day + 1, offset, 0, 0);

					List<TithiView> tithiViews = retrieveTithiViews(from, to);
					for (TithiView view : tithiViews) {
						dayView.addTithiView(view);
					}

					List<MoonRashiView> moonRashiViews = retrieveMoonRashiViews(
							from, to);
					for (MoonRashiView view : moonRashiViews) {
						dayView.addMoonRashiView(view);
					}

					List<SunRashiView> sunRashiViews = retrieveSunRashiViews(
							from, to);
					for (SunRashiView view : sunRashiViews) {
						dayView.addSunRashiView(view);
					}

					List<NakshatraView> nakshatraViews = retrieveNakshatraViews(
							from, to);
					for (NakshatraView view : nakshatraViews) {
						dayView.addNakshatraView(view);
					}

					dayView.setEclipseView(retrieveEclipseView(from, to));
				} else {
					dayView.setEmpty(true);
				}

				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}

		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		connection.dispose();

		return weeks;
	}

	private SimpleEntityView retrieveEclipseView(Calendar from, Calendar to)
			throws SQLiteException {

		SimpleEntityView eclipseView = null;

		LinkedHashMap<Long, Integer> data = getData("eclipses", null, null);

		for (Long time : data.keySet()) {

			Integer value = data.get(time);
			// eclipseView = new EclipseView(time, Graha.getByNumber(value +
			// 1));
		}

		return eclipseView;
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

	private List<TithiView> retrieveTithiViews(Calendar from, Calendar to)
			throws SQLiteException {

		Calendar f = (Calendar) from.clone();
		Calendar t = (Calendar) to.clone();

		List<TithiView> views = createTithiViews(f, t);

		while (views.isEmpty()) {

			f.add(Calendar.DAY_OF_MONTH, -1);
			t.add(Calendar.DAY_OF_MONTH, -1);

			views = createTithiViews(f, t);

			for (TithiView view : views) {
				view.setDateTime(null);
			}
		}

		return views;
	}

	private List<TithiView> createTithiViews(Calendar from, Calendar to)
			throws SQLiteException {

		List<TithiView> views = new LinkedList<TithiView>();

		LinkedHashMap<Long, Integer> data = getData("moon_tithi_changes", null,
				null);

		for (Long time : data.keySet()) {
			Integer value = data.get(time);
			TithiView tithiView = null; // new TithiView(value, time);
			views.add(tithiView);
		}

		return views;
	}

	private List<MoonRashiView> retrieveMoonRashiViews(Calendar from,
			Calendar to) throws SQLiteException {

		Calendar f = (Calendar) from.clone();
		Calendar t = (Calendar) to.clone();

		List<MoonRashiView> views = createMoonRashiViews(f, t);

		while (views.isEmpty()) {

			f.add(Calendar.DAY_OF_MONTH, -1);
			t.add(Calendar.DAY_OF_MONTH, -1);

			views = createMoonRashiViews(f, t);

			for (MoonRashiView view : views) {
				view.setDateTime(null);
			}
		}

		return views;
	}

	private List<MoonRashiView> createMoonRashiViews(Calendar from, Calendar to)
			throws SQLiteException {

		List<MoonRashiView> views = new LinkedList<MoonRashiView>();

		LinkedHashMap<Long, Integer> data = getData("moon_rashi_changes", null,
				null);

		for (Long time : data.keySet()) {
			Integer value = data.get(time);
			MoonRashiView moonRashiView = null; // new MoonRashiView(
			// Rashi.getByNumber(value), time);

			views.add(moonRashiView);
		}

		return views;
	}

	private List<SunRashiView> retrieveSunRashiViews(Calendar from, Calendar to)
			throws SQLiteException {

		Calendar f = (Calendar) from.clone();
		Calendar t = (Calendar) to.clone();

		List<SunRashiView> views = createSunRashiViews(f, t);

		while (views.isEmpty()) {

			f.add(Calendar.DAY_OF_MONTH, -1);
			t.add(Calendar.DAY_OF_MONTH, -1);

			views = createSunRashiViews(f, t);

			for (SunRashiView view : views) {
				view.setDateTime(null);
			}
		}

		return views;
	}

	private List<SunRashiView> createSunRashiViews(Calendar from, Calendar to)
			throws SQLiteException {

		List<SunRashiView> views = new LinkedList<SunRashiView>();

		LinkedHashMap<Long, Integer> data = getData("sun_rashi_changes", null,
				null);

		for (Long time : data.keySet()) {
			Integer value = data.get(time);
			SunRashiView sunRashiView = null; // new SunRashiView(
			// Rashi.getByNumber(value), time);

			views.add(sunRashiView);
		}

		return views;
	}

	private List<NakshatraView> retrieveNakshatraViews(Calendar from,
			Calendar to) throws SQLiteException {

		Calendar f = (Calendar) from.clone();
		Calendar t = (Calendar) to.clone();

		List<NakshatraView> views = createNakshatraViews(f, t);

		while (views.isEmpty()) {

			f.add(Calendar.DAY_OF_MONTH, -1);
			t.add(Calendar.DAY_OF_MONTH, -1);

			views = createNakshatraViews(f, t);

			for (NakshatraView view : views) {
				view.setDateTime(null);
			}
		}

		return views;
	}

	private List<NakshatraView> createNakshatraViews(Calendar from, Calendar to)
			throws SQLiteException {

		List<NakshatraView> views = new LinkedList<NakshatraView>();

		LinkedHashMap<Long, Integer> data = getData("moon_nakshatra_changes",
				null, null);

		for (Long time : data.keySet()) {
			Integer value = data.get(time);
			NakshatraView nakshatraView = null; // new NakshatraView(
			// Nakshatra.getByNumber(value), time);

			views.add(nakshatraView);
		}

		return views;
	}

	private LinkedHashMap<Long, Integer> getData(String table, DateTime from,
			DateTime to) throws SQLiteException {

		LinkedHashMap<Long, Integer> map = new LinkedHashMap<Long, Integer>();

		SQLiteStatement st = connection.prepare(createSQLStatementForTable(
				table, from, to));

		while (st.step()) {
			map.put(st.columnLong(1), st.columnInt(2));
		}

		st.dispose();

		return map;
	}

	private String createSQLStatementForTable(String table, DateTime from,
			DateTime to) {

		return "SELECT * FROM " + table + " WHERE time >= " + from.getMillis()
				+ " AND time < " + to.getMillis();
	}

	public LinkedHashMap<Date, Object[]> getValuesMap(String table, int year,
			int month, int day) throws SQLiteException {

		LinkedHashMap<Date, Object[]> values = new LinkedHashMap<Date, Object[]>();

		Calendar from = Calendar.getInstance();
		from.set(year, month, day, -4, 0, 0);
		// System.out.println("1: " + from.getTime());
		// from = convertToGmt(from);
		// System.out.println("2: " + from.getTime());
		Calendar to = Calendar.getInstance();
		to.set(year, month, day + 1, -4, 0, 0);
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
