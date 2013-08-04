package com.beyobe.client.views;

import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.activities.TagdayPlace;
import com.beyobe.client.beans.Question;
import com.beyobe.client.icons.Icons;
import com.beyobe.client.widgets.QuestionForm;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.RoundPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.touch.TouchDelegate;

public class QuestionViewImpl extends Composite implements QuestionView {

	private static final Logger log = Logger.getLogger("QuestionViewImpl");
	private ScrollPanel scrollPanel = new ScrollPanel();
	private Presenter presenter;
//	private Question question;
	
//	private static QuestionViewImplUiBinder uiBinder = GWT
//			.create(QuestionViewImplUiBinder.class);


//	public QuestionViewImpl() {
//		initWidget(uiBinder.createAndBindUi(this));
//	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public QuestionViewImpl() {
		RoundPanel roundPanel = new RoundPanel();
		roundPanel.addStyleName("ttd-editQuestionPanel");
//		Button closeButton = new Button("x");
//		closeButton.setImportant(true);
//		closeButton.setSmall(true);
//		closeButton.getElement().getStyle().setProperty("float", "right");
		
//		HorizontalPanel menuBar = new HorizontalPanel();
//		roundPanel.add(menuBar);
		
		Icons icons = GWT.create(Icons.class);
		
		Image closeIcon = new Image(icons.close32());
		closeIcon.getElement().getStyle().setProperty("float", "right");
		TouchDelegate closeTd = new TouchDelegate(closeIcon);
		closeTd.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				App.placeController.goTo(new TagdayPlace());
			}
		});
		roundPanel.add(closeIcon);
		
		App.questionForm = new QuestionForm(App.question);
		roundPanel.add(App.questionForm);
        scrollPanel.setWidget(roundPanel);
		
        scrollPanel.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss().fillPanelExpandChild());
        scrollPanel.setScrollingEnabledX(false);
        scrollPanel.setScrollingEnabledY(true);
        scrollPanel.refresh();
        scrollPanel.setSnap(false);
        scrollPanel.setSnapSelector(null);
        scrollPanel.setHideScrollBar(false);
        
        
        scrollPanel.setHeight("100%");
        initWidget(scrollPanel);
	}

//	@Override
//	public void setQuestion(Question question) {
//		this.question = question;		
//	}
}
