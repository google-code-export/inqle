package com.beyobe.client.activities;

import java.util.Date;

import com.beyobe.client.App;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.beyobe.client.beans.Unit;
import com.beyobe.client.views.TagdayView;
import com.beyobe.client.widgets.Day;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class TagdayActivity extends AbstractActivity implements TagdayView.Presenter {
    private Place defaultPlace;

    public TagdayActivity(Place defaultPlace) {
        this.defaultPlace = defaultPlace;
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        TagdayView tagdayView = App.tagdayView;
        tagdayView.setPresenter(this);
        loadDays(tagdayView);
        containerWidget.setWidget(tagdayView.asWidget());
    }

    //TODO load tags dynamically
    private void loadDays(TagdayView tagdayView) {
    	Date date = new Date();
    	Day todayDay = new Day(date);
    	
        Question q1 = new Question();
        q1.setId(1L);
        q1.setLongForm("What is your weight?");
        q1.setShortForm("Weight");
        q1.setAbbreviation("Wt");
        q1.setDataType(Question.DATA_TYPE_DOUBLE);
        q1.setReferenceUnit(Unit.KG);
        Question q2 = new Question();
        q2.setId(2L);
        q2.setLongForm("How happy are you?");
        q2.setShortForm("Happy");
        q2.setAbbreviation("Happy");
        Datum d2 = new Datum();
        d2.setQuestionId(q2.getId());
        d2.setNumericValue(4.0);
        d2.setTextValue("4");
        TagButton tb1 = new TagButton(date, q1, null);
        TagButton tb2 = new TagButton(date, q2, d2);
        todayDay.addTagButton(tb1);
        todayDay.addTagButton(tb2);
    	
		tagdayView.addDay(todayDay);
		
		CalendarUtil.addDaysToDate(date, 1);
    	Day tomorrowDay = new Day(date);
    	tagdayView.addDay(tomorrowDay);
	}

	/**
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        return "Please hold on. This activity is stopping.";
    }

    /**
     * Navigate to a new Place in the browser
     */
    public void goTo(Place place) {
        App.placeController.goTo(place);
    }
}