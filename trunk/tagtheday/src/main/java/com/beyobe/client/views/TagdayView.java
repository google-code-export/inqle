package com.beyobe.client.views;

import java.util.Date;
import java.util.List;

import com.beyobe.client.beans.Question;
import com.beyobe.client.widgets.Day;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface TagdayView extends IsWidget {
    void setPresenter(Presenter presenter);

    public interface Presenter {
        void goTo(Place place);
    }

	void addDay(Day todayDay);

	void updateNavigation();

	void scrollToDay(Date date);

//	void setQuestionOptions(List<Question> questions);
}