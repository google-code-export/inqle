package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
public class ListSelectorPage extends BeanWizardPage {

	protected IConverter beanItemToWidgetConverter;

	protected IConverter widgetToBeanItemConverter;

	/**
	 * the class of items in the bean's List field.
	 * Defaults to String.class if not explicitly set
	 */
	protected Class<?> beanItemClass = String.class;

	protected String[] initialItems;

	private Object[] items;

	protected Composite composite;
	private ListViewer viewer;
	private static final Logger log = Logger.getLogger(ListSelectorPage.class);
	
	public ListSelectorPage(IBasicJenabean bean, String beanValueId, String title, ImageDescriptor titleImage) {
		super(bean, beanValueId, title, titleImage);
		assert(this.bean != null);
		assert(this.beanValueId != null);
	}

	/**
	 * Add form elements.
	 */
	@Override
	public void addElements(Composite composite) {
		this.composite = composite;
		assert(composite != null);
		assert(initialItems != null);
		
		createList();
	}

	protected void createList() {
		new Label (composite, SWT.NONE).setText(labelText);	
		viewer = new ListViewer(composite, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		viewer.getList().setLayoutData(gridData);
		viewer.setContentProvider(new ArrayContentProvider());
		//viewer.setContentProvider(new ObservableListContentProvider());
		
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		updateList();
	}

	private void updateList() {
		viewer.getList().removeAll();
		viewer.add(items);	
		bindItem(viewer, bean, beanValueId);
	}

	/**
	 * Optional.  Set the class of the items to be loaded into the bean.
	 * @param tableBeanObject
	 */
	public void setBeanItemClass(Class<?> beanItemClass) {
		this.beanItemClass = beanItemClass;
	}

	public void setBeanItemToWidgetConverter(IConverter beanItemToWidgetConverter) {
		this.beanItemToWidgetConverter = beanItemToWidgetConverter;
	}

	public void setWidgetToBeanItemConverter(IConverter widgetToBeanItemConverter) {
		this.widgetToBeanItemConverter = widgetToBeanItemConverter;
	}

	public void setListItems(Object[] items) {
		this.items = items;
	}
}
