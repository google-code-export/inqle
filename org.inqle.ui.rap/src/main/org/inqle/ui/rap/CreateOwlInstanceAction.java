package org.inqle.ui.rap;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.widgets.ResourceDialog;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.larq.IndexBuilderModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * This action opens the ResourceDialog.  If user submits a new Resource, this 
 * action then sets the new Resource as a subclass of owlClassUri.  
 * It then adds any new statements to the model.
 */
public class CreateOwlInstanceAction extends Action {
	
//	private final IWorkbenchWindow window;
	//private int instanceNum = 0;
//	private final String viewId;

	private Model model;

	private String owlClassUri;

	private Model newStatements;

	private Shell shell;

	private IndexBuilderModel textIndexBuilder;

	private String newUri;
	
	private static final Logger log = Logger.getLogger(CreateOwlInstanceAction.class);
	
	/**
	 * Create a dialog, to import a new OWL resource, which is an instance of the class specified by
	 * owlClassUri.  Import into the internal dataset of the specified role.
	 * This constructor supports updating the LARQ text index, if applicable.
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlClassUri
	 */
	public CreateOwlInstanceAction(Shell shell, String internalDatasetRoleId, String owlClassUri) {
		log.trace("Create CreateOwlInstanceAction");
		this.shell = shell;
		Persister persister = Persister.getInstance();
		this.model = persister.getInternalModel(internalDatasetRoleId);
		this.textIndexBuilder = persister.getIndexBuilder(internalDatasetRoleId);
		this.owlClassUri = owlClassUri;
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
	public CreateOwlInstanceAction(Shell shell, Model model, String owlClassUri) {
		log.trace("Create CreateOwlInstanceAction");
		this.shell = shell;
		this.model = model;
		this.owlClassUri = owlClassUri;
	}
	
	public void run() {
		try {
			OntModel ontModel = ModelFactory.createOntologyModel();
			OntClass ontClass = ontModel.createClass(owlClassUri);
			ResourceDialog resourceDialog = new ResourceDialog(shell, ontClass);
			resourceDialog.open();
			if (resourceDialog.getReturnCode() == Window.OK) {
				log.info("Created new <" + owlClassUri + ">:\n" + JenabeanWriter.modelToString(ontModel));
				this.newUri = resourceDialog.getUri();
				newStatements = ontModel.difference(model);
				log.info("Saving these new statements:" + JenabeanWriter.modelToString(newStatements));
//				model.add(newStatements);
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
				boolean success = Requestor.postData(InqleInfo.URL_CENTRAL_REGISTRATION_SERVICE, params, new PrintWriter(System.out));
				log.info("...success? " + success);
			}
		}	catch (Exception e) {
			log.error("Error running ResourceDialog:" + e.getMessage());
		}
	}

	public Model getNewStatements() {
		return newStatements;
	}

	public String getNewUri() {
		return newUri;
	}
}
