package org.inqle.ui.rap;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.widgets.AOntResourceDialog;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.larq.IndexBuilderModel;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * This action opens the AOntResourceDialog.  If user submits a new Resource, this 
 * action then sets the new Resource as a subclass of owlPropertyUri.  
 * It then adds any new statements to the model.
 */
public abstract class ACreateOntResourceAction extends Action {
	
	protected Model model;

	protected Model newStatements;

	private IndexBuilderModel textIndexBuilder;

	private String newUri;

	protected Shell shell;

	protected String parentResourceUri;
	
	private static final Logger log = Logger.getLogger(ACreateOntResourceAction.class);
	
	/**
	 * Create a dialog, to import a new OntResource, which is an instance of the class specified by
	 * owlPropertyUri.  Import into the internal dataset of the specified role.
	 * This constructor supports updating the LARQ text index, if applicable.
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlPropertyUri
	 */
	public ACreateOntResourceAction(Shell shell, String internalDatasetRoleId, String parentResourceUri) {
		this.shell = shell;
		log.trace("Create ACreateOntResourceAction");
		Persister persister = Persister.getInstance();
		this.model = persister.getInternalModel(internalDatasetRoleId);
		this.textIndexBuilder = persister.getIndexBuilder(internalDatasetRoleId);
		this.parentResourceUri = parentResourceUri;
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
	public ACreateOntResourceAction(Shell shell, Model model, String parentResourceUri) {
		this.shell = shell;
		log.trace("Create ACreateOntResourceAction");
		this.model = model;
		this.parentResourceUri = parentResourceUri;
	}
	
	/**
	 * Implementations should create an OntModel and the proper type of OntResource.
	 * Use this instance of the propert OntResource type to generate the proper
	 * AOntResourceDialog.  Open this dialog, then if return code is OK, call registerNewRdf()
	 */
	public abstract void run();

	protected void registerNewRdf(OntModel ontModel, AOntResourceDialog aResourceDialog) {
		this.newUri = aResourceDialog.getUri();
		newStatements = ontModel.difference(model);
		log.info("Saving these new statements:" + JenabeanWriter.modelToString(newStatements));
		Persister persister = Persister.getInstance();
		
		if (textIndexBuilder != null) {
			model.register(textIndexBuilder);
		}
		model.begin();
		model.add(newStatements);
		model.commit();
		if (textIndexBuilder != null) {
			model.unregister(textIndexBuilder);
		}
		//send the new statements to the central INQLE server
		Map<String, String> params = new HashMap<String, String>();
		params.put(InqleInfo.PARAM_REGISTER_RDF, JenabeanWriter.modelToString(newStatements));
		params.put(InqleInfo.PARAM_SITE_ID, persister.getAppInfo().getSite().getId());
		log.info("posting data to " + InqleInfo.URL_CENTRAL_REGISTRATION_SERVICE + "...");
		boolean success = Requestor.sendPost(InqleInfo.URL_CENTRAL_REGISTRATION_SERVICE, params, new PrintWriter(System.out));
		log.info("...success? " + success);
	}

	public Model getNewStatements() {
		return newStatements;
	}

	public String getNewUri() {
		return newUri;
	}
}
