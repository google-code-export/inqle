package com.beyobe.client.views;

import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.beans.InfoStatus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.Button;

public class InfoViewImpl extends Composite implements InfoView {

	private static final Logger log = Logger.getLogger("InfoViewImpl");
	private static final long MAX_COUNTER = 10000;
	@UiField StackPanel stackPanel;
	private VerticalPanel alertPanel = new VerticalPanel();
	private VerticalPanel aboutPanel = new VerticalPanel();
	
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
		Label noAlertsLabel = new Label("No alerts");
		alertPanel.add(noAlertsLabel);
		stackPanel.add(alertPanel, "Alerts");
		String html = "Beyobe Client " + Constants.CLIENT + " version " + Constants.CLIENT_VERSION;
		html += "<br/><a target='_blank' href='http://www.beyobe.com'>Beyobe Website</a>";
		HTML aboutHtml = new HTML(html);
		aboutPanel.add(aboutHtml);
		stackPanel.add(aboutPanel, "About");

		openTab(presenter.getInfoStatus());
	}

	private void openTab(InfoStatus infoStatus) {
		if (infoStatus == InfoStatus.UNSAVED) {
			alertPanel.clear();
			Button saveButton = new Button("Save now");
			saveButton.addTapHandler(new TapHandler(){

				@Override
				public void onTap(TapEvent event) {
					App.dataBus.doSaveUnsavedToServer();
				}
				
			});
			alertPanel.add(new Label("Unable to save your data to the Beyobe server.  Please try to save when you have a signal."));
			alertPanel.add(saveButton);
			stackPanel.showStack(0);
		}
		
	}
}
