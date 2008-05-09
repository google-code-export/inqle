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
import org.eclipse.swt.events.SelectionEvent;
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
import org.inqle.ui.rap.views.SparqlView;

import thewebsemantic.TypeWrapper;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * @author David Donohue
 * May 6, 2008
 */
public class ExperimentsView extends SparqlView {

	private static final Logger log = Logger.getLogger(ExperimentsView.class);

	public static final String ID = "org.inqle.experiment.rapidminer.experimentsView";

	@Override
	public String getSparql() {
		
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT ?id ?creationDate \n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"?uri inqle:id ?id\n" +
			". ?uri inqle:creationDate ?creationDate\n" +
			". OPTIONAL { ?uri inqle:name ?name }\n" +
				". ?uri a ?classUri\n" +
			". ?classUri <" + RDF.JAVA_CLASS + "> \"" + ExperimentResult.class.getName() + "\" \n" +
			"\n} } ORDER BY " + getCurrentSortDirection() + "(?" + getCurrentSortColumn() + ") \n";
		sparql +=  "LIMIT " + String.valueOf(getRecordCount()) + " OFFSET " + String.valueOf(getOffset());
		return sparql;
	}

}
