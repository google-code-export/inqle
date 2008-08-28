package org.inqle.ui.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.ui.rap.widgets.InstanceDialog;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * This action opens the AOntResourceDialog.  If user submits a new Resource, this 
 * action then sets the new Resource as a subclass of owlClassUri.  
 * It then adds any new statements to the model.
 */
public class CreateInstanceAction extends ACreateOntResourceAction {
	
	private static final Logger log = Logger.getLogger(CreateInstanceAction.class);
	
	/**
	 * Create a dialog, to import a new OWL resource, which is an instance of the class specified by
	 * owlClassUri.  Import into the internal dataset of the specified role.
	 * This constructor supports updating the LARQ text index, if applicable.
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlClassUri
	 */
	public CreateInstanceAction(Shell shell, String internalDatasetRoleId, String owlClassUri) {
		super(shell, internalDatasetRoleId, owlClassUri);
		log.trace("Created CreateInstanceAction");
	}
	
	/**
	 * Create a dialog, to import a new OWL resource, which is an instance of the class specified by
	 * owlClassUri.  Import into the internal dataset of the specified role/
	 * This constructor does not support updating the LARQ text index.  However, it can
	 * be used for importing into an external dataset
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlClassUri
	 */
	public CreateInstanceAction(Shell shell, Model model, String owlClassUri) {
		super(shell, model, owlClassUri);
		log.trace("Created CreateInstanceAction");
	}
	
	public void run() {
		try {
			OntModel ontModel = ModelFactory.createOntologyModel();
			OntClass ontClass = ontModel.createClass(parentResourceUri);
			InstanceDialog aResourceDialog = new InstanceDialog(shell, ontClass);
			aResourceDialog.open();
			if (aResourceDialog.getReturnCode() == Window.OK) {
				log.info("Created new instance of <" + parentResourceUri + ">:\n" + JenabeanWriter.modelToString(ontModel));
				registerNewRdf(ontModel, aResourceDialog.getOntResource());
			}
		}	catch (Exception e) {
			log.error("Error running InstanceDialog", e);
		}
	}
}
