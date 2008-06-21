package org.inqle.ui.rap.pages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class EmbeddedDBPage extends WizardPage {

	private Composite composite;
	private Text dbNameText;
	private Text dbLoginText;
	private Text dbPasswordText;

	public EmbeddedDBPage(String pageName) {
		this(pageName, pageName, null);
	}
	
	protected EmbeddedDBPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout (new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		new Label(composite, SWT.BORDER).setText("Database Name");
		dbNameText =  new Text(composite, SWT.NONE);
		new Label(composite, SWT.BORDER).setText("Database Login");
		dbLoginText =  new Text(composite, SWT.NONE);
		new Label(composite, SWT.BORDER).setText("Database Password");
		dbPasswordText =  new Text(composite, SWT.NONE);
		setControl(composite);
	}
	
	public String getDbName() {
		return dbNameText.getText();
	}
	
	public String getDbLogin() {
		return dbLoginText.getText();
	}
	
	public String getDbPassword() {
		return dbPasswordText.getText();
	}
}
