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
public class NameDescriptionPage extends BeanWizardPage {

	private static final Logger log = Logger.getLogger(NameDescriptionPage.class);
	
	public NameDescriptionPage(IBasicJenabean bean, String title, ImageDescriptor titleImage) {
		super(bean, null, title, titleImage);
		assert(this.bean != null);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(2, false);
		selfComposite.setLayout(gl);
		new Label (selfComposite, SWT.NONE).setText("Name");	
		Text name = new Text(selfComposite, SWT.BORDER);
    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    name.setLayoutData(gridData);
    
    new Label (selfComposite, SWT.NONE).setText("Description");	
		Text description = new Text(selfComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		//description.setSize (description.computeSize (500, 200));
		//gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//description.setEditable(true);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		description.setLayoutData(gridData);
		
//    description.addListener(SWT.TRAVERSE_RETURN, this);
//    
//    {
//  		public void keyTraversed(TraverseEvent e) {
//  			if (e.detail == SWT.TRAVERSE_RETURN) {
//  				e.doit = false;
//  				e.detail = SWT.TRAVERSE_NONE;
//  			}
//  		}
//  	});
    
    bindText(name, bean, "name");
    bindText(description, bean, "description");
	}

}
