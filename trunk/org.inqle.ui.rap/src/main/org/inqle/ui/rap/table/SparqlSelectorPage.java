/**
 * 
 */
package org.inqle.ui.rap.table;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * Abstract base class for Wizard Pages, which will perform a query at run time to populate a checkbox
 * table, permitting selection of 1 or more rows.  Implementing classes must implement the
 * <code>getQuery()</code> method to retrieve the SPARQL query, and the <code>getNamedModelIds()</code> method to 
 * retrieve the List of NamedModel IDs to query
 * @author David Donohue
 * Mar 18, 2008
 */
public abstract class SparqlSelectorPage extends RdfTableSelectorPage {

	private Persister persister;

	private RdfTable resultRdfTable = new RdfTable();

	private static final Logger log = Logger.getLogger(SparqlSelectorPage.class);
	
	public SparqlSelectorPage(IBasicJenabean modelBean, String modelBeanValueId,
			Class<?> modelListClass, String title, ImageDescriptor titleImage) {
		super(modelBean, modelBeanValueId, modelListClass, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<QuerySolution> getRows() {
		return resultRdfTable.getResultList();
	}

	protected abstract Collection<String> getNamedModelIds();

	protected abstract String getQuery();

	public void setPersister(Persister persister) {
		this.persister = persister;
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		updateRows();
		//tableViewer.refresh();
		refreshTableData();
	}

	private void updateRows() {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(getQuery());
		queryCriteria.addNamedModelIds(getNamedModelIds());
		log.info("Performing query:\n" + getQuery() + "\non these named models:\n" + getNamedModelIds());
		resultRdfTable = Queryer.selectRdfTable(queryCriteria);
		log.info("Retrieved " + resultRdfTable.getResultList());
	}
}
