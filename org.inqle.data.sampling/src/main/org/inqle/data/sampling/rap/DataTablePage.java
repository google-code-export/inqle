package org.inqle.data.sampling.rap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.sampling.IDataTable;
import org.inqle.ui.rap.pages.BeanWizardPage;
import org.inqle.ui.rap.table.ListListTableLabelProvider;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * This generates a wizard page which has a table of data, for display only.  
 * The data in the table is represented by a RdfTable object,
 * which contains the result of a SPARQL query.
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
public class DataTablePage extends BeanWizardPage {
	
	/**
	 * the class of items in the table's List field.
	 */
	protected Class<?> tableBeanClass = List.class;

	/**
	 * the class of the objects in the model's List field.
	 * Defaults to String.class if not explicitly set
	 */
	protected Class<?> modelListClass = String.class;
	
	protected List<List<Object>> rows = new ArrayList<List<Object>>();

	private static final Logger log = Logger.getLogger(DataTablePage.class);
	
	/**
	 * the List of property names = columns in the table, = field identifiers in QuerySolution
	 */
	protected List<String> propertyNames = new ArrayList<String>();

//	protected TableViewer tableViewer;

	private Composite composite;

//	private Table table;
	
	/**
	 * Create a new page.
	 * @param modelBean the model bean to receive the checkbox selections
	 * @param modelBeanValueId the Javabean-compliant field within the model bean, to contain the checked items
	 * @param modelListClass the class of items in the List in the model bean
	 * @param title the title of this page
	 * @param titleImage
	 */
	public DataTablePage(IBasicJenabean modelBean, String modelBeanValueId, Class<?> modelListClass, String title, ImageDescriptor titleImage) {
		super(modelBean, modelBeanValueId, title, titleImage);
		this.modelListClass = modelListClass;
		assert(this.bean != null);
		assert(this.beanValueId != null);
	}

	/**
	 * Add form elements upon initial construction
	 */
	@Override
	public void addElements() {
		Composite composite = selfComposite;
		assert(composite != null);
		this.composite = composite;
//		table = new Table(composite, SWT.NONE);
//		tableViewer = new TableViewer(table);
	}
	

	/**
	 * Create and populate the table, using the column names provided in 
	 * <code>setPropertyNames</code> and the row data provided by <code>setRdfTable</code>
	 * 
	 */
	public void refreshTableData() {
//		if (table != null) {
//			table.clearAll();
//		}

		Table table = new Table(composite, SWT.NONE);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
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
		
		TableViewer tableViewer = new TableViewer(table);
		
		ObservableListContentProvider olContentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(olContentProvider);
		
		ListListTableLabelProvider labelProvider = new ListListTableLabelProvider();
		labelProvider.setColumnFields(propertyNames);
		tableViewer.setLabelProvider(labelProvider);
		
		WritableList writableListInput = new WritableList(getRows(), tableBeanClass);
		log.debug("getRows():" + getRows());
		tableViewer.setInput(writableListInput);
		tableViewer.refresh();
		//this.getShell().pack(true);
	}

	protected List<List<Object>> getRows() {
		return rows;
	}

	/**
	 * Set the rows of data in the table.  The class of items should be set in the setRowItemClass() method
	 * @param beans
	 */
	public void setDataTable(IDataTable dataTable) {
		//this.rdfTable = rdfTable;
		this.rows = dataTable.getRows();
	}

	public void setPropertyNames(List<String> propertyNames) {
		this.propertyNames = propertyNames;
	}

}
