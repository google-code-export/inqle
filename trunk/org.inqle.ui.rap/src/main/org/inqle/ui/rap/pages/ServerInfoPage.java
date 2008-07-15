package org.inqle.ui.rap.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ServerInfoPage extends WizardPage {

	private static final String PAGE_NAME = "Your INQLE Server";
	private static final String PAGE_DESCRIPTION = "Please enter info about your INQLE server.  " +
			"This info will be sent to the Central INQLE Server.  " +
			"It will be used to identify your server, and to permit INQLE to " +
			"contact owners of INQLE servers with security-related messages.";
	private Text siteNameText;
	private Text ownerEmailText;
	private String defaultSiteName;
	private String defaultOwnerEmail;
	
	public ServerInfoPage(String pageName, String pageDescription) {
		super(pageName, pageName, null);
		setMessage(pageDescription);
	}

	public ServerInfoPage() {
		this(PAGE_NAME, PAGE_DESCRIPTION);
	}
	
	public void createControl(Composite parent) {
		GridData gridData;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout (new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		
		new Label(composite, SWT.NONE).setText("Server Nickname");
		siteNameText =  new Text(composite, SWT.BORDER);
		if (getDefaultSiteName() != null) {
			siteNameText.setText(getDefaultSiteName());
		}
		siteNameText.setToolTipText("Enter a nickname for your INQLE Server, e.g. David Donohue's INQLE Server #3");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		siteNameText.setLayoutData(gridData);
		
		new Label(composite, SWT.NONE).setText("Owner Email");
		ownerEmailText =  new Text(composite, SWT.BORDER);
		if (getDefaultOwnerEmail() != null) {
			ownerEmailText.setText(getDefaultOwnerEmail());
		}
		ownerEmailText.setToolTipText("Email address of the owner of this server.");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		ownerEmailText.setLayoutData(gridData);
		
		setControl(composite);
	}

	public String getDefaultSiteName() {
		return defaultSiteName;
	}

	public void setDefaultSiteName(String defaultSiteName) {
		this.defaultSiteName = defaultSiteName;
	}

	public String getDefaultOwnerEmail() {
		return defaultOwnerEmail;
	}

	public void setDefaultOwnerEmail(String defaultOwnerEmail) {
		this.defaultOwnerEmail = defaultOwnerEmail;
	}
	
	public String getSiteName() {
		return siteNameText.getText();
	}
	
	public String getOwnerEmail() {
		return ownerEmailText.getText();
	}

}
