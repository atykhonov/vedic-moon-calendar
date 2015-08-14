package org.satvika.jyotish.moon.calendar.view;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CalendarDayView extends View {

	private int dayOfMonth = 0;

	private int month = 0;

	private CalendarView calendarView;

	private List<SimpleEntityView> tithiViews = new LinkedList<SimpleEntityView>();

	private List<SimpleEntityView> nakshatraViews = new LinkedList<SimpleEntityView>();

	private List<SimpleEntityView> moonRashiViews = new LinkedList<SimpleEntityView>();

	private List<SimpleEntityView> sunRashiViews = new LinkedList<SimpleEntityView>();

	private SimpleEntityView eclipseView = null;

	private boolean empty = false;

	public CalendarDayView(int dayOfMonth, int month) {
		setDayOfMonth(dayOfMonth);
		setMonth(month);
	}

	public void addTithiView(TithiView view) {
		tithiViews.add(view);
	}

	public void addTithiViews(List<TithiView> views) {
		tithiViews.addAll(views);
	}

	public List<SimpleEntityView> getTithiViews() {
		return this.tithiViews;
	}

	public void addNakshatraView(NakshatraView view) {
		nakshatraViews.add(view);
	}

	public void addNakshatraViews(List<NakshatraView> views) {
		nakshatraViews.addAll(views);
	}

	public List<SimpleEntityView> getNakshatraViews() {
		return this.nakshatraViews;
	}

	public void addMoonRashiView(MoonRashiView view) {
		moonRashiViews.add(view);
	}

	public void addMoonRashiViews(List<MoonRashiView> views) {
		moonRashiViews.addAll(views);
	}

	public List<SimpleEntityView> getMoonRashiViews() {
		return moonRashiViews;
	}

	public void addSunRashiView(SunRashiView view) {
		sunRashiViews.add(view);
	}

	public void addSunRashiViews(List<SunRashiView> views) {
		sunRashiViews.addAll(views);
	}

	public List<SimpleEntityView> getSunRashiViews() {
		return sunRashiViews;
	}

	private void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getDayOfMonth() {
		return this.dayOfMonth;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return this.month;
	}

	public void setCalendarView(CalendarView view) {
		this.calendarView = view;
	}

	public CalendarView getCalendarView() {
		return this.calendarView;
	}

	public int getPhase() {
		int result = 0;
		for (SimpleEntityView entityView : retrieveTithiViews()) {
			TithiView tithiView = (TithiView) entityView;
			result = tithiView.getTithiNum();
		}
		return result;
	}

	public List<SimpleEntityView> retrieveTithiViews() {

		List<SimpleEntityView> result = new LinkedList<SimpleEntityView>();

		result = getTithiViews();

		if (result.isEmpty()) {
			SimpleEntityView view = searchTithiView();
			if (view != null) {
				result.add(view);
			}
		}

		return result;
	}

	public List<SimpleEntityView> retrieveMoonRashiViews() {

		List<SimpleEntityView> result = new LinkedList<SimpleEntityView>();

		result = getMoonRashiViews();

		if (result.isEmpty()) {
			SimpleEntityView view = searchMoonRashiView();
			if (view != null) {
				result.add(view);
			}
		}
		return result;
	}

	public List<SimpleEntityView> retrieveNakshatraViews() {

		List<SimpleEntityView> result = new LinkedList<SimpleEntityView>();

		result = getNakshatraViews();

		if (result.isEmpty()) {
			SimpleEntityView view = searchNakshatraView();
			if (view != null) {
				result.add(view);
			}
		}
		return result;
	}

	public List<SimpleEntityView> retrieveSunRashiViews() {

		List<SimpleEntityView> result = new LinkedList<SimpleEntityView>();

		result = getSunRashiViews();

		if (result.isEmpty()) {
			SimpleEntityView view = searchSunRashiView();
			if (view != null) {
				result.add(view);
			}
		}
		return result;
	}

	private SimpleEntityView searchNakshatraView() {
		return searchView(NakshatraView.class);
	}

	private SimpleEntityView searchSunRashiView() {
		return searchView(SunRashiView.class);
	}

	private SimpleEntityView searchTithiView() {
		return searchView(TithiView.class);
	}

	private SimpleEntityView searchView(Class cls) {
		SimpleEntityView view = searchView(cls, true);
		if (view != null) {
			view.setDateTime(null);
		}
		return view;
	}

	private SimpleEntityView searchView(Class cls, boolean backward) {
		int correction = -1;
		SimpleEntityView result = null;
		LinkedList<CalendarDayView> daysViews = getCalendarView().getDayViews();
		int index = daysViews.indexOf(this);
		int cIndex = index + correction;
		while (result == null) {
			if (cIndex >= 0 && cIndex < daysViews.size()) {
				CalendarDayView dayView = daysViews.get(cIndex);
				List<SimpleEntityView> list = new LinkedList<SimpleEntityView>();
				if (TithiView.class.equals(cls)) {
					list = dayView.getTithiViews();
				} else if (MoonRashiView.class.equals(cls)) {
					list = dayView.getMoonRashiViews();
				} else if (SunRashiView.class.equals(cls)) {
					list = dayView.getSunRashiViews();
				} else if (NakshatraView.class.equals(cls)) {
					list = dayView.getNakshatraViews();
				}
				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					result = (SimpleEntityView) iterator.next();
				}
			} else {
				break;
			}
			cIndex = cIndex + correction;
		}
		return result;
	}

	private SimpleEntityView searchMoonRashiView() {
		return searchView(MoonRashiView.class);
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public void setEclipseView(SimpleEntityView view) {
		this.eclipseView = view;
	}

	public SimpleEntityView getEclipseView() {
		return this.eclipseView;
	}

	@Override
	public String render() {

		StringBuffer html = new StringBuffer();

		html.append("<td valign=\"top\">");
		html.append("<h1>");
		html.append(getDayOfMonth());
		html.append("</h1>");

		if (!isEmpty()) {
			for (SimpleEntityView view : retrieveTithiViews()) {
				html.append(view.render());
			}
			for (SimpleEntityView view : retrieveMoonRashiViews()) {
				html.append(view.render());
			}
			for (SimpleEntityView view : retrieveNakshatraViews()) {
				html.append(view.render());
			}
			for (SimpleEntityView view : retrieveSunRashiViews()) {
				html.append(view.render());
			}
		}

		html.append("</td>");

		return html.toString();
	}
}
