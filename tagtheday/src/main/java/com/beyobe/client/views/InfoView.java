package com.beyobe.client.views;

import com.beyobe.client.beans.InfoStatus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface InfoView extends IsWidget {
    void setPresenter(Presenter presenter);

    public interface Presenter {
        void goTo(Place place);
        
        InfoStatus getInfoStatus();
    }
}