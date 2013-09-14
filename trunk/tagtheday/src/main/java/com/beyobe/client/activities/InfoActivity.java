package com.beyobe.client.activities;

import com.beyobe.client.App;
import com.beyobe.client.beans.InfoStatus;
import com.beyobe.client.views.InfoView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class InfoActivity extends AbstractActivity implements InfoView.Presenter {
    private InfoPlace place;

    public InfoActivity(InfoPlace place) {
        this.place = place;
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        InfoView infoView = App.infoView;
        infoView.setPresenter(this);
        containerWidget.setWidget(infoView.asWidget());
        infoView.setInfoStatusTab(place.getInfoStatus());
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
    
    public InfoStatus getInfoStatus() {
    	return place.getInfoStatus();
    }
}