package org.inqle.ui.rap.widgets;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class SearchBox extends Composite {

	private Text searchText;
	private Button searchButton;
	private Label label;
	private String toolTipText;
	private Layout formLayout;

	private static final Logger log = Logger.getLogger(SearchBox.class);
	
	public SearchBox(Composite parent, int style, String labelText, String searchButtonText) {
		super(parent, style);
		//Composite composite = new Composite(parent, style);
		Composite composite = this;
		if (formLayout == null) {
			formLayout = new GridLayout(3, false);
		}
		composite.setLayout(formLayout);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		composite.setLayoutData(gridData);
		
		//create the controls
		label = new Label(composite, SWT.NONE);
		label.setText(labelText);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		searchText = new Text(composite, SWT.BORDER);
		searchText.setLayoutData(gridData);
		log.info("Size=" + searchText.getSize());
		if (toolTipText != null) {
			searchText.setToolTipText(toolTipText);
		}
		searchButton = new Button(composite, SWT.PUSH | SWT.BORDER);
		searchButton.setText(searchButtonText);
	}
	
	public String getSearchText() {
		return searchText.getText();
	}
	
	public void setSearchText(String searchString) {
		searchText.setText(searchString);
	}

	public void addSelectionListener(SelectionListener selectionListener) {
		searchButton.addSelectionListener(selectionListener);
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}

	public Layout getFormLayout() {
		return formLayout;
	}

	public void setFormLayout(Layout formLayout) {
		this.formLayout = formLayout;
	}
}
