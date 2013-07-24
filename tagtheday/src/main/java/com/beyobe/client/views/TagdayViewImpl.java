package com.beyobe.client.views;

import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.event.EditQuestionEvent;
import com.beyobe.client.widgets.Carousel;
import com.beyobe.client.widgets.Day;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
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

        @UiField FlowPanel menuPanel;
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
                
                addTagButton.addStyleName("ttd-controlButton");
                addTagButton.addStyleName("ttd-addTagButton");
//                Button addTagButton = new Button("+");
//                addTagButton.addStyleName("ttd-button");
//                addTagButton.setSmall(true);
//                addTagButton.addTouchEndHandler(new TouchEndHandler() {
//					@Override
//					public void onTouchEnd(TouchEndEvent event) {
//						App.eventBus.fireEvent(new EditQuestionEvent(null));
//					}
//                });
//                addTagButton.getElement().getStyle().setProperty("float", "right");
//                menuPanel.add(addTagButton);
                
//                Button dateDownButton = new Button("<");
//                dateDownButton.addStyleName("ttd-button");
//                menuPanel.add();
//                loadDayPicker();
                
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
                
//                daysPanel.setHeight(Window.getClientHeight() + "px");
//                Window.addResizeHandler(new ResizeHandler() {
//                 public void onResize(ResizeEvent event) {
//                   int height = event.getHeight();
//                   daysPanel.setHeight(height + "px");
//                 }
//                });
                
                daysPanel.add(carousel);
        }

//        @UiHandler("goodbyeLink")
//        void onClickGoodbye(ClickEvent e) {
//                presenter.goTo(new GoodbyePlace(name));
//        }

        public Day getCurrentDay() {
        	Widget w = carousel.getCurrentWidget();
//        	log.info("getCurrentDay() widget = " + w.getClass());
        	if (w != null && w instanceof Day) {
        		return (Day)w;
        	}
        	return null;
//        	return carousel.getCurrentDay();
		}

//		private void loadDayPicker() {
//        	WeekView weekView = new WeekView();
//        	dayPicker.add(weekView);
//		}

		@Override
        public void setPresenter(Presenter presenter) {
                this.presenter = presenter;
        }

		@Override
		public void addDay(Day day) {
			carousel.addWidget(day);
		}

		@UiHandler("addTagButton")
		void onAddTag(TapEvent e) {
			App.question = null;
			App.eventBus.fireEvent(new EditQuestionEvent(null));
		}
//		@Override
//		public void setQuestionOptions(List<Question> questions) {
//			
//			
//		}

		@Override
		public void onAttach() {
			super.onAttach();
			scrollToDay(new Date());
	   	 	updateNavigation();
		}
		
		@Override
		public void updateNavigation() {
			Day day = getCurrentDay();
			if (day==null) return;
			log.info("updateNavigation to day: " + day);
			dateLabel.setText(day.getDateText());
			monthLabel.setText(day.getMonthText());
			yearLabel.setText(day.getYearText());
		}

		@Override
		public void scrollToDay(Date date) {
			Iterator carouselI = carousel.iterator();
			int i=0;
			while(carouselI.hasNext()) {
				Object dayObj = carouselI.next();
				Day day = (Day) dayObj;
				if (day.getEnd().after(date) && (day.getStart().before(date) || day.getStart().equals(date))) {
					carousel.setSelectedPage(i);
					break;
				}
				i++;
			}
			
		}
		
		
}