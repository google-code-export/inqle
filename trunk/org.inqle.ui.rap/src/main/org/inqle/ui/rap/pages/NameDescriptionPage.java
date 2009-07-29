package org.inqle.ui.rap.pages;


import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

/**
 * This generates a wizard page which has a single text field, without validation.  
 * Usage (from within your implementation of DynaWizard):
 * 
 * public void addPages() {
 * 	SingleTextPage dbUserPage = new SingleTextPage(bean, "dbUser", "Database User Name", null);
 * 	dbUserPage.setLabelText("Enter database user name");
 * 	addPage(dbUserPage);
 * }
 * 
 * @author David Donohue
 * Feb 20, 2008
 */
public class NameDescriptionPage extends DynaWizardPage {

	private static final Logger log = Logger.getLogger(NameDescriptionPage.class);
	private Text name;
	private Text description;
	
	public NameDescriptionPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(2, false);
		selfComposite.setLayout(gl);
		new Label (selfComposite, SWT.NONE).setText("Name");	
		name = new Text(selfComposite, SWT.BORDER);
    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    name.setLayoutData(gridData);
    
    new Label (selfComposite, SWT.NONE).setText("Description");	
		description = new Text(selfComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		//description.setSize (description.computeSize (500, 200));
		//gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//description.setEditable(true);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		description.setLayoutData(gridData);
	}

	public String getName() {
		if (name==null) return null;
		return name.getText();
	}
	
	public String getDescription() {
		if (description == null) return null;
		return description.getText();
	}

	public void setTheName(String theName) {
		name.setText(theName);
	}

	public void setTheDescription(String theDescription) {
		description.setText(theDescription);
	}
}
