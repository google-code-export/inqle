/**
 * 
 */
package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

/**
 * Abstract class for wizard pages, which is designed to be extended w/ minimal effort.
 * 
 * This class expects to receive a Bean, to be used as a model, for storing
 * information filled out in this wizard page.
 * 
 * To get full functionality of this class (e.g. methods onNextPage() and onEnterPage()),
 * must use DynaWizardDialog and not the standard WizardDialog
 * @author David Donohue
 * Feb 20, 2008
 */
public abstract class BeanWizardPage extends DynaWizardPage {

	protected IBasicJenabean bean;
	protected String beanValueId;
	protected Realm realm = Realm.getDefault();
	protected DataBindingContext bindingContext;
	
	static Logger log = Logger.getLogger(BeanWizardPage.class);
	
	protected String labelText = "";
	
	/**
	 * @param pageName
	 * @param titleImage
	 */
	public BeanWizardPage(IBasicJenabean bean, String valueId, String title, ImageDescriptor titleImage) {
		super(title, titleImage);
		assert(bean != null);
		this.bean = bean;
		this.beanValueId = valueId;
	}
	
	/* 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {		
		//set up data binding
		if( Realm.getDefault() == null ) {
      SWTObservables.getRealm( Display.getCurrent() );
    }
		realm = Realm.getDefault();
		bindingContext = new DataBindingContext();
		
		super.createControl(parent);
	}

	protected void bindText(Control control, Object beanObject, String beanObjectValueId) {
		IObservableValue observedWidget = SWTObservables.observeText(control, SWT.FocusOut);
		IObservableValue observedBean = BeansObservables.observeValue(realm, beanObject, beanObjectValueId);
		bindingContext.bindValue(observedWidget, observedBean, null, null);
	}
	
	/**
	 * Bind an ISelectionProvider (e.g. a ListViewer) to the bean object.
	 * Permits selecting single item from a ListViewer
	 * Objects in List selector have the same class as the object in bean field
	 * @param viewer
	 * @param viewerItemField the name of the field (within the viewer's item) to display
	 * @param beanObject
	 * @param beanObjectValueId
	 */
	protected void bindItem(ISelectionProvider viewer, Object beanObject, String beanObjectValueId) {
		IObservableValue selection = ViewersObservables.observeSingleSelection(viewer);
		//IObservableValue detailObservable = BeansObservables.observeDetailValue(realm, selection, viewerItemField, String.class);
		IObservableValue observedBean = BeansObservables.observeValue(realm, beanObject, beanObjectValueId);
		
		//bindingContext.bindValue(detailObservable, observedBean, null, null);
		bindingContext.bindValue(selection, observedBean, new UpdateValueStrategy(), new UpdateValueStrategy());
	}
	
	/**
	 * Set up databinding from a list widget to a List field in a Javabean.
	 * @param control the widget (Control) object
	 * @param beanObject the Java Object adhering to the Javabean spec
	 * @param beanObjectValueId the id of the field in the bean, which we are binding to
	 * @param listItemClass the Class of the items in the bean's List field
	 * @param controlToBeanConverter either null (if no conversion required) 
	 * or an IConverter which converts the list elements from the widget 
	 * to the members of the bean's List field
	 * @param beanToControlConverter either null (if no conversion required) 
	 * or an IConverter which converts the members of the bean's List field
	 * to the list elements from the widget 
	 */
	protected void bindList(IObservableList observedList, 
			Object beanObject, 
			String beanObjectValueId,
			Class<?> listItemClass,
			IConverter controlToBeanConverter, 
			IConverter beanToControlConverter) {
		 // Initiating the realm once sets the default session Realm
	  if( Realm.getDefault() == null ) {
	    SWTObservables.getRealm( Display.getCurrent() );
	  }
	  // Strategy to convert elements from list widget to bean
	  UpdateListStrategy controlToBean = new UpdateListStrategy();
	  if (controlToBeanConverter != null) {
	  	controlToBean.setConverter( controlToBeanConverter );
	  }
	  // Strategy to convert elements from bean to list widget
	  UpdateListStrategy beanToControl = new UpdateListStrategy();
	  if (beanToControlConverter != null) {
	  	beanToControl.setConverter( beanToControlConverter );
	  }
	  
	  IObservableList observedBean = BeansObservables.observeList( realm, beanObject, beanObjectValueId, listItemClass );

	  bindingContext.bindList( observedList, 
	  		observedBean,
	  		controlToBean,
	  		beanToControl );
	}
}
