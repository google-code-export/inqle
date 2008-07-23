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
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.widgets.ResourceDialog;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * This action opens the ResourceDialog.  If user submits a new Resource, this 
 * action then sets the new Resource as a subclass of owlClassUri.  
 * It then adds any new statements to the model.
 */
public class CreateOwlClassAction extends Action {
	
//	private final IWorkbenchWindow window;
	//private int instanceNum = 0;
//	private final String viewId;

	private Model model;

	private String owlClassUri;

	private Model newStatements;

	private Shell shell;
	
	private static final Logger log = Logger.getLogger(CreateOwlClassAction.class);
	
	public CreateOwlClassAction(Shell shell, Model model, String owlClassUri) {
		log.trace("Create CreateOwlClassAction");
		this.shell = shell;
		this.model = model;
		this.owlClassUri = owlClassUri;
//		this.viewId = viewId;
//    setText(label);
    // The id is used to refer to the action in a menu or toolbar
//		setId(ICommandIds.CMD_OPEN);
    // Associate the action with a pre-defined command, to allow key bindings.
//		setActionDefinitionId(ICommandIds.CMD_OPEN);
		//setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.inqle.ui.rap", "/icons/sample2.gif"));
//		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "/" + iconPath));
	}
	
	public void run() {
		try {
			OntModel ontModel = ModelFactory.createOntologyModel();
			OntClass ontClass = ontModel.createClass(owlClassUri);
			ResourceDialog resourceDialog = new ResourceDialog(shell, ontClass);
			resourceDialog.open();
			if (resourceDialog.getReturnCode() == Window.OK) {
				log.info("Created new <" + RDF.DATA_SUBJECT + ">:\n" + JenabeanWriter.modelToString(ontModel));
				newStatements = ontModel.difference(model);
				log.info("Saving these new statements:" + JenabeanWriter.modelToString(newStatements));
				model.add(newStatements);
				
				//send the new statements to the central INQLE server
				Map<String, String> params = new HashMap<String, String>();
				params.put(InqleInfo.PARAM_REGISTER_RDF, JenabeanWriter.modelToString(model));
				Persister persister = Persister.getInstance();
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
}
