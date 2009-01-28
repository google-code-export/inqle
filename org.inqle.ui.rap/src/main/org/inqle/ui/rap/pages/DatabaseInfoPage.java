package org.inqle.ui.rap.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.inqle.ui.rap.widgets.TextFieldShower;

public class DatabaseInfoPage extends DynaWizardPage {

	private static final String PAGE_NAME = "Database Info";
	private static final String PAGE_DESCRIPTION = "";
	private TextFieldShower idShower;
	private TextFieldShower nameShower;
	private TextFieldShower descriptionShower;
	private String databaseId;
	private String databaseName;
	private String databaseDescription;
	
	private DatabaseInfoPage(String pageName, String pageDescription) {
		super(pageName, null);
		setMessage(pageDescription);
	}

	public DatabaseInfoPage(String id, String name, String description) {
		this(PAGE_NAME, PAGE_DESCRIPTION);
		this.databaseId = id;
		this.databaseName = name;
		this.databaseDescription = description;
	}
	
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout (new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

		idShower = new TextFieldShower(
				composite,
				"Database ID",
				"Enter a unique ID for the database, e.g. my.domain.name.data.1\nDo not use spaces or special characters, other than a period or underscore.",
				null,
				SWT.BORDER
		);
		if (databaseId != null) {
			idShower.setValue(databaseId);
			idShower.setEnabled(false);
		}

		nameShower = new TextFieldShower(
				composite,
				"Database Name",
				"Enter a nickname for this database.",
				null,
				SWT.BORDER
		);
		if (databaseName != null) {
			nameShower.setValue(databaseName);
		}
			
		descriptionShower = new TextFieldShower(
				composite,
				"Database Name",
				"Enter a description about this database.",
				null,
				SWT.BORDER
		);
		if (databaseDescription != null) {
			descriptionShower.setValue(databaseDescription);
		}
		
		setControl(composite);
	}

	@Override
	public boolean onNextPage() {
		setMessage(PAGE_DESCRIPTION);
		
		if (getDatabaseId()==null || getDatabaseId().length()==0) {
			setMessage("Please enter an ID for this database.");
			return false;
		}
		
		if (getDatabaseName()==null || getDatabaseName().length()==0) {
			setMessage("Please enter a name for this database.");
			return false;
		}
		
		if (getDatabaseDescription()==null || getDatabaseDescription().length()==0) {
			setMessage("Please enter a description.");
			return false;
		}

		return true;
	}
	
	@Override
	public void addElements() {		
	}

	public String getDatabaseId() {
		return idShower.getValue();
	}

	public String getDatabaseName() {
		return nameShower.getValue();
	}

	public String getDatabaseDescription() {
		return descriptionShower.getValue();
	}

}
