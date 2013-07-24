package com.beyobe.client.activities;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.views.TagdayView;
import com.beyobe.client.widgets.Day;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

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
    	App.loadData();
        TagdayView tagdayView = App.tagdayView;
        tagdayView.setPresenter(this);
        loadDays(tagdayView);
        containerWidget.setWidget(tagdayView.asWidget());
    }

    //TODO load tags dynamically
    private void loadDays(TagdayView tagdayView) {
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
    		tagdayView.addDay(day);
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
}