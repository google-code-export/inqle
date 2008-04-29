package org.inqle.ui.rap.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.ui.rap.actions.BeanWizardPage;
import org.inqle.ui.rap.actions.DynaWizardPage;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * This generates a wizard page which has a table of data.  
 * The data in the table is represented by a RdfTable object,
 * which contains the result of a SPARQL query.
 * Each row has a checkbox, and may be selected.  The list of selected rows
 * is converted into a List of URIs
 *  
 * Usage (from within your implementation of DynaWizard):
 * 
 * public void addPages() {
 * 	TODO
 * }
 * 
 * Must provide converter using <code>setQuerySolutionToListItemConverter()</code>
 * 
 * @author David Donohue
 * Feb 20, 2008
 */
public class RdfTableSelectorPage extends BeanWizardPage {

	protected IConverter listItemToQuerySolutionConverter;

	protected IConverter querySolutionToListItemConverter;
	
	/**
	 * the class of items in the table's List field.
	 */
	protected Class<?> tableBeanClass = QuerySolution.class;

	/**
	 * the class of the objects in the model's List field.
	 * Defaults to String.class if not explicitly set
	 */
	protected Class<?> modelListClass = String.class;
	
	protected List<QuerySolution> rows = new ArrayList<QuerySolution>();

	private static final Logger log = Logger.getLogger(RdfTableSelectorPage.class);
	
	/**
	 * the List of property names = columns in the table, = field identifiers in QuerySolution
	 */
	protected List<String> propertyNames;

	//private RdfTable rdfTable;

	private String keyPropertyName;

	protected CheckboxTableViewer tableViewer;
	
	/**
	 * Create a new page.
	 * @param modelBean the model bean to receive the checkbox selections
	 * @param modelBeanValueId the Javabean-compliant field within the model bean, to contain the checked items
	 * @param modelListClass the class of items in the List in the model bean
	 * @param title the title of this page
	 * @param titleImage
	 */
	public RdfTableSelectorPage(IBasicJenabean modelBean, String modelBeanValueId, Class<?> modelListClass, String title, ImageDescriptor titleImage) {
		super(modelBean, modelBeanValueId, title, titleImage);
		this.modelListClass = modelListClass;
		assert(this.bean != null);
		assert(this.beanValueId != null);
	}

	/**
	 * Add form elements.
	 */
	@Override
	public void addElements(Composite composite) {
		assert(composite != null);
		
		//new Label (composite, SWT.NONE).setText(labelText);	
		Table table = new Table(composite, SWT.MULTI | SWT.CHECK);
    //GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    //table.setLayoutData(gridData);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    //create the TableViewer
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		//add columns
		for (String propertyName: propertyNames) {
			TableColumn column = new TableColumn(table,SWT.LEFT);
			column.setText(propertyName);
			column.setResizable(true);
			column.setWidth(200);
			
			//column.pack();
			log.debug("Added column: " + propertyName);
		}
		
		tableViewer = new CheckboxTableViewer(table);
		
		ObservableListContentProvider olContentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(olContentProvider);
		
		QuerySolutionTableLabelProvider labelProvider = new QuerySolutionTableLabelProvider();
		labelProvider.setColumnFields(propertyNames);
		tableViewer.setLabelProvider(labelProvider);
		refreshTableData();
		//log.info("Set List items to:" + beans);
//		bindList(listWidget, bean, beanValueId, this.beanItemClass, this.widgetToBeanItemConverter, this.beanItemToWidgetConverter);
		//log.info("Created CheckboxTableViewer using bean:" + JenabeanWriter.toString(this.bean));
	}
	

	public void refreshTableData() {
		WritableList writableListInput = new WritableList(getRows(), tableBeanClass);
		tableViewer.setInput(writableListInput);
		
		RdfTableCheckedList checkedBoxes = new RdfTableCheckedList(tableViewer, keyPropertyName);
		
		tableViewer.addCheckStateListener(checkedBoxes);
		//bindList(checkedBoxes, modelBean, beanValueId, modelListClass, new ConverterQuerySolutionToString(keyPropertyName), null);
		bindList(checkedBoxes, bean, beanValueId, modelListClass, null, null);
		
		tableViewer.refresh();
	}

	protected List<QuerySolution> getRows() {
		return rows;
	}

	public void setQuerySolutionToListItemConverter(IConverter querySolutionToListItemConverter) {
		this.querySolutionToListItemConverter = querySolutionToListItemConverter;
	}

	/**
	 * Set the rows of data in the table.  The class of items should be set in the setRowItemClass() method
	 * @param beans
	 */
	public void setRdfTable(RdfTable rdfTable) {
		//this.rdfTable = rdfTable;
		this.rows = rdfTable.getResultList();
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

	/**
	 * set the name of the field in the QuerySolution, to use as the value to populate the model List
	 * @param keyPropertyName
	 */
	public void setKeyPropertyName(String keyPropertyName) {
		this.keyPropertyName = keyPropertyName;		
	}
}
