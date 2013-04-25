package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.beyobe.client.App;
import com.beyobe.client.event.TagClickedEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;

public class Day extends Composite implements Block, TouchHandler, TapHandler {
	private static final String DAY_LABEL_FORMAT = "MMMM d, y";
	public static final int MILLISECONDS_IN_A_DAY = 86400000;
	protected Date start;
	protected Date end;
	protected Date created;
	protected VerticalPanel panel;
	protected FlowPanel tagsPanel;
	protected Label dateLabel;
	private Date midpoint;
	private Date timepoint;
	private List<TagButton> tagButtons = new ArrayList<TagButton>();
	
	public Day(Date point) {
		this.timepoint = point;
		this.created = new Date();
		panel = new VerticalPanel();
		panel.setWidth("100px");
		panel.setHeight("100%");
		dateLabel = new Label(getLabelText());
		tagsPanel = new FlowPanel();
		panel.add(dateLabel);
		panel.add(tagsPanel);
		
		start = new Date(point.getYear(), point.getMonth(), point.getDate());
		long startMS = start.getTime();
		end = new Date(startMS + MILLISECONDS_IN_A_DAY - 1);
		
		initWidget(panel);
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
//		tagButton.addTouchHandler(this);
		tagButton.addTapHandler(this);
		tagsPanel.add(tagButton);
	}

	@Override
	public void onTouchStart(TouchStartEvent event) {
		// Do nothing		
	}

	@Override
	public void onTouchMove(TouchMoveEvent event) {
		// Do nothing		
	}

	@Override
	public void onTouchEnd(TouchEndEvent event) {
		App.eventBus.fireEvent(new TagClickedEvent((TagButton)event.getSource()));	
	}

	@Override
	public void onTouchCanceled(TouchCancelEvent event) {
		// Do nothing	
	}

	@Override
	public void onTap(TapEvent event) {
		App.eventBus.fireEvent(new TagClickedEvent((TagButton)event.getSource()));	
	}

}
