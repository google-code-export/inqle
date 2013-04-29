package com.beyobe.client.views;

import com.beyobe.client.widgets.Day;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface TagdayView extends IsWidget {
    void setPresenter(Presenter presenter);

    public interface Presenter {
        void goTo(Place place);
    }

	void addDay(Day todayDay);
}