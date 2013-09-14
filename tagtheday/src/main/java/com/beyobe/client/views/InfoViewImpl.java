package com.beyobe.client.views;

import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.activities.TagdayPlace;
import com.beyobe.client.beans.InfoStatus;
import com.beyobe.client.icons.Icons;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.touch.TouchDelegate;

public class InfoViewImpl extends Composite implements InfoView {

	private static final Logger log = Logger.getLogger("InfoViewImpl");
	private static final long MAX_COUNTER = 10000;
//	@UiField StackPanel stackPanel;
//	@UiField StackLayoutPanel stackPanel;
	@UiField VerticalPanel alertPanel;
	@UiField VerticalPanel aboutPanel;
	@UiField Image backIcon;
	
	private Presenter presenter;
//	private int status;
	
	private static InfoViewImplUiBinder uiBinder = GWT
			.create(InfoViewImplUiBinder.class);

	interface InfoViewImplUiBinder extends UiBinder<Widget, InfoViewImpl> {
	}

//	public InfoViewImpl() {
//		initWidget(uiBinder.createAndBindUi(this));
//	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public InfoViewImpl() {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		showNoAlerts();
//		stackPanel.add(alertPanel, "Alerts");
		String html = "Beyobe Client <b>" + Constants.CLIENT + "</b> version <b>" + Constants.CLIENT_VERSION + "</b>";
		html += "<br/><a target='_blank' href='http://www.beyobe.com'>Beyobe Website</a>";
		HTML aboutHtml = new HTML(html);
		aboutPanel.add(aboutHtml);
		
		 TouchDelegate backTd = new TouchDelegate(backIcon);
		 backTd.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent e) {
					App.placeController.goTo(new TagdayPlace());
				}
			});
	}

	private void showNoAlerts() {
		alertPanel.clear();
		Label noAlertsLabel = new Label("No alerts");
		alertPanel.add(noAlertsLabel);
	}

	@Override
	public void setInfoStatusTab(InfoStatus infoStatus) {
		if (infoStatus == InfoStatus.UNSAVED) {
			alertPanel.clear();
			Button saveButton = new Button("Save now");
			saveButton.addTapHandler(new TapHandler(){
				@Override
				public void onTap(TapEvent event) {
					App.dataBus.doSaveUnsavedToServer();
				}
			});
			Icons icons = GWT.create(Icons.class);
			Image warnIcon = new Image(icons.warn32());
			Label warnLabel = new Label("Unable to save your data to the Beyobe server.  Please try to save when you have a signal.");
			warnLabel.addStyleName("ttd-warnLabel");
			alertPanel.add(warnIcon);
			alertPanel.add(warnLabel);
			alertPanel.add(saveButton);
		} else {
			showNoAlerts();
		}
		
	}
}
