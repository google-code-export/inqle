package com.beyobe.client.views;

import java.util.Date;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.event.EditQuestionEvent;
import com.beyobe.client.widgets.Day;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeEndEvent;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeEndHandler;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeEvent.DIRECTION;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.ui.client.animation.AnimationHelper;
import com.googlecode.mgwt.ui.client.widget.touch.TouchDelegate;

public class TagdayViewImpl extends Composite implements TagdayView, SwipeEndHandler {
	private static Logger log = Logger.getLogger("TagdayViewImpl");
	
        private static TagdayViewImplUiBinder uiBinder = GWT
                        .create(TagdayViewImplUiBinder.class);

        interface TagdayViewImplUiBinder extends UiBinder<Widget, TagdayViewImpl> {
        }

        @UiField HorizontalPanel menuPanel;
//        @UiField Button addTagButton;
        @UiField Image addTagIcon;
        @UiField FlowPanel daysPanel;
        
//        @UiField FlowPanel dayPicker;
        private Presenter presenter;
//        private List<Day> days = new ArrayList<Day>();
		private Date date;
		@UiField Label dateLabel;
		@UiField Label monthLabel;
		@UiField Label yearLabel;
        
		@UiField Image dayEarlierIcon;
		@UiField Image dayLaterIcon;
		@UiField Image monthEarlierIcon;
		@UiField Image monthLaterIcon;
		@UiField Image yearEarlierIcon;
		@UiField Image yearLaterIcon;
		
		@UiField Image todayIcon;
		
		private AnimationHelper animater = new AnimationHelper();

		private Day day;
		
        public TagdayViewImpl() {
    		this.date = new Date();
            initWidget(uiBinder.createAndBindUi(this));
            
            TouchDelegate addTagTd = new TouchDelegate(addTagIcon);
            addTagTd.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent e) {
					onAddTag(e);
				}
			});
            addTagIcon.addStyleName("ttd-addTagIcon");
            
            dateLabel.addStyleName("ttd-dayNavLabel");
            monthLabel.addStyleName("ttd-monthNavLabel");
            yearLabel.addStyleName("ttd-yearNavLabel");
            
            TouchDelegate dayEarlierTd = new TouchDelegate(dayEarlierIcon);
            dayEarlierTd.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent e) {
					onDayEarlier(e);
				}
			});
            dayEarlierIcon.addStyleName("ttd-navEarlierIcon");
            
            TouchDelegate dayLaterTd = new TouchDelegate(dayLaterIcon);
            dayLaterTd.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent e) {
					onDayLater(e);
				}
			});
            dayLaterIcon.addStyleName("ttd-navLaterIcon");
            
            TouchDelegate monthEarlierTd = new TouchDelegate(monthEarlierIcon);
            monthEarlierTd.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent e) {
					onMonthEarlier(e);
				}
			});
            monthEarlierIcon.addStyleName("ttd-navEarlierIcon");
            
            TouchDelegate monthLaterTd = new TouchDelegate(monthLaterIcon);
            monthLaterTd.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent e) {
					onMonthLater(e);
				}
			});
            monthLaterIcon.addStyleName("ttd-navLaterIcon");
            
            TouchDelegate yearEarlierTd = new TouchDelegate(yearEarlierIcon);
            yearEarlierTd.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent e) {
					onYearEarlier(e);
				}
			});
            yearEarlierIcon.addStyleName("ttd-navEarlierIcon");
            
            TouchDelegate yearLaterTd = new TouchDelegate(yearLaterIcon);
            yearLaterTd.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent e) {
					onYearLater(e);
				}
			});           
            yearLaterIcon.addStyleName("ttd-navLaterIcon");
            
            TouchDelegate todayTd = new TouchDelegate(todayIcon);
            todayTd.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent e) {
					onNavToToday(e);
				}
			});
            todayIcon.addStyleName("ttd-todayIcon");
            
//            daysPanel.setHeight("100%");
            daysPanel.setWidth("100%");
            
            daysPanel.setHeight(Window.getClientHeight() + "px");
            
            TouchDelegate touchDelegate = new TouchDelegate(daysPanel);
            touchDelegate.addSwipeEndHandler(this);
            
            Window.addResizeHandler(new ResizeHandler() {
             public void onResize(ResizeEvent event) {
               int height = event.getHeight();
               daysPanel.setHeight(height + "px");
             }
            });
            
            daysPanel.add(animater);
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

//		@UiHandler("dayEarlierButton")
		void onDayEarlier(TapEvent e) {
			presenter.onDayEarlier();
		}
//		@UiHandler("dayLaterButton")
		void onDayLater(TapEvent e) {
			presenter.onDayLater();
		}
		
		private void onNavToToday(TapEvent e) {
			presenter.onNavToToday();
		}
		
//		@Override
//		public void setEarlierDay(Day day) {
//			if (day == null) throw new RuntimeException("Cannot set a null day");
////			daysPanel.clear();
////			daysPanel.add(day);
//			animater.goTo(day, Animation.SLIDE_REVERSE);
//		}
//		
//		@Override
//		public void setLaterDay(Day day) {
//			if (day == null) throw new RuntimeException("Cannot set a null day");
////			daysPanel.clear();
////			daysPanel.add(day);
//			animater.goTo(day, Animation.SLIDE);
//		}
		
		@Override
		public void setDay(Day day, Animation animation) {
			if (day == null) throw new RuntimeException("Cannot set a null day");
//			daysPanel.clear();
//			daysPanel.add(day);
			//TODO add interim "2014" or "September" page for year or month changes respectively
			animater.goTo(day, animation);
		}
		
//		@UiHandler("monthEarlierButton")
		void onMonthEarlier(TapEvent e) {
			presenter.onMonthEarlier();
		}
//		@UiHandler("monthLaterButton")
		void onMonthLater(TapEvent e) {
			presenter.onMonthLater();
		}
		
//		@UiHandler("yearEarlierButton")
		void onYearEarlier(TapEvent e) {
			presenter.onYearEarlier();
		}
//		@UiHandler("yearLaterButton")
		void onYearLater(TapEvent e) {
			presenter.onYearLater();
		}
		
		@Override
		public void onSwipeEnd(SwipeEndEvent e) {
			if (e.getDirection() == DIRECTION.LEFT_TO_RIGHT) {
				presenter.onDayEarlier();
			}
			else if (e.getDirection() == DIRECTION.RIGHT_TO_LEFT) {
				presenter.onDayLater();
			}
		}

		void onAddTag(TapEvent e) {
			App.eventBus.fireEvent(new EditQuestionEvent(null));
		}

//		@Override
//		public void removeTagButton(TagButton tagButton) {
//			Day day = presenter.getCurrentDay();
//			day.removeTagButton(tagButton);
//		}

		
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