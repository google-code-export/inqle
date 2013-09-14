package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.beans.AnswerStatus;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.beyobe.client.event.DataCapturedEvent;
import com.beyobe.client.event.EditQuestionEvent;
import com.beyobe.client.event.TagClickedEvent;
import com.beyobe.client.icons.Icons;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.RoundPanel;
import com.googlecode.mgwt.ui.client.widget.touch.TouchDelegate;

public class Day extends Composite implements Block, TapHandler {
	private static final String DAY_LABEL_FORMAT = "EEEE, MMMM d, y";
	public static final int MILLISECONDS_IN_A_DAY = 86400000;
	protected Date start;
	protected Date end;
	protected Date created;
//	protected VerticalPanel panel;
	protected RoundPanel roundPanel;
	protected Label dateLabel;
	private Date midpoint;
	private Date timepoint;
	private List<TagButton> tagButtons = new ArrayList<TagButton>();
	
	Logger log = Logger.getLogger("Day");
	
	public Day(Date point) {
		this.timepoint = point;
		this.created = new Date();
//		panel = new VerticalPanel();
//		panel.setWidth("90%");
//		panel.setHeight((Window.getClientHeight() - 120) + "px");
//		panel.addHandler(new ResizeHandler() {
//			  public void onResize(ResizeEvent event) {
//				  panel.setHeight((Window.getClientHeight() - 120) + "px");
//			  }
//		}, ResizeEvent.getType() );
		
		HorizontalPanel topRow = new HorizontalPanel();
		topRow.setWidth("100%");
		dateLabel = new Label(getLabelText());
		dateLabel.addStyleName("ttd-fullDateLabel");
		topRow.add(dateLabel);
//		ScrollPanel scrollPanel = new ScrollPanel();
		roundPanel = new RoundPanel();
//		tagsPanel.setWidth("90%");
		roundPanel.setHeight((Window.getClientHeight() - 100) + "px");
//		tagsPanel.setWidth(Window.getClientWidth()-50 + "px");
//		tagsPanel.addTapHandler(this);
		
//		roundPanel.addHandler(new ResizeHandler() {
//			  public void onResize(ResizeEvent event) {
//				  Window.alert("Client is " + Window.getClientWidth() + " by " + Window.getClientHeight());
//				  roundPanel.setHeight((Window.getClientHeight() - 100) + "px");
////				  tagsPanel.setWidth(Window.getClientWidth()-50 + "px");
//			  }
//		}, ResizeEvent.getType() );
		
		
		
		Icons icons = GWT.create(Icons.class);
		Image addTagIcon = new Image(icons.add24());
		TouchDelegate addTagTd = new TouchDelegate(addTagIcon);
		addTagTd.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent e) {
				App.eventBus.fireEvent(new EditQuestionEvent(null));
			}
		});
		addTagIcon.addStyleName("ttd-addTagDayIcon");
		topRow.add(addTagIcon);
		
		roundPanel.add(topRow);
		
		start = new Date(point.getYear(), point.getMonth(), point.getDate());
		long startMS = start.getTime();
		end = new Date(startMS + MILLISECONDS_IN_A_DAY - 1);
		
//		addDummyTB("Day constructor");
//		scrollPanel.add(roundPanel);
//		initWidget(scrollPanel);
		initWidget(roundPanel);
	}

//	public void addDummyTB(String label) {
//		Question q = BeanMaker.makeQuestion();
//		q.setId(UUID.uuid());
//		q.setCreated(new Date());
//		q.setAbbreviation(label);
//		q.setLongForm(label + "?");
//		TagButton tb = new TagButton(new Date(), q, null);
//		tagsPanel.add(tb);
//	}
	
	@Override
	public String toString() {
		return "Day [start=" + start + ", midpoint=" + midpoint
				+ ", timepoint=" + timepoint + ", end=" + end + ", created="
				+ created + ", tagButtons=" + tagButtons + "]";
	}

	@Override
	public String getLabelText() {
		DateTimeFormat formatter = DateTimeFormat.getFormat(DAY_LABEL_FORMAT);
		return formatter.format(getTimepoint());
	}

	public String getDateText() {
		DateTimeFormat formatter = DateTimeFormat.getFormat("d");
		return formatter.format(getTimepoint());
	}
	
	public String getMonthText() {
		DateTimeFormat formatter = DateTimeFormat.getFormat("MMM");
		return formatter.format(getTimepoint());
	}
	
	public String getYearText() {
		DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy");
		return formatter.format(getTimepoint());
	}
	
	@Override
	public Date getMidpoint() {
		return new Date(midpoint.getTime());
	}

	@Override
	public Date getTimepoint() {
		return new Date(timepoint.getTime());
	}

	@Override
	public Date getStart() {
		return new Date(start.getTime());
	}

	@Override
	public Date getEnd() {
		return new Date(end.getTime());
	}

	@Override
	public List<TagButton> getTagButtons() {
		return tagButtons;
	}

	@Override
	public void addTagButton(TagButton tagButton) {
//		addDummyTB("addTagButton");
//		addTestMessage("1. Why did this not work??");
		tagButtons.add(tagButton);
		tagButton.addTapHandler(this);
		tagButton.getElement().getStyle().setProperty("float", "left");
		roundPanel.add(tagButton);
//		addTestMessage("2. Why did this not work??");
		log.info("Added tagButton: " + tagButton + " to day: " + getLabelText() + "; Current tagButtons: " + tagButtons);
	}
	
	public void addTestMessage(String msg) {
		roundPanel.add(new HTML(msg));
	}

//	@Override
//	public void onTouchStart(TouchStartEvent event) {
//		// Do nothing		
//	}
//
//	@Override
//	public void onTouchMove(TouchMoveEvent event) {
//		// Do nothing		
//	}
//
//	@Override
//	public void onTouchEnd(TouchEndEvent event) {
//		if (event.getSource() instanceof TagButton) {
//			App.eventBus.fireEvent(new TagClickedEvent((TagButton)event.getSource()));
//		} else if (event.getSource() instanceof Day){
//			App.eventBus.fireEvent(new NewTagEvent((Day)event.getSource()));
//		}
//	}
//
//	@Override
//	public void onTouchCanceled(TouchCancelEvent event) {
//		// Do nothing	
//	}

	@Override
	public void onTap(TapEvent event) {
		
		if (event.getSource() instanceof TagButton) {
			TagButton tagButton = (TagButton)event.getSource();
			Datum d = tagButton.getDatum();
			if (d!=null && d.getAnswerStatus()==AnswerStatus.INFERRED) {
				d.setAnswerStatus(AnswerStatus.ANSWERED_PERSONALLY);
				tagButton.refreshAppearance();
				App.eventBus.fireEvent(new DataCapturedEvent(tagButton));
				return;
			}
			App.eventBus.fireEvent(new TagClickedEvent(tagButton));
		} 
//		else if (event.getSource().equals(tagsPanel)){
//			App.eventBus.fireEvent(new EditQuestionEvent(null));
//		}
	}

	public void addQuestion(Question question) {
		log.fine("Adding question: " + question.getAbbreviation() + " to Day: " + getLabelText());
		if (question==null) return;
		boolean foundQuestion = false;
		for (TagButton tagButton: tagButtons) {
			if(question.getId().equals(tagButton.getQuestion().getId())) {
				foundQuestion = true;
				tagButton.setQuestion(question);
				tagButton.refreshAppearance();
				log.info("Updated question, tagButton=" + tagButton);
			}
		}
		if (! foundQuestion) {
			TagButton tagButton = new TagButton(this.timepoint, question, null);
			addTagButton(tagButton);
			log.info("Added new question, tagButton=" + tagButton);
		}
		
	}

}
