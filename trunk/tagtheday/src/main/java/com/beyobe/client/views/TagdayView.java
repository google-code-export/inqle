package com.beyobe.client.views;

import java.util.Date;

import com.beyobe.client.widgets.Day;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.mvp.client.Animation;

public interface TagdayView extends IsWidget {
    void setPresenter(Presenter presenter);

    public interface Presenter {
        void goTo(Place place);

		void onAddTag(TapEvent e);

//		void onAttach();
		
		public void updateNavigation();
		
//		public void goToDate(Date date, Animation animation);
		
//		public void goToLaterDate(Date date);
		
		public Day getCurrentDay();
		
//		public void addDayOntoEndOfCarousel(Day day);

		void onDayEarlier();

		void onDayLater();

		void onMonthEarlier();

		void onMonthLater();

		void onYearEarlier();

		void onYearLater();

		void goToDate(Date d, Animation animation, boolean navigatingToPast);

		void onNavToToday();

//		void addDayOntoBeginningOfCarousel(Day day);
    }

	HasText getDateLabel();

	HasText getMonthLabel();

	HasText getYearLabel();

	void setDay(Day day, Animation animation);

//	void removeTagButton(TagButton tagButton);
	
//	Carousel getCarousel();

//	void setQuestionOptions(List<Question> questions);
}