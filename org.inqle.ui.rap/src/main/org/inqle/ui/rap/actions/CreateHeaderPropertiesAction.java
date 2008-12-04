package org.inqle.ui.rap.actions;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.ui.rap.widgets.HeaderPropertiesDialog;
import org.inqle.ui.rap.widgets.SubpropertyDialog;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * This action opens the AOntResourceDialog.  If user submits a new Resource, this 
 * action then sets the new Resource as a subclass of owlPropertyUri.  
 * It then adds any new statements to the model.
 */
public class CreateHeaderPropertiesAction extends Action {
	
//	private final IWorkbenchWindow window;
	//private int instanceNum = 0;
//	private final String viewId;

	
	private static final Logger log = Logger.getLogger(CreateHeaderPropertiesAction.class);
	private OntClass domainClass;
	private Shell shell;
	private List<String> headerList;
	private String internalDatasetRoleId;
	private String subjectClass;
	
	/**
	 * Create a dialog, to create multiple properties
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlPropertyUri
	 */
	public CreateHeaderPropertiesAction(Shell shell, List<String> headerList, String internalDatasetRoleId, String subjectClass) {
//		super(shell, internalDatasetRoleId, owlPropertyUri);
		this.shell = shell;
		this.headerList = headerList;
		this.internalDatasetRoleId = internalDatasetRoleId;
		log.trace("Created CreateHeaderPropertiesAction");
		this.subjectClass = subjectClass;
	}
	
	public void run() {
		try {
			OntModel ontModel = ModelFactory.createOntologyModel();
			HeaderPropertiesDialog headerPropertiesDialog = new HeaderPropertiesDialog(shell, ontModel, headerList, subjectClass);
			headerPropertiesDialog.open();
			if (headerPropertiesDialog.getReturnCode() == Window.OK) {
				log.info("Created new properties for <" + parentResourceUri + ">:\n" + JenabeanWriter.modelToString(ontModel));
				OntProperty ontProperty = (OntProperty)aResourceDialog.getOntResource();
				if (domainClass != null) {
					ontProperty.addDomain(domainClass);
				}
				registerNewRdf(ontModel, ontProperty);
			}
		}	catch (Exception e) {
			log.error("Error running SubpropertyDialog", e);
		}
	}

	public void setDomainClass(OntClass domainSubject) {
		this.domainClass = domainSubject;
	}
}
