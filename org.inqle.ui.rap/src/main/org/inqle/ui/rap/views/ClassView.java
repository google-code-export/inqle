package org.inqle.ui.rap.views;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.inqle.core.util.InqleInfo;
import org.inqle.rdf.RDF;
import org.inqle.ui.rap.widgets.ResultSetTable.UriValData;

/**
 * @author David Donohue
 * May 6, 2008
 */
public class ClassView extends SparqlView {

	private static final Logger log = Logger.getLogger(ClassView.class);

	public static final String ID = "org.inqle.ui.rap.views.ClassView";

	protected String classUri;

	private Link viewDetailsForClass;
	
	@Override
	public void createPartControl(Composite parent) {
		viewDetailsForClass = new Link(parent, SWT.NONE);
		viewDetailsForClass.setText("<a>View details for this class</a>");
		viewDetailsForClass.addSelectionListener(this);
		
		super.createPartControl(parent);
		currentSortColumn = InqleInfo.URI_VARIABLE;
		currentSortDirection = SparqlView.ASC;
		hideUriColumn = false;
		linkColumn = InqleInfo.URI_VARIABLE;
		linkUriOnly = true;
	}
	
	//TODO optional fields will render the columns retrieved on any pass thru variable. Must extract columns from the query
	@Override
	public String getSparql() {
		
		String sparql = 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"SELECT ?" + InqleInfo.URI_VARIABLE + " ?Name ?Description \n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"?" + InqleInfo.URI_VARIABLE + " a <" + getClassUri() + "> \n" +
			". OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " rdfs:label ?Name } \n" +
			". OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " rdfs:comment ?Description }\n" +
			"\n} } ORDER BY " + getCurrentSortDirection() + "(?" + getCurrentSortColumn() + ") \n";
		sparql +=  "LIMIT " + String.valueOf(getRecordCount()) + " OFFSET " + String.valueOf(getOffset());
		return sparql;
	}
	
//	@Override
//	public Datamodel getDatamodel() {
//		return datamodel;
//	}
//
//	public void setDatamodel(Datamodel datamodel) {
//		this.namedModel = datamodel;
//	}

	public String getClassUri() {
		return classUri;
	}

	public void setClassUri(String classUri) {
		this.classUri = classUri;
	}
	
	@Override
	public void widgetSelected(SelectionEvent event) {
		
		Object source = event.getSource();
		if (source instanceof Link) {
			
			if (source == viewDetailsForClass) {
				showStatementsForSubject(datamodelId, classUri);
				return;
			}
			
			Link link = (Link)source;
			Object data = link.getData();
			if (data==null) return;
			
			if (data instanceof UriValData) {
				UriValData uriValData = (UriValData) data;
				
				showStatementsForSubject(datamodelId, uriValData.getUriVal());
				
//				ssView.setFocus();
				
				return;
			}
		}
		
		super.widgetSelected(event);
	}

	private void showStatementsForSubject(String datamodelId, String subjectUri) {
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
		ssView.setSubjectUri(subjectUri);
		ssView.setTitleText("Properties of thing: <" + subjectUri + ">");
		log.info("Refreshing Subject Statements View with datamodel: " + datamodelId + " and instance URI: " + subjectUri);
		ssView.refreshView();
		
	}
}
