package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.beans.Question;
import com.beyobe.client.event.EditQuestionEvent;
import com.beyobe.client.event.TagClickedEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.RoundPanel;

public class Day extends Composite implements Block, TapHandler {
	private static final String DAY_LABEL_FORMAT = "EEEE, MMMM d, y";
	public static final int MILLISECONDS_IN_A_DAY = 86400000;
	protected Date start;
	protected Date end;
	protected Date created;
//	protected VerticalPanel panel;
	protected RoundPanel tagsPanel;
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
		dateLabel = new Label(getLabelText());
		tagsPanel = new RoundPanel();
		tagsPanel.setWidth("90%");
		tagsPanel.setHeight((Window.getClientHeight() - 100) + "px");
		tagsPanel.addTapHandler(this);
		tagsPanel.addHandler(new ResizeHandler() {
			  public void onResize(ResizeEvent event) {
				  tagsPanel.setHeight((Window.getClientHeight() - 100) + "px");
			  }
		}, ResizeEvent.getType() );
		
		tagsPanel.add(dateLabel);
//		panel.add(tagsPanel);
		
		start = new Date(point.getYear(), point.getMonth(), point.getDate());
		long startMS = start.getTime();
		end = new Date(startMS + MILLISECONDS_IN_A_DAY - 1);
		
		initWidget(tagsPanel);
	}

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

	@Override
	public Date getMidpoint() {
		return midpoint;
	}

	@Override
	public Date getTimepoint() {
		return timepoint;
	}

	@Override
	public Date getStart() {
		return start;
	}

	@Override
	public Date getEnd() {
		return end;
	}

	@Override
	public List<TagButton> getTagButtons() {
		return tagButtons;
	}

	@Override
	public void addTagButton(TagButton tagButton) {
		tagButtons.add(tagButton);
		tagButton.addTapHandler(this);
		tagButton.getElement().getStyle().setProperty("float", "left");
		tagsPanel.add(tagButton);
		tagsPanel.add(new HTML("Why did this not work??"));
		log.info("Added tagButton: " + tagButton + " to day: " + getLabelText() + "; Current tagButtons: " + tagButtons);
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
			App.eventBus.fireEvent(new TagClickedEvent((TagButton)event.getSource()));
		} else if (event.getSource().equals(tagsPanel)){
			App.eventBus.fireEvent(new EditQuestionEvent(null));
		}
	}

	public void addQuestion(Question question) {
		log.fine("Adding question: " + question.getAbbreviation() + " to Day: " + getLabelText());
		if (question==null) return;
		boolean foundQuestion = false;
		for (TagButton tagButton: tagButtons) {
			if(question.getId().equals(tagButton.getQuestion().getId())) {
				foundQuestion = true;
				tagButton.setQuestion(question);
				log.info("Updated question");
			}
		}
		if (! foundQuestion) {
			TagButton tagButton = new TagButton(this.timepoint, question, null);
			addTagButton(tagButton);
			log.info("Added new question");
		}
		
	}

}
