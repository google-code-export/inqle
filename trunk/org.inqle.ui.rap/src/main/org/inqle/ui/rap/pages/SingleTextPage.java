package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;

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
public class SingleTextPage extends BeanWizardPage {

	private static final Logger log = Logger.getLogger(SingleTextPage.class);
	
	public SingleTextPage(IBasicJenabean bean, String beanValueId, String title, ImageDescriptor titleImage) {
		super(bean, beanValueId, title, titleImage);
		assert(this.bean != null);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(2, false);
		selfComposite.setLayout(gl);
		new Label (selfComposite, SWT.NONE).setText(labelText);	
		Text text = new Text(selfComposite, SWT.BORDER);
    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    text.setLayoutData(gridData);
		
    bindText(text, bean, beanValueId);
	}

}
