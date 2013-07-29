package com.beyobe.client.views;

import java.util.Date;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.event.EditQuestionEvent;
import com.beyobe.client.widgets.Carousel;
import com.beyobe.client.widgets.Day;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.Button;

public class TagdayViewImpl extends Composite implements TagdayView {
	private static Logger log = Logger.getLogger("TagdayViewImpl");
	
        private static TagdayViewImplUiBinder uiBinder = GWT
                        .create(TagdayViewImplUiBinder.class);

        interface TagdayViewImplUiBinder extends UiBinder<Widget, TagdayViewImpl> {
        }

        @UiField HorizontalPanel menuPanel;
        @UiField Button addTagButton;
        @UiField FlowPanel daysPanel;
        
//        @UiField FlowPanel dayPicker;
        private Presenter presenter;
//        private List<Day> days = new ArrayList<Day>();
		private Date date;
		private Carousel carousel;
		@UiField Label dateLabel;
		@UiField Label monthLabel;
		@UiField Label yearLabel;
        
		@UiField Button dayEarlierButton;
		@UiField Button dayLaterButton;
		@UiField Button monthEarlierButton;
		@UiField Button monthLaterButton;
		@UiField Button yearEarlierButton;
		@UiField Button yearLaterButton;
		
        public TagdayViewImpl() {
    		this.date = new Date();
            initWidget(uiBinder.createAndBindUi(this));
            
            addTagButton.addStyleName("ttd-addTagButton");
            
            dateLabel.addStyleName("ttd-dayNavLabel");
            monthLabel.addStyleName("ttd-monthNavLabel");
            yearLabel.addStyleName("ttd-yearNavLabel");
            
            dayEarlierButton.addStyleName("ttd-navEarlierButton");
            monthEarlierButton.addStyleName("ttd-navEarlierButton");
            yearEarlierButton.addStyleName("ttd-navEarlierButton");
            
            dayLaterButton.addStyleName("ttd-navLaterButton");
            monthLaterButton.addStyleName("ttd-navLaterButton");
            yearLaterButton.addStyleName("ttd-navLaterButton");
            
            carousel = new Carousel();
            carousel.setHeight("100%");
            carousel.setWidth("100%");
            
            daysPanel.setHeight("100%");
            daysPanel.setWidth("100%");
            
            daysPanel.setHeight(Window.getClientHeight() + "px");
            Window.addResizeHandler(new ResizeHandler() {
             public void onResize(ResizeEvent event) {
               int height = event.getHeight();
               daysPanel.setHeight(height + "px");
             }
            });
        }

		@Override
        public void setPresenter(Presenter presenter) {
            this.presenter = presenter;
        }

//		@Override
//		public void onAttach() {
//			super.onAttach();
//			presenter.onAttach();
//		}

		@Override
		public HasText getDateLabel() {
			return dateLabel;
		}

		@Override
		public HasText getMonthLabel() {
			return monthLabel;
		}

		@Override
		public HasText getYearLabel() {
			return yearLabel;
		}

		@UiHandler("dayEarlierButton")
		void onDayEarlier(TapEvent e) {
			presenter.onDayEarlier();
		}
		@UiHandler("dayLaterButton")
		void onDayLater(TapEvent e) {
			presenter.onDayLater();
		}
		
		@UiHandler("addTagButton")
		void onAddTag(TapEvent e) {
			App.eventBus.fireEvent(new EditQuestionEvent(null));
		}

		@Override
		public void setDay(Day day) {
			if (day == null) throw new RuntimeException("Cannot set a null day");
			daysPanel.clear();
			daysPanel.add(day);
		}
		
		@UiHandler("monthEarlierButton")
		void onMonthEarlier(TapEvent e) {
			presenter.onMonthEarlier();
		}
		@UiHandler("monthLaterButton")
		void onMonthLater(TapEvent e) {
			presenter.onMonthLater();
		}
		
		@UiHandler("yearEarlierButton")
		void onYearEarlier(TapEvent e) {
			presenter.onYearEarlier();
		}
		@UiHandler("yearLaterButton")
		void onYearLater(TapEvent e) {
			presenter.onYearLater();
		}

		
//		@Override
//		public void updateNavigation() {
//			Day day = getCurrentDay();
//			if (day==null) return;
//			log.info("updateNavigation to day: " + day);
//			dateLabel.setText(day.getDateText());
//			monthLabel.setText(day.getMonthText());
//			yearLabel.setText(day.getYearText());
//		}
//
//		@Override
//		public void scrollToDay(Date date) {
//			Iterator carouselI = carousel.iterator();
//			int i=0;
//			while(carouselI.hasNext()) {
//				Object dayObj = carouselI.next();
//				Day day = (Day) dayObj;
//				if (day.getEnd().after(date) && (day.getStart().before(date) || day.getStart().equals(date))) {
//					carousel.setSelectedPage(i);
//					break;
//				}
//				i++;
//			}
//		}
		
		
}