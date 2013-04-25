package com.beyobe.client.widgets;

import java.util.Date;
import java.util.List;

import com.googlecode.mgwt.ui.client.widget.Button;

/**
 * An area in the UI representing a time interval, which can contain tags
 * @author donohue
 *
 */
public interface Block {
	
	String getLabelText();

	Date getMidpoint();

	Date getTimepoint();

	Date getStart();
	
	Date getEnd();
	
	List<TagButton> getTagButtons();
	
	void addTagButton(TagButton tagButton);
}
