package com.beyobe.client.views;

import java.util.Date;

import com.beyobe.client.widgets.Carousel;
import com.beyobe.client.widgets.Day;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;

public interface CarouselTagdayView extends IsWidget {
    void setPresenter(Presenter presenter);

    public interface Presenter extends SelectionHandler<Integer> {
        void goTo(Place place);

		void onAddTag(TapEvent e);

		void onAttach();
		
		public void updateNavigation();
		
		public void goToDate(Date date);
		
		public Day getCurrentDay();
		
		public void addDayOntoEndOfCarousel(Day day);

		void onDayEarlier();

		void onDayLater();

		void onMonthEarlier();

		void onMonthLater();

		void onYearEarlier();

		void onYearLater();

		void addDayOntoBeginningOfCarousel(Day day);
    }

	HasText getDateLabel();

	HasText getMonthLabel();

	HasText getYearLabel();

	Carousel getCarousel();

//	void setQuestionOptions(List<Question> questions);
}