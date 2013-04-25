package com.beyobe.client.views;

import java.util.Date;

import com.beyobe.client.App;
import com.beyobe.client.event.NewTagEvent;
import com.beyobe.client.widgets.Carousel;
import com.beyobe.client.widgets.Day;
import com.beyobe.client.widgets.WeekView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.ui.client.widget.Button;


public class TagdayViewImpl extends Composite implements TagdayView {
        private static TagdayViewImplUiBinder uiBinder = GWT
                        .create(TagdayViewImplUiBinder.class);

        interface TagdayViewImplUiBinder extends UiBinder<Widget, TagdayViewImpl> {
        }

        @UiField FlowPanel tagsPanel;
        @UiField FlowPanel daysPanel;
        @UiField FlowPanel dayPicker;
        private Presenter presenter;
//        private List<Day> days = new ArrayList<Day>();
		private Date date;
		private Carousel carousel;
        
        public TagdayViewImpl() {
        		this.date = new Date();
                initWidget(uiBinder.createAndBindUi(this));
                Button addTagButton = new Button("+");
//                addTagButton.addStyleName("mgwt-Button-small");
//                addTagButton.addStyleName("mgwt-Button-round");
                addTagButton.setSmall(true);
                addTagButton.setRound(true);
                addTagButton.addTouchEndHandler(new TouchEndHandler() {
					@Override
					public void onTouchEnd(TouchEndEvent event) {
						App.eventBus.fireEvent(new NewTagEvent());
					}
                });
                tagsPanel.add(addTagButton);
                loadDayPicker();
                carousel = new Carousel();
                carousel.setHeight("100%");
                carousel.setWidth("100%");
                daysPanel.setHeight("100%");
                daysPanel.setWidth("100%");
                daysPanel.add(carousel);
        }

//        @UiHandler("goodbyeLink")
//        void onClickGoodbye(ClickEvent e) {
//                presenter.goTo(new GoodbyePlace(name));
//        }

        private void loadDayPicker() {
        	WeekView weekView = new WeekView();
        	dayPicker.add(weekView);
		}

		@Override
        public void setPresenter(Presenter presenter) {
                this.presenter = presenter;
        }

		@Override
		public void addDay(Day day) {
			carousel   .add(day);
		}
}