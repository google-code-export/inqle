package org.inqle.ui.rap.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.ui.rap.pages.DynaWizardPage;

public class UserAccountPage extends DynaWizardPage {

	private static final String PAGE_DESCRIPTION = "Specify the username and password for accessing this server.";
	private static final String PAGE_NAME = "Create an Administrator Account";
	private Text loginText;
	private Text password1Text;
	private Text password2Text;

	protected UserAccountPage(String pageName) {
		super(pageName, null);
	}

	public UserAccountPage(String pageName, String pageDescription) {
		super(pageName, null);
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
		
		loginText.forceFocus();
		
		setControl(composite);

	}
	
	@Override
	public boolean onNextPage() {
		if (getUserName() == null || getPassword() == null) {
			setMessage("Please provide a user name and a password for your INQLE server.  Make sure your 2 passwords match.");
			return false;
		}
		return true;
	}
	
	public String getUserName() {
		if (loginText==null || loginText.getText()==null || loginText.getText().length()==0) return null;
		return loginText.getText();
	}
	
	public String getPassword() {
		if (password1Text==null || password2Text==null || password1Text.getText()==null || password1Text.getText().length()==0) return null;
		if (password1Text.getText().equals(password2Text.getText())) {
			return password1Text.getText();
		}
		return null;
	}

	@Override
	public void addElements() {	
	}

}
