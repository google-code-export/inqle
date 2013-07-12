package com.beyobe.client.activities;

import com.beyobe.client.App;
import com.beyobe.client.views.QuestionView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class QuestionActivity extends AbstractActivity implements QuestionView.Presenter {
    private Place defaultPlace;

    public QuestionActivity(Place defaultPlace) {
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
        QuestionView questionView = App.questionView;
        questionView.setPresenter(this);
        containerWidget.setWidget(questionView.asWidget());
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