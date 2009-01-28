/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jena.util.OntModelUtil;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class AddReasonerStatementsWizard extends DynaWizard {

	private Text reasonerText;
	private static Logger log = Logger.getLogger(AddReasonerStatementsWizard.class);
	Composite composite;
	private Datamodel datamodel;
//	private ModelPart modelPart = null;
	private boolean successImporting = false;
	
	public AddReasonerStatementsWizard(Model saveToModel, Shell shell) {
		super(saveToModel, shell);
	}
	
	
	class AddReasonerPage extends WizardPage implements SelectionListener {

		private static final String TITLE = "Add Reasoner Statements";
		private static final String DESCRIPTION = "Specify RDF reasoner rules text, " +
				"which will be applied to this datamodel.  " +
				"The rules will be applied to the data set and any new statements that can be inferred " +
				"will be inserted into the datamodel.\n\n" +
				"See http://jena.sourceforge.net/inference/#RULEsyntax for details on semantic inference.\n\n" +
				"Example:\n" +
				"    [umbelSubj1: (?a owl:equivalentClass ?b) (?p rdfs:domain ?b) -> (?p rdfs:domain ?a)]\n" +
				"    [umbelSubj2: (?p rdfs:domain ?a) (?c <http://www.w3.org/2004/02/skos/core#narrowTransitive> ?b) -> (?p rdfs:domain ?c)]";

		AddReasonerPage() {
			super(TITLE);
			setMessage(DESCRIPTION);
		}
		
		public void createControl(Composite pageParent) {
			
			//clear the uploader
			//closeUploader();
			
			//shell = pageParent;
			composite = new Composite(pageParent, SWT.NONE);
	    // create the desired layout for this wizard page
			GridLayout gl = new GridLayout(1, false);
			composite.setLayout(gl);
	    
	    //create the form
			GridData gridData;
			
			new Label (composite, SWT.NONE).setText("Enter reasoner rules below.");	
			reasonerText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			//description.setSize (description.computeSize (500, 200));
			//gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			//description.setEditable(true);
			gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
			reasonerText.setLayoutData(gridData);
	    
			Button addStatementsButton = new Button(composite, SWT.BORDER);
			addStatementsButton.setText("Add Statements");
			addStatementsButton.addSelectionListener(this);
			setControl(composite);
		}

		public void widgetDefaultSelected(SelectionEvent arg0) {
		}

		public void widgetSelected(SelectionEvent arg0) {
			doAddStatements();
		}
	}
	
	
	
	
	
//	public LoadRdfFileWizard(ModelPart modelPart,	SDBDatabase connection) {
//		Persister persister = Persister.getInstance();
//		//this.persister = persister;
//		this.modelPart = modelPart;
//		this.connection = connection;
//		this.modelToLoad = persister.getModel(modelPart.getRdbModel());
//		//log.info("Temp Dir = " + tempDir.getAbsolutePath() + ": can write? " + tempDir.canWrite());
//	}

	@Override
	public void addPages() {
		AddReasonerPage addReasonerPage = new AddReasonerPage();
		addPage(addReasonerPage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override	
	public boolean performFinish() {
		return successImporting;
	}
	 
	
	public boolean doAddStatements() {
		String ruleText = reasonerText.getText();
		if (ruleText==null || ruleText.length()==0) return false;
		long preSize = saveToModel.size();
		
//		PopupDialog popup = new PopupDialog(getShell(), SWT.NONE, true, false, false, false, "Adding inferred statements...", "Using inference rule:\n" + ruleText);
//		popup.open();
//		log.info("Rendered popup");
		
		MessageDialog waitingDialog = new MessageDialog(
				getShell(), 
				"Adding inferred statements...", 
				 null, 
				 "Using inference rule:\n" + ruleText, 
				 MessageDialog.NONE,
				  new String [] {  }, 0);
		waitingDialog.setBlockOnOpen(false);
		waitingDialog.open();
				
	
		boolean errorOccurred = false;
		try {
			OntModelUtil.mergeInferredStatementsUsingSparql(saveToModel, ruleText);
		} catch (Exception e) {
			log.error("Unable to add inferred statements for rule:\n" + ruleText, e);
		}
		
		waitingDialog.close();
		log.info("Closed popup");
		long postSize = saveToModel.size();
		
		log.info("Added " + (postSize - preSize) + " statements.");
		
    if (errorOccurred) {
    	MessageDialog.openError(getShell(), "Error", "Error adding inferred statements for rule:\n" + ruleText);
    } else if (postSize > preSize) {
    	successImporting  = true;
    	MessageDialog.openInformation( getShell(), "Success adding inferred statements", "Successfully added " + (postSize - preSize) + " statements."); 
    	log.info("Flushing text index...");
			//flush any text indexes for the datamodel
			Persister persister = Persister.getInstance();
			persister.flushIndexes(datamodel);
			log.info("Finished flushing text index.");
    } else {
    	MessageDialog.openWarning( getShell(), "Loaded no data", "Failed to add any statements using reasoner rules:\n" + ruleText); 
    }
		
    return true;
	}

	public Datamodel getDataset() {
		return datamodel;
	}

	public void setDataset(Datamodel datamodel) {
		this.datamodel = datamodel;
	}
	
	/**
	 * Clean up the uploader to prevent javascript errors on repeated calls
	 */
//	public void closeUploader() {
//		//uploaderWidget.removeUploadListener(uploadAdapter);
//		log.info("Disposing of uploaderWidget=" + uploaderWidget);
//		if (uploaderWidget != null) {
//			uploaderWidget.dispose();
//		}
//	}

}
