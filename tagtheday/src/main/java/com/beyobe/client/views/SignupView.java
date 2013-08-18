package com.beyobe.client.views;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface SignupView extends IsWidget {
    void setPresenter(Presenter presenter);

    public interface Presenter {
        void goTo(Place place);
    }

	void setMessage(String string);

	void displayMessage(String msg);
}