package org.inqle.ui.rap.actions;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.widgets.HeaderPropertiesDialog;
import org.inqle.ui.rap.widgets.SubpropertyDialog;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
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
//	private OntClass domainClass;
	private Shell shell;
	private String[] headers;
	private String internalDatasetRoleId;
	private String subjectClass;
	private Model model;
	private String subjectName;
	
	/**
	 * Create a dialog, to create multiple properties
	 * @param shell
	 * @param internalDatasetRoleId
	 * @param owlPropertyUri
	 */
	public CreateHeaderPropertiesAction(Shell shell, String[] headers, String internalDatasetRoleId, String subjectClass, String subjectName) {
//		super(shell, internalDatasetRoleId, owlPropertyUri);
		this.shell = shell;
		this.headers = headers;
		this.internalDatasetRoleId = internalDatasetRoleId;
		log.trace("Created CreateHeaderPropertiesAction");
		this.subjectClass = subjectClass;
		this.subjectName = subjectName;
		Persister persister = Persister.getInstance();
		this.model = persister.getInternalModel(internalDatasetRoleId);
	}
	
	public void run() {
		try {
			OntModel ontModel = ModelFactory.createOntologyModel();
			HeaderPropertiesDialog headerPropertiesDialog = new HeaderPropertiesDialog(shell, ontModel, headers, subjectClass, subjectName);
			headerPropertiesDialog.setUriPrefix(subjectClass + "/");
			headerPropertiesDialog.open();
			if (headerPropertiesDialog.getReturnCode() == Window.OK) {
				OntModel theModel = headerPropertiesDialog.getOntModel();
				log.info("Created new Model:" + JenabeanWriter.modelToString(theModel));
				registerNewRdf(theModel);
//				log.info("Created new properties for <" + parentResourceUri + ">:\n" + JenabeanWriter.modelToString(ontModel));
//				OntProperty ontProperty = (OntProperty)aResourceDialog.getOntResource();
//				if (domainClass != null) {
//					ontProperty.addDomain(domainClass);
//				}
//				registerNewRdf(ontModel, ontProperty);
			}
		}	catch (Exception e) {
			log.error("Error running HeaderPropertiesDialog", e);
		}
	}
	
	protected void registerNewRdf(OntModel ontModel) {
//	this.ontResource = aResourceDialog.getOntResource();
//	this.newUri = aResourceDialog.getUri();
	
		Model newStatementsModel = ontModel.difference(model);
		log.info("Saving these new statements:" + JenabeanWriter.modelToString(newStatementsModel));
		Persister persister = Persister.getInstance();
		
		long sizeBefore = model.size();
		model.begin();
		model.add(newStatementsModel);
		model.commit();
		
		long sizeDifference = model.size() - sizeBefore;
		log.info("Registered new type locally: Added " + sizeDifference + " new statements to the model.");
		
		//send the new statements to the central INQLE server
		Map<String, String> params = new HashMap<String, String>();
		params.put(InqleInfo.PARAM_ACTION, InqleInfo.ACTION_REGISTER_PROPERTIES);
		params.put(InqleInfo.PARAM_REGISTER_RDF, JenabeanWriter.modelToString(newStatementsModel));
		params.put(InqleInfo.PARAM_SITE_ID, persister.getAppInfo().getSite().getId());
		log.info("posting new type RDF data to " + InqleInfo.URL_CENTRAL_REGISTRATION_SERVICE + "...");
		boolean success = Requestor.sendPost(InqleInfo.URL_CENTRAL_REGISTRATION_SERVICE, params, new PrintWriter(System.out));
		log.info("...success? " + success);
	}

//	public void setDomainClass(OntClass domainSubject) {
//		this.domainClass = domainSubject;
//	}
}
