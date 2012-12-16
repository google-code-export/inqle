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

public class EmbeddedDBPage extends DynaWizardPage {

	private Composite composite;
	private Text dbNameText;
	private Text dbLoginText;
	private Text dbPasswordText;
	private String defaultDBName;
	private String defaultUserName;
	private Text dbPassword2Text;
	private String pageDescription;

	public EmbeddedDBPage(String pageName, String pageDescription) {
		super(pageName,null);
		this.pageDescription = pageDescription;
		setMessage(pageDescription);
	}
	
//	protected EmbeddedDBPage(String pageName, String title,
//			ImageDescriptor titleImage) {
//		super(pageName, title, titleImage);
//	}

	public void createControl(Composite parent) {
		GridData gridData;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout (new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		
		new Label(composite, SWT.NONE).setText("Database Name");
		dbNameText =  new Text(composite, SWT.BORDER);
		if (defaultDBName != null) {
			dbNameText.setText(defaultDBName);
		}
		dbNameText.setToolTipText("The database name may only contain letters, numerals, and underscores _");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		dbNameText.setLayoutData(gridData);
		
		new Label(composite, SWT.NONE).setText("Database Login");
		dbLoginText =  new Text(composite, SWT.BORDER);
		if (defaultUserName != null) {
			dbLoginText.setText(defaultUserName);
		}
		dbLoginText.setToolTipText("This is the username which INQLE will use to connect to the embedded database.  If you later wish to administer this H2 database, you must remember this username.");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		dbLoginText.setLayoutData(gridData);
		
		new Label(composite, SWT.NONE).setText("Database Password");
		dbPasswordText =  new Text(composite, SWT.BORDER | SWT.PASSWORD);
		dbPasswordText.setToolTipText("This is the password which INQLE will use to connect to the embedded database.  If you later wish to administer this H2 database, you must remember this password.");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		dbPasswordText.setLayoutData(gridData);
		dbPasswordText.forceFocus();
		
		new Label(composite, SWT.NONE).setText("Retype Password");
		dbPassword2Text =  new Text(composite, SWT.BORDER | SWT.PASSWORD);
		dbPassword2Text.setToolTipText("Please retype the password which INQLE will use to connect to the embedded database.  If you later wish to administer this H2 database, you must remember this password.");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		dbPassword2Text.setLayoutData(gridData);

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

	public void setDefaultDBName(String defaultDBName) {
		this.defaultDBName = defaultDBName;
	}

	public void setDefaultUserName(String defaultUserName) {
		this.defaultUserName = defaultUserName;
	}
	
	@Override
	public boolean onNextPage() {
		setMessage(pageDescription);
		if (getDbName()==null || getDbName().length()==0) {
			setMessage("Please provide a name for yoru database.");
			return false;
		}
		if (getDbLogin()==null || getDbLogin().length()==0) {
			setMessage("Please provide a database user name.");
			return false;
		}
		if (dbPasswordText==null || dbPasswordText.getText()==null || dbPasswordText.getText().length()==0) {
			setMessage("Please provide a database password.");
			return false;
		}
		if (dbPassword2Text==null || dbPassword2Text.getText()==null || dbPassword2Text.getText().length()==0) {
			setMessage("Please reenter the database password.");
			return false;
		}
		if (dbPasswordText.getText().equals(dbPassword2Text.getText())) {
			return true;
		}
		setMessage("Please make sure your 2 passwords match.");
		return false;
	}

	@Override
	public void addElements() {		
	}
}
