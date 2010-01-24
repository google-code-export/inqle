package org.inqle.ui.rap.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jena.Jenamodel;
import org.inqle.data.rdf.jenabean.util.JenabeanWriter;
import org.inqle.rdf.beans.INamedAndDescribedJenabean;
import org.inqle.ui.rap.pages.BeanWizardPage;
import org.inqle.ui.rap.pages.DynaWizardPage;

/**
 * This generates a wizard page which has a table of data.  
 * Each row of data in the table is represented by a Javabean.
 * Each row has a checkbox, and may be selected.  Only a single
 * row may be selected.  The selected Javabeans
 * is converted into a the appropriate field in the model object
 *  
 * Usage (from within your implementation of DynaWizard):
 * 
 * public void addPages() {
 * 	 BeanTableSingleSelectorPage availableModelsPage = new BeanTableSingleSelectorPage(bean, "availableDatamodels", String.class, "Select available datasets", null);
		 availableModelsPage.setBeans(persister.listDatamodels());
		 availableModelsPage.setPropertyNames(Arrays.asList(new String[]{"modelName", "id", "class"}));
		 addPage(availableModelsPage);
 * }
 * 
 * If the class of beans in the table are not identical to the items in the model,
 * must provide converter using <code>setListItemToTableBeanConverter</code> and 
 * <code>setTableBeanToListItemConverter</code>
 * @author David Donohue
 * Feb 20, 2008
 */
@Deprecated
public class BeanTableSingleSelectorPage extends BeanWizardPage {

	protected IConverter listItemToTableBeanConverter;

	protected IConverter tableBeanToListItemConverter;
	
	/**
	 * the class of items in the table's List field.
	 */
	protected Class<?> tableBeanClass;

	/**
	 * the class of the objects in the model's List field.
	 * Defaults to String.class if not explicitly set
	 */
	protected Class<?> modelListClass = String.class;
	
	protected List<?> beans = new ArrayList<Object>();

	private static final Logger log = Logger.getLogger(BeanTableSingleSelectorPage.class);
	
	/**
	 * the List of property names = columns in the table, in the model Javabean, using Javabean property convention
	 */
	protected List<String> propertyNames;
	
	/**
	 * Create a new page.
	 * @param modelBean the model bean to receive the checkbox selections
	 * @param modelBeanValueId the Javabean-compliant field within the model bean, to contain the checked items
	 * @param modelListClass the class of items in the List in the model bean
	 * @param title the title of this page
	 * @param titleImage
	 */
	public BeanTableSingleSelectorPage(INamedAndDescribedJenabean modelBean, String modelBeanValueId, Class modelListClass, String title, ImageDescriptor titleImage) {
		super(modelBean, modelBeanValueId, title, titleImage);
		this.modelListClass = modelListClass;
		assert(this.bean != null);
		assert(this.beanValueId != null);
	}

	/**
	 * Add form elements.
	 */
	@Override
	public void addElements() {
		Composite composite = selfComposite;
		assert(composite != null);
		assert(beans != null);
		
		//new Label (composite, SWT.NONE).setText(labelText);	
		Table table = new Table(composite, SWT.MULTI | SWT.CHECK);
    //GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    //table.setLayoutData(gridData);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    //create the TableViewer
		CheckboxTableViewer tableViewer = createBoundViewer(table, bean);
		//log.info("Set List items to:" + beans);
//		bindList(listWidget, bean, beanValueId, this.beanItemClass, this.widgetToBeanItemConverter, this.beanItemToWidgetConverter);
		//log.info("Created CheckboxTableViewer using bean:" + JenabeanWriter.toString(this.bean));
	}

	/**
	 * Create a TableViewer, given the provided Table, the Javabean acting as the 
	 * data model, and the class of objects representing each row.
	 * Add the databinding to each row
	 * @param table
	 * @param modelBean
	 * @return
	 */
	private CheckboxTableViewer createBoundViewer(Table table, Object modelBean) {
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		//add columns
		for (String propertyName: propertyNames) {
			TableColumn column = new TableColumn(table,SWT.LEFT);
			column.setText(propertyName);
			column.setResizable(true);
			column.setWidth(200);
			log.debug("Added column: " + propertyName);
		}
		
		final CheckboxTableViewer viewer = new CheckboxTableViewer(table);
		
		ObservableListContentProvider olContentProvider = new ObservableListContentProvider();
		viewer.setContentProvider(olContentProvider);
		
		BeanTableLabelProvider labelProvider = new BeanTableLabelProvider();
		labelProvider.setColumnFields(propertyNames);
		viewer.setLabelProvider(labelProvider);
		WritableList writableListInput = new WritableList(beans, tableBeanClass);
		viewer.setInput(writableListInput); 
		BeanTableCheckedList checkedBoxes = new BeanTableCheckedList(viewer);

		//bind(checkedBoxes, modelBean, beanValueId, modelListClass, tableBeanToListItemConverter, listItemToTableBeanConverter);
		return viewer;
	}

	/**
	 * Set the class of the items to be loaded into the table.
	 * @param tableBeanClass
	 */
	public void setTableBeanClass(Class<?> tableBeanClass) {
		this.tableBeanClass = tableBeanClass;
	}
	
	public void setListItemToTableBeanConverter(IConverter listItemToBeanConverter) {
		this.listItemToTableBeanConverter = listItemToBeanConverter;
	}

	public void setTableBeanToListItemConverter(IConverter beanToListItemConverter) {
		this.tableBeanToListItemConverter = beanToListItemConverter;
	}

	/**
	 * Set the rows of data in the table.  The class of items should be set in the setRowItemClass() method
	 * @param beans
	 */
	public void setBeans(List<?> beans) {
		this.beans = beans;
	}
	
	
	/**
	 * Set the class of the items in the List in the model, to contain one entry for
	 * each checked item
	 * @param modelListClass
	 */
	public void setModelListClass(Class<?> modelListClass) {
		this.modelListClass = modelListClass;
	}

	public void setPropertyNames(List<String> propertyNames) {
		this.propertyNames = propertyNames;
	}
}
