/**
 * 
 */
package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Abstract class for wizard pages, which is designed to be extended w/ minimal effort.
 * To get full functionality of this class (e.g. methods onNextPage() and onEnterPage(),
 * must use DynaWizardDialog and not the standard WizardDialog
 * @author David Donohue
 * Feb 20, 2008
 */
public abstract class DynaWizardPage extends WizardPage {

	//protected Composite parentComposite;
	protected Composite selfComposite;
	
	private static Logger log = Logger.getLogger(DynaWizardPage.class);
	
	protected String labelText = "";
	
	/**
	 * @param pageName
	 * @param titleImage
	 */
	public DynaWizardPage(String title, ImageDescriptor titleImage) {
		super(title, title, titleImage);
	}
	
	/* 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
//		this.parentComposite = parent;
		selfComposite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		selfComposite.setLayout(gl);
		
		//add any form elements
		//addElements(selfComposite);
		addElements();
		
		setControl(selfComposite);
	}
	
	/**
	 * Override this to add any form elements.  For each form element, 
	 * add any controls, and add databinding if necessary
	 * 
	 * Example implementation:
	    new Label (composite, SWT.NONE).setText("Database Username");	
	    Text dbUser = new Text(composite, SWT.BORDER);
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			dbUser.setLayoutData(gridData);
			IObservableValue dbUserObserveWidget = SWTObservables.observeText(dbUser, SWT.FocusOut);
			IObservableValue dbUserObserveValue = BeansObservables.observeValue(realm, connection, "dbUser");
			bindingContext.bindValue(dbUserObserveWidget, dbUserObserveValue, null, null);
			
	 * @param composite the composite to add the controls to
	 */
	public abstract void addElements();

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	/**
	 * Override if you need something to happen upon going to the next page.  
	 * Default implementation does nothing & returns true.  
	 * @param if false, not permitted to go to next page
	 */
	public boolean onNextPage() {
		return true;
	}

	/**
	 * Override if you need something to happen upon going to the previous page.  
	 * Default implementation does nothing & returns true.  
	 * @param if false, not permitted to go to next page
	 */
	public boolean onPreviousPage() {
		return true;
	}
	
	/**
	 * This is called upon entering this page from the next page
	 * Default implementation does nothing & returns true.  Override if you need something to 
	 * happen upon entering this page
	 */
	public void onEnterPageFromNext() {
	}

	/**
	 * This is called upon entering this page from the previous page
	 * Default implementation does nothing & returns true.  Override if you need something to 
	 * happen upon entering this page
	 */
	public void onEnterPageFromPrevious() {
	}
}
