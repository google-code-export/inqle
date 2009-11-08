package org.inqle.ui.rap.views;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Jenamodel;
import org.inqle.ui.rap.widgets.ResultSetTable;
import org.inqle.ui.rap.widgets.ResultSetTable.UriValData;

/**
 * @author David Donohue
 * May 6, 2008
 */
public class SubjectStatementsView extends SparqlView {

	private static final Logger log = Logger.getLogger(SubjectStatementsView.class);

	public static final String ID = "org.inqle.ui.rap.views.SubjectStatementsView";

	private static final String PROPERTY_VARIABLE = "Property";

	private static final String VALUE_VARIABLE = "Value";

	protected String classUri;

	protected String subjectUri;
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		currentSortColumn = PROPERTY_VARIABLE;
		currentSortDirection = SparqlView.ASC;
		hideUriColumn = false;
		linkColumn = VALUE_VARIABLE;
	}
	
	//TODO optional fields will render the columns retrieved on any pass thru variable. Must extract columns from the query
	@Override
	public String getSparql() {
		
		String sparql = 
			"SELECT ?" + PROPERTY_VARIABLE + " ?" + VALUE_VARIABLE + " \n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"<" + getSubjectUri() + "> ?" + PROPERTY_VARIABLE + " ?" + VALUE_VARIABLE + " \n" +
			"\n} } ORDER BY " + getCurrentSortDirection() + "(?" + getCurrentSortColumn() + ") \n";
		sparql +=  "LIMIT " + String.valueOf(getRecordCount()) + " OFFSET " + String.valueOf(getOffset());
		return sparql;
	}
	
	private String getSubjectUri() {
		return subjectUri;
	}

//	@Override
//	public Datamodel getDatamodel() {
//		return datamodel;
//	}
//
//	public void setDatamodel(Datamodel datamodel) {
//		this.namedModel = datamodel;
//	}

	public void setSubjectUri(String subjectUri) {
		this.subjectUri = subjectUri;
	}
	
	@Override
	public void widgetSelected(SelectionEvent event) {
		
		Object source = event.getSource();
		if (source instanceof Link) {
			Link link = (Link)source;
			Object data = link.getData();
			if (data==null) return;
			
			if (data instanceof UriValData) {
				UriValData uriValData = (UriValData) data;
				SubjectStatementsView ssView = (SubjectStatementsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(SubjectStatementsView.ID);
				if (ssView==null) {
					try {
						ssView = (SubjectStatementsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SubjectStatementsView.ID);
					} catch (PartInitException e) {
						log.error("Error showing view: " + SubjectStatementsView.ID, e);
					}
				}
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop(ssView);
				ssView.setDatamodelId(datamodelId);
				ssView.setSubjectUri(uriValData.getUriVal());
				ssView.setTitleText("Properties of thing: <" + uriValData.getUriVal() + ">");
				log.info("Refreshing Subject Statements View with datamodel: " + datamodelId + " and instance URI: " + data.toString());
				ssView.refreshView();
//				ssView.setFocus();
				
				return;
			}
		}
		
		super.widgetSelected(event);
	}
}
