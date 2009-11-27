package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.widgets.SubclassDialog;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * This action opens the AOntResourceDialog.  If user submits a new Resource, this 
 * action then sets the new Resource as a subclass of owlClassUri.  
 * It then adds any new statements to the model.
 */
public class CreateSubclassAction extends ACreateOntResourceAction {
	
	private static final Logger log = Logger.getLogger(CreateSubclassAction.class);
	
	/**
	 * Create a dialog, to import a new OWL resource, which is an instance of the class specified by
	 * owlClassUri.  Import into the system datamodel of the specified role.
	 * This constructor supports updating the LARQ text index, if applicable.
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlClassUri
	 */
	public CreateSubclassAction(Shell shell, String datamodelId, String owlClassUri) {
		super(shell, datamodelId, owlClassUri, InqleInfo.ACTION_REGISTER_SUBJECTS);
		log.trace("Created CreateSubclassAction");
	}
	
	/**
	 * Create a dialog, to import a new OWL resource, which is an instance of the class specified by
	 * owlClassUri.  Import into the system datamodel of the specified role/
	 * This constructor does not support updating the LARQ text index.  However, it can
	 * be used for importing into an user datamodel
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlClassUri
	 */
	public CreateSubclassAction(Shell shell, Model model, String owlClassUri) {
		super(shell, model, owlClassUri, InqleInfo.ACTION_REGISTER_SUBJECTS);
		log.trace("Created CreateSubclassAction");
	}
	
	public void run() {
		try {
			OntModel ontModel = ModelFactory.createOntologyModel();
			OntClass ontClass = ontModel.createClass(parentResourceUri);
			SubclassDialog aResourceDialog = new SubclassDialog(shell, ontClass);
			
			aResourceDialog.open();
			if (aResourceDialog.getReturnCode() == Window.OK) {
				log.info("Created new subclass of <" + parentResourceUri + ">:\n" + JenabeanWriter.modelToString(ontModel));
				registerNewRdf(ontModel, aResourceDialog.getOntResource());
			}
		}	catch (Exception e) {
			log.error("Error running SubclassDialog", e);
		}
	}
}
