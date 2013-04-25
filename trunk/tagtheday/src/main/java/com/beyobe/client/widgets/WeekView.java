package com.beyobe.client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

public class WeekView extends Composite implements TapHandler {
	private Button left;
	private TabPanel tabPanel;
	private Button right;

	public WeekView() {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setHeight("15px");
		left = new Button("<");
		left.addTapHandler(this);
		left.setSmall(true);
//		left.setRound(true);
		right = new Button(">");
		right.addTapHandler(this);
		right.setSmall(true);
//		right.setRound(true);
		panel.add(left);
		
//		addWeekTabs();
//		panel.add(tabPanel);
		
		panel.add(right);
		initWidget(panel);
	}

//	private void addWeekTabs() {
//		for (int i=0; i< 7; i++) {
//			TabBarButtonBase tab = new TabBarButtonBase(null, null);
//			tab.setText(String.valueOf(i));
//			tab.setWidth("14.29%");
//			tabPanel.add(tab);
//		}
//	}

	@Override
	public void onTap(TapEvent event) {
		if (left.equals(event.getSource())) {
			
		}
		if (right.equals(event.getSource())) {
			
		}
	}
}
