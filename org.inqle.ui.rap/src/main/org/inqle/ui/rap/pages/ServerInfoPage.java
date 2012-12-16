package org.inqle.ui.rap.pages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.ui.rap.widgets.TextFieldShower;

public class ServerInfoPage extends DynaWizardPage {

	private static final String PAGE_NAME = "Your INQLE Server";
	private static final String PAGE_DESCRIPTION = "Please enter info about your INQLE server.  " +
			"This info will be sent to the Central INQLE Server.  " +
			"It will be used to identify your server, and to permit INQLE to " +
			"contact owners of INQLE servers with security-related messages.";
//	private Text siteNameText;
//	private Text ownerEmailText;
	private String defaultSiteName;
	private String defaultOwnerEmail;
	private TextFieldShower siteNameShower;
	private TextFieldShower ownerEmailShower;
	private TextFieldShower uriPrefixShower;
	private TextFieldShower uriPrefixAbbrevShower;
	private String defaultUriPrefix;
	private String defaultUriPrefixAbbrev;
	
	public ServerInfoPage(String pageName, String pageDescription) {
		super(pageName, null);
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
		
//		new Label(composite, SWT.NONE).setText("Server Nickname");
//		siteNameText =  new Text(composite, SWT.BORDER);
//		if (getDefaultSiteName() != null) {
//			siteNameText.setText(getDefaultSiteName());
//		}
//		siteNameText.setToolTipText("Enter a nickname for your INQLE Server, e.g. David Donohue's INQLE Server #3");
//		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//		siteNameText.setLayoutData(gridData);
//		siteNameText.forceFocus();

//		ownerDomainShower = new TextFieldShower(
//				composite,
//				"Your domain",
//				"Enter your domain identifier, e.g. mycompany.com, or mydepartment.mycompany.com\nUse only letters, numbers, periods (.), or underscores.  Do not use spaces or other characters.",
//				null,
//				SWT.BORDER
//		);
		
		siteNameShower = new TextFieldShower(
				composite,
				"Server ID",
				"Enter an ID for your INQLE Server, e.g. my.domain.name.inqle.server.1\nUse only letters, numbers, periods (.), or underscores (_).  Do not use spaces or special characters.",
				null,
				SWT.BORDER
		);
		if (getDefaultSiteName() != null) {
			siteNameShower.setValue(getDefaultSiteName());
		}
		
//		new Label(composite, SWT.NONE).setText("Owner Email");
//		ownerEmailText =  new Text(composite, SWT.BORDER);
//		if (getDefaultOwnerEmail() != null) {
//			ownerEmailText.setText(getDefaultOwnerEmail());
//		}
//		ownerEmailText.setToolTipText("Email address of the owner of this server.");
//		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//		ownerEmailText.setLayoutData(gridData);
		
		ownerEmailShower = new TextFieldShower(
				composite,
				"Owner Email",
				"Enter the email address of the owner of this server.",
				null,
				SWT.BORDER
		);
		if (getDefaultOwnerEmail() != null) {
			ownerEmailShower.setValue(getDefaultOwnerEmail());
		}
			
//		new Label(composite, SWT.NONE).setText("Server URI Prefix");
//		uriPrefixText =  new Text(composite, SWT.BORDER);
//		if (getDefaultSiteName() != null) {
//			uriPrefixText.setText(getDefaultUriPrefix());
//		}
//		uriPrefixText.setToolTipText("Enter a unique URI for your INQLE Server, e.g. http://my.domain.name/inqle/1/");
//		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//		uriPrefixText.setLayoutData(gridData);
		
		uriPrefixShower = new TextFieldShower(
				composite,
				"URI Prefix",
				"Enter the unique URI prefix for your institution, e.g. http://my.domain.name/inqle/ns/",
				null,
				SWT.BORDER
		);
		if (getDefaultUriPrefix() != null) {
			uriPrefixShower.setValue(getDefaultUriPrefix());
		}
		
		uriPrefixAbbrevShower = new TextFieldShower(
				composite,
				"URI Prefix Abbreviation",
				"Enter an abbreviation for the URI prefix for your institution, e.g. my_domain\nDo not use spaces or special characters.",
				null,
				SWT.BORDER
		);
		if (getDefaultUriPrefixAbbrev() != null) {
			uriPrefixAbbrevShower.setValue(getDefaultUriPrefixAbbrev());
		}
		
		setControl(composite);
	}

	public String getDefaultUriPrefix() {
		return defaultUriPrefix;
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
		if (siteNameShower==null) return null;
		return siteNameShower.getValue();
	}
	
	public String getOwnerEmail() {
		if (ownerEmailShower==null) return null;
		String ownerEmail = ownerEmailShower.getValue();
		
		//Do a regular expression confirmation of the owner email:
		//Set the email pattern string
    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
    Matcher m = p.matcher(ownerEmail);
    if(! m.matches()) return null;
		return ownerEmail;
	}
	
	public String getUriPrefix() {
		String enteredUriPrefix = uriPrefixShower.getValue();
		if (enteredUriPrefix == null || enteredUriPrefix.length()==0) return null;
		if (!(enteredUriPrefix.charAt(enteredUriPrefix.length()-1)=="/".charAt(0))) {
			enteredUriPrefix += "/";
		}
		return enteredUriPrefix;
	}
	
	public String getUriPrefixAbbrev() {
		if (uriPrefixAbbrevShower==null) return null;
		return uriPrefixAbbrevShower.getValue();
	}

	@Override
	public boolean onNextPage() {
		setMessage(PAGE_DESCRIPTION);
		
		if (getSiteName()==null || getSiteName().length()==0) {
			setMessage("Please enter a nickname for your INQLE server.");
			return false;
		}
		
		if (getOwnerEmail()==null) {
			setMessage("Please enter a valid email address.");
			return false;
		}
		
		if (getUriPrefix()==null) {
			setMessage("Please enter a URI prefix.  It should look like a web address.");
			return false;
		}
		
		if (getUriPrefixAbbrev()==null) {
			setMessage("Please enter a URI prefix abbreviation.  It should contain no spaces or special characters.");
			return false;
		}
		
		
		
		return true;
	}
	
	@Override
	public void addElements() {		
	}

	public String getDefaultUriPrefixAbbrev() {
		return defaultUriPrefixAbbrev;
	}

	public void setDefaultUriPrefixAbbrev(String defaultUriPrefixAbbrev) {
		this.defaultUriPrefixAbbrev = defaultUriPrefixAbbrev;
	}

}
