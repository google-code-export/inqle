/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.table.QuerySolutionTableLabelProvider;

import thewebsemantic.TypeWrapper;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * @author David Donohue
 * May 6, 2008
 */
public class ExperimentsViewDeleteme extends ViewPart {

	private static final Logger log = Logger.getLogger(ExperimentsViewDeleteme.class);

	public static final String ID = "org.inqle.experiment.rapidminer.experimentsView";
	
	private static final int COLUMN_WIDTH = 100;
	private static final int DEFAULT_RECORD_COUNT = 100;
	
	public static final String SPARQL_END =
		"\n} } ORDER BY DESC(?creationDate) \n";
	
	//TODO clean up name URI
	public static final String SPARQL_BEGIN = 
		"PREFIX rdf: <" + RDF.RDF + ">\n" + 
		"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
		"SELECT ?id ?creationDate \n" +
		"{\n" +
		"GRAPH ?g {\n" +
		"?uri inqle:" + RDF.JENABEAN_ID_ATTRIBUTE + " ?id\n" +
		". ?uri inqle:creationDate ?creationDate\n" +
		". OPTIONAL { ?uri inqle:name ?name }\n";

	
	private Composite composite;
	
	protected List<QuerySolution> rows = new ArrayList<QuerySolution>();
	
	/**
	 * the class of items in the table's List field.
	 */
	protected Class<?> tableBeanClass = QuerySolution.class;
	
	/**
	 * the List of property names = columns in the table, = field identifiers in QuerySolution
	 */
	protected List<String> propertyNames = new ArrayList<String>();

	private Persister persister;
	private TableViewer tableViewer;
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		//composite = new Composite(parent, SWT.None);
		composite = parent;
//		GridLayout gridLayout = new GridLayout();
//		composite.setLayout(gridLayout);
		//GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		
		composite.setLayoutData(gridData);
		showTable();
	}
	
	public void createTable() {
//		if (table != null) {
//			table.clearAll();
//		}

		//if no data, return
		if (rows == null || rows.size() == 0) {
			return;
		}
		
		Table table = new Table(composite, SWT.NONE);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		//add columns
		for (String propertyName: propertyNames) {
			TableColumn column = new TableColumn(table,SWT.LEFT);
			column.setText(propertyName);
			column.setResizable(true);
			column.setWidth(COLUMN_WIDTH);
			log.trace("Added column: " + propertyName);
		}
		
		tableViewer = new TableViewer(table);
		ObservableListContentProvider olContentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(olContentProvider);
		
		QuerySolutionTableLabelProvider labelProvider = new QuerySolutionTableLabelProvider();
		labelProvider.setColumnFields(propertyNames);
		tableViewer.setLabelProvider(labelProvider);
	}
	
	public void showTable() {
		if (rows == null || rows.size() == 0) {
			doQuery();
		}
		if (rows == null || rows.size() == 0) {
			return;
		}
		if (tableViewer == null) {
			createTable();
		}
		
		WritableList writableListInput = new WritableList(rows, tableBeanClass);
		log.info("writableListInput=" + writableListInput);
		tableViewer.setInput(writableListInput);
		//tableViewer.getTable().pack();
		tableViewer.refresh(false);
		//composite.getShell().pack();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		log.info("ExperimentsView.setFocus()");
		showTable();
	}
	
	/**
	 * Set the rows of data in the table.  The class of items should be set in the setRowItemClass() method
	 * @param beans
	 */
	public void setRdfTable(RdfTable rdfTable) {
		//this.rdfTable = rdfTable;
		this.rows = rdfTable.getResultList();
		this.propertyNames = rdfTable.getVarNameList();
	}

	public List<QuerySolution> getRows() {
		return rows;
	}
	
	public void doQuery() {
		String sparql = getSparql();
		log.trace("Querying w/ SPARQL:" + sparql);
		QueryCriteria queryCriteria = new QueryCriteria(getPersister());
		queryCriteria.setQuery(sparql);
		//TODO change to LogModel
		queryCriteria.addNamedModel(persister.getAppInfo().getRepositoryNamedModel());
		RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
		log.trace("Received these results: " + resultTable.getResultList());
		setRdfTable(resultTable);
	}

	private String getSparql() {
		String recordCount = String.valueOf(DEFAULT_RECORD_COUNT);
		String offset = "0";
		
		String sparql = SPARQL_BEGIN +
			". ?uri a ?classUri\n" +
			". ?classUri <" + RDF.JAVA_CLASS + "> \"" + ExperimentResult.class.getName() + "\" \n" +
			SPARQL_END;
		sparql +=  "LIMIT " + recordCount + " OFFSET " + offset;
		return sparql;
	}

	private Persister getPersister() {
		if (persister == null) {
			persister = Persister.createPersister();
		}
		return persister;
	}

}
