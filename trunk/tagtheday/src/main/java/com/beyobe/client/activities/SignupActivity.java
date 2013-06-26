package com.beyobe.client.activities;

import com.beyobe.client.App;
import com.beyobe.client.views.SignupView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class SignupActivity extends AbstractActivity implements SignupView.Presenter {
    private Place defaultPlace;

    public SignupActivity(Place defaultPlace) {
        this.defaultPlace = defaultPlace;
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        SignupView signupView = App.signupView;
        signupView.setPresenter(this);
        containerWidget.setWidget(signupView.asWidget());
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