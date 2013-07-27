package com.beyobe.client.activities;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.event.EditQuestionEvent;
import com.beyobe.client.views.TagdayView;
import com.beyobe.client.widgets.Carousel;
import com.beyobe.client.widgets.Day;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;

public class TagdayActivity extends AbstractActivity implements TagdayView.Presenter {
    private Place defaultPlace;

    private static Logger log = Logger.getLogger("TagdayActivity");
    
    public TagdayActivity(Place defaultPlace) {
        this.defaultPlace = defaultPlace;
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
    	if (!App.isUserLoggedIn()) {
    		App.placeController.goTo(new LoginPlace());
    		return;
    	}
    	App.tagdayView.setPresenter(this);
        loadDays();
        containerWidget.setWidget(App.tagdayView.asWidget());
    }

    //TODO load tags dynamically
    private void loadDays() {
//    	Date date = new Date();
//    	Day todayDay = new Day(date);
//    	
//        Question q1 = new Question();
//        q1.setId(1L);
//        q1.setLongForm("What is your weight?");
//        q1.setShortForm("Weight");
//        q1.setAbbreviation("Wt");
//        q1.setDataType(Question.DATA_TYPE_DOUBLE);
//        q1.setReferenceUnit(Unit.KG);
//        Question q2 = new Question();
//        q2.setId(2L);
//        q2.setLongForm("How happy are you?");
//        q2.setShortForm("Happy");
//        q2.setAbbreviation("Happy");
//        Datum d2 = new Datum();
//        d2.setQuestionId(q2.getId());
//        d2.setNumericValue(4.0);
//        d2.setTextValue("4");
//        TagButton tb1 = new TagButton(date, q1, null);
//        TagButton tb2 = new TagButton(date, q2, d2);
//        todayDay.addTagButton(tb1);
//        todayDay.addTagButton(tb2);
//    	
//		tagdayView.addDay(todayDay);
//		
//		CalendarUtil.addDaysToDate(date, 1);
//    	Day tomorrowDay = new Day(date);
//    	tagdayView.addDay(tomorrowDay);
    	
    	List<Day> days = App.dataBus.getAllDays();
    	if (days == null) {
    		days = App.dataBus.createAllDays();
    	}
    	for (Day day: days) {
    		addDayOntoEndOfCarousel(day);
    		log.info("Added day: " + day);
    	}
	}

	/**
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        return null;
    }

    /**
     * Navigate to a new Place in the browser
     */
    public void goTo(Place place) {
        App.placeController.goTo(place);
    }

	@Override
	public void onAddTag(TapEvent e) {
		App.question = null;
		App.eventBus.fireEvent(new EditQuestionEvent(null));
	}

	@Override
	public void onAttach() {
		goToDate(new Date());
   	 	updateNavigation();
	}
	
	@Override
	public void updateNavigation() {
		Day day = getCurrentDay();
		if (day==null) return;
		log.info("updateNavigation to day: " + day);
		App.tagdayView.getDateLabel().setText(day.getDateText());
		log.info("updateNavigation to date: " + day.getDateText());
		App.tagdayView.getMonthLabel().setText(day.getMonthText());
		App.tagdayView.getYearLabel().setText(day.getYearText());
	}

	@Override
	public void goToDate(Date d) {
		if (d == null) return;
		Carousel carousel = App.tagdayView.getCarousel();
		
		//if the date is already on the caurosel, scroll to it
		int dateIndex = carousel.indexOf(d);
		if (dateIndex >= 0) {
			carousel.setSelectedPage(dateIndex);
			updateNavigation();
			return;
		}
		
		//otherwise if the date is 1 day earlier, gather data for that day and add it on to the beginning
		Date oneDayBeforeCaurosel = carousel.getEarliestDate();
		CalendarUtil.addDaysToDate(oneDayBeforeCaurosel, -1);
		if(d.before(carousel.getEarliestDate()) && (d.equals(oneDayBeforeCaurosel) || d.after(oneDayBeforeCaurosel))) {
			//TODO load data from server
			log.info("addDayOntoBeginning...");
			Day day = App.dataBus.addDayOntoBeginning(d);
			addDayOntoBeginningOfCarousel(day);
			carousel.setSelectedPage(0);
			log.info(carousel.toString());
			updateNavigation();
			return;
		}
		
		//otherwise if the date is 1 day later, gather data for that day and add it on to the end
		Date oneDayAfterCaurosel = carousel.getLatestDate();
		CalendarUtil.addDaysToDate(oneDayAfterCaurosel, 1);
		if(d.after(carousel.getLatestDate()) && (d.equals(oneDayAfterCaurosel) || d.before(oneDayAfterCaurosel))) {
			log.info("addDayOntoEnd...");
			//TODO load data from server
			Day day = App.dataBus.addDayOntoEnd(d);
			addDayOntoEndOfCarousel(day);
			carousel.setSelectedPage(carousel.size()-1);
			log.info(carousel.toString());
			updateNavigation();
		}
		
		//otherwise dump the carousel and load new data completely
		//TODO support creating remote days
	}
	
	@Override
	public Day getCurrentDay() {
	 	Widget w = App.tagdayView.getCarousel().getCurrentWidget();
	 	if (w != null && w instanceof Day) {
	 		return (Day)w;
	 	}
	 	return null;
	}
	
	@Override
	public void addDayOntoEndOfCarousel(Day day) {
		App.tagdayView.getCarousel().addDayOntoEnd(day);
	}
	
	@Override
	public void addDayOntoBeginningOfCarousel(Day day) {
		App.tagdayView.getCarousel().addDayOntoBeginning(day);
	}
	

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		log.info("Carousel stopped at: " + event.getSelectedItem() + "\nCarpusel=" + App.tagdayView.getCarousel().toString());
		updateNavigation();
	}

	@Override
	public void onDayEarlier() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addDaysToDate(d, -1);
		log.info("1 day earlier: going to date: " + d + "...");
		goToDate(d);
	}

	@Override
	public void onDayLater() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addDaysToDate(d, 1);
		log.info("1 day later: going to date: " + d + "...");
		goToDate(d);
	}

	@Override
	public void onMonthEarlier() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addMonthsToDate(d, -1);
		goToDate(d);
	}

	@Override
	public void onMonthLater() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addMonthsToDate(d, 1);
		goToDate(d);
	}

	@Override
	public void onYearEarlier() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addMonthsToDate(d, -12);
		goToDate(d);
	}

	@Override
	public void onYearLater() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addMonthsToDate(d, 12);
		goToDate(d);
	}
}