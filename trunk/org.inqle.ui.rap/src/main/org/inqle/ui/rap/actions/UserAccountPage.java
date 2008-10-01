package org.inqle.ui.rap.actions;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UserAccountPage extends WizardPage {

	private static final String PAGE_DESCRIPTION = "Specify the username and password for accessing this server.";
	private static final String PAGE_NAME = "Create an Administrator Account";
	private Text loginText;
	private Text password1Text;
	private Text password2Text;

	protected UserAccountPage(String pageName) {
		super(pageName);
	}

	public UserAccountPage(String pageName, String pageDescription) {
		super(pageName, pageName, null);
		setMessage(pageDescription);
	}

	public UserAccountPage() {
		this(PAGE_NAME, PAGE_DESCRIPTION);
	}
	
	public void createControl(Composite parent) {
		GridData gridData;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout (new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		
		new Label(composite, SWT.NONE).setText("User Name");
		loginText =  new Text(composite, SWT.BORDER);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		loginText.setLayoutData(gridData);
		
		new Label(composite, SWT.NONE).setText("Password");
		password1Text = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		password1Text.setLayoutData(gridData);
		
		new Label(composite, SWT.NONE).setText("Re-enter Password");
		password2Text = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		password2Text.setLayoutData(gridData);
		
		setControl(composite);

	}
	
	@Override
	public boolean canFlipToNextPage() {
		if (getUserName() != null && getPassword() != null) {
			return true;
		}
		return false;
	}
	
	public String getUserName() {
		if (loginText==null) return null;
		return loginText.getText();
	}
	
	public String getPassword() {
		if (password1Text==null || password2Text==null) return null;
		if (password1Text.getText().equals(password2Text.getText())) {
			return password1Text.getText();
		}
		return null;
	}

}
