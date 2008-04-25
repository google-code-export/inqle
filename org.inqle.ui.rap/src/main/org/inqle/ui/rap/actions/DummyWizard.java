/**
 * 
 */
package org.inqle.ui.rap.actions;


import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.RDBModel;
import org.inqle.data.rdf.jena.sdb.DBConnector;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.tree.parts.DatabasePart;
import org.inqle.ui.rap.tree.parts.ModelPart;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class DummyWizard extends Wizard {

	static Logger log = Logger.getLogger(DummyWizard.class);
	Composite composite;

	Composite parent;
	
	LoadFromPage loadFromPage = new LoadFromPage("Dummy loadFromPage");
	LoadFilePage loadFilePage = new LoadFilePage("Dummy loadFilePage");
	/**
	 * This generates the wizard page for creating a database connection
	 * @author David Donohue
	 * Feb 8, 2008
	 */
	class LoadFromPage extends WizardPage {
		Button fileRadioButton = null;
		Button webRadioButton = null;
		
		LoadFromPage(String pageName) {
			super(pageName);
		}
		
		public void createControl(Composite pageParent) {
			parent = pageParent;
			
			composite = new Composite(parent, SWT.NONE);
	    // create the desired layout for this wizard page
			GridLayout gl = new GridLayout(2, false);
			composite.setLayout(gl);
	    
	    //create the form
			GridData gridData;
			
			new Label (composite, SWT.NONE).setText("Load data from");	
			fileRadioButton = new Button(composite, SWT.RADIO);
	    fileRadioButton.setText("Local file");
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    fileRadioButton.setLayoutData(gridData);
	    
	    webRadioButton = new Button(composite, SWT.RADIO);
	    webRadioButton.setText("Remote file via the web");
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    webRadioButton.setLayoutData(gridData);
	    
	    setControl(composite);

		}
		
		public IWizardPage getNextPage(){	
		   if (fileRadioButton.getSelection()) {
		       return ((DummyWizard)getWizard()).loadFilePage;
		   }
		   return ((DummyWizard)getWizard()).loadFromPage;
		}
	}
	
	class LoadFilePage extends WizardPage {

		LoadFilePage(String pageName) {
			super(pageName);
		}
		
		public void createControl(Composite pageParent) {
			parent = pageParent;
			composite = new Composite(parent, SWT.NONE);
	    // create the desired layout for this wizard page
			GridLayout gl = new GridLayout(2, false);
			composite.setLayout(gl);
	    
	    //create the form
			GridData gridData;
			
			new Label (composite, SWT.NONE).setText("Select file to upload");	
			setControl(composite);
		}
	}
	
	public DummyWizard() {
	}

	@Override
	public void addPages() {
		
		addPage(loadFromPage);
		addPage(loadFilePage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		//close wizard regardless
		return true;
	}

}
