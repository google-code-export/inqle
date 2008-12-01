package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
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
public class CreateSubpropertyAction extends ACreateOntResourceAction {
	
//	private final IWorkbenchWindow window;
	//private int instanceNum = 0;
//	private final String viewId;

	
	private static final Logger log = Logger.getLogger(CreateSubpropertyAction.class);
	private OntClass domainClass;
	
	/**
	 * Create a dialog, to import a new OWL resource, which is an instance of the class specified by
	 * owlPropertyUri.  Import into the internal dataset of the specified role.
	 * This constructor supports updating the LARQ text index, if applicable.
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlPropertyUri
	 */
	public CreateSubpropertyAction(Shell shell, String internalDatasetRoleId, String owlPropertyUri) {
		super(shell, internalDatasetRoleId, owlPropertyUri);
		log.trace("Created CreateSubpropertyAction");
	}
	
	/**
	 * Create a dialog, to import a new OWL resource, which is an instance of the class specified by
	 * owlPropertyUri.  Import into the internal dataset of the specified role/
	 * This constructor does not support updating the LARQ text index.  However, it can
	 * be used for importing into an external dataset
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlPropertyUri
	 */
	public CreateSubpropertyAction(Shell shell, Model model, String owlPropertyUri) {
		super(shell, model, owlPropertyUri);
		log.trace("Created CreateSubpropertyAction");
	}
	
	public void run() {
		try {
			OntModel ontModel = null;
			if (domainClass == null) {
				ontModel = ModelFactory.createOntologyModel();
			} else {
				ontModel = domainClass.getOntModel();
			}
			OntProperty superOntProperty = ontModel.createOntProperty(parentResourceUri);
			SubpropertyDialog aResourceDialog = new SubpropertyDialog(shell, superOntProperty);
			aResourceDialog.open();
			if (aResourceDialog.getReturnCode() == Window.OK) {
				log.info("Created new subproperty of <" + parentResourceUri + ">:\n" + JenabeanWriter.modelToString(ontModel));
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
