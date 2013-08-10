package com.beyobe.client.activities;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.event.EditQuestionEvent;
import com.beyobe.client.views.TagdayView;
import com.beyobe.client.widgets.Day;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.mvp.client.Animation;

/**
 * TODO:
 * Load data in time windows
 * option to auto-answer intervening days when 2 isolated days have identical value
 * unit test somewhere!
 * server-side safeguard - prevent too many insertions, invalidate sessions
 * fix login slowness problems
 * integrate typicalsecurity
 *  * add back email validation regexp
 *  * CAPTCHA
 *  * email confirmation
 * @author donohue
 *
 */
public class TagdayActivity extends AbstractActivity implements TagdayView.Presenter {
    private Place defaultPlace;

	private Day currentDay;

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
//        loadDays();
        containerWidget.setWidget(App.tagdayView.asWidget());
        goToDate(new Date());
        
//        currentDay = App.dataBus.loadDay(new Date());
//        App.tagdayView.setDay(currentDay);
    }

//    private void loadDays() {
//    	
//    	Map<String, Day> days = App.dataBus.getAllDays();
//    	if (days == null) {
//    		days = App.dataBus.createAllDays();
//    	}
////    	for (Day day: days) {
////    		addDayOntoEnd(day);
////    		log.info("Added day: " + day);
////    	}
//	}

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

//	@Override
//	public void onAttach() {
//		goToDate(new Date());
//   	 	updateNavigation();
//	}
	
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

	private void goToDate(Date d) {
		if (d == null) return;
		Day day = App.dataBus.loadDay(d, false);
		currentDay = day;
		Date now = new Date();
		if (day.getEnd().after(now) && day.getStart().before(now)) {
			day.addStyleName("ttd-day-today");
		} else if (day.getStart().after(now)) {
			day.addStyleName("ttd-day-future");
		} else {
			day.addStyleName("ttd-day-past");
		}
		App.tagdayView.setDay(day, null);
		updateNavigation();
	}
	
//	@Override
//	public void goToEarlierDate(Date d) {
//		if (d == null) return;
//		Day day = App.dataBus.loadDay(d);
//		currentDay = day;
//		Date now = new Date();
//		if (day.getEnd().after(now) && day.getStart().before(now)) {
//			day.addStyleName("ttd-day-today");
//		} else if (day.getStart().after(now)) {
//			day.addStyleName("ttd-day-future");
//		} else {
//			day.addStyleName("ttd-day-past");
//		}
//		App.tagdayView.setDay(day, Animation.SLIDE_REVERSE);
//		updateNavigation();
//	}
	
	@Override
	public void goToDate(Date d, Animation animation, boolean navigatingToPast) {
		if (d == null) return;
		Day day = App.dataBus.loadDay(d, navigatingToPast);
		currentDay = day;
		Date now = new Date();
		if (day.getEnd().after(now) && day.getStart().before(now)) {
			day.addStyleName("ttd-day-today");
		} else if (day.getStart().after(now)) {
			day.addStyleName("ttd-day-future");
		} else {
			day.addStyleName("ttd-day-past");
		}
		App.tagdayView.setDay(day, animation);
		updateNavigation();
	}
	
	@Override
	public Day getCurrentDay() {
	 	return currentDay;
	}
	
	@Override
	public void onDayEarlier() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addDaysToDate(d, -1);
		goToDate(d, Animation.SLIDE_REVERSE, true);
	}

	@Override
	public void onDayLater() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addDaysToDate(d, 1);
		goToDate(d, Animation.SLIDE, false);
	}

	@Override
	public void onMonthEarlier() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addMonthsToDate(d, -1);
		goToDate(d, Animation.SLIDE_REVERSE, true);
	}

	@Override
	public void onMonthLater() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addMonthsToDate(d, 1);
		goToDate(d, Animation.SLIDE, false);
	}

	@Override
	public void onYearEarlier() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addMonthsToDate(d, -12);
		goToDate(d, Animation.SLIDE_REVERSE, true);
	}

	@Override
	public void onYearLater() {
		Date d = getCurrentDay().getStart();
		CalendarUtil.addMonthsToDate(d, 12);
		goToDate(d, Animation.SLIDE, false);
	}

	@Override
	public void onNavToToday() {
		Date d = getCurrentDay().getStart();
		Date now = new Date();
		if (d.after(now)) {
			goToDate(now, Animation.SLIDE, false);
		} else {
			goToDate(now, Animation.SLIDE_REVERSE, true);
		}
	}
}