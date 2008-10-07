/**
 * 
 */
package org.inqle.ui.rap.actions;


import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.Delete_OntologyDataset;
import org.inqle.data.rdf.jena.sdb.DBConnector;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.tree.parts.DatabasePart;
import org.inqle.ui.rap.widgets.TextFieldShower;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 * 
 * TODO extend DynaWizard instead of Wizard
 */
@Deprecated
public class Delete_OntologyDatasetWizard extends Wizard {

	private Connection connection = null;
	//private Persister persister;
	static Logger log = Logger.getLogger(Delete_OntologyDatasetWizard.class);
	Composite composite;
	int mode;

	Composite parent;
	private DatabasePart databasePart = null;
	//private ModelPart modelPart;
	private Delete_OntologyDataset startingDataset;
	private Delete_OntologyDataset dataset;
	private Text datasetIdText;
	private TextFieldShower filePathTextField;
	
	/**
	 * This generates the wizard page for creating a database connection
	 * @author David Donohue
	 * Feb 8, 2008
	 */
	public class OntologyDatasetInfoPage extends WizardPage {

		OntologyDatasetInfoPage(String pageName) {
			super(pageName);
		}
		
		public void createControl(Composite pageParent) {
			log.info("DatasetInfoPage.createControl()");
			parent = pageParent;
			
			//initialize the Dataset to the base starting Dataset
			resetModel();
			
			composite = new Composite(parent, SWT.NONE);
	    // create the desired layout for this wizard page
			GridLayout gl = new GridLayout(2, false);
			composite.setLayout(gl);
	    
	    
	    //create the form
			GridData gridData;
			
			/*
			list.addListener (SWT.DefaultSelection, new Listener () {
				public void handleEvent (Event e) {
					String string = "";
					int [] selection = list.getSelectionIndices ();
					for (int i=0; i<selection.length; i++) string += selection [i] + " ";
					System.out.println ("DefaultSelection={" + string + "}");
				}
			});
			*/
			
			new Label (composite, SWT.NONE).setText("Dataset ID (must be a unique name)");	
	    datasetIdText = new Text(composite, SWT.BORDER);
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    datasetIdText.setLayoutData(gridData);
			
//	    new Label (composite, SWT.NONE).setText("File path to the ontology file (optional)");	
//	    filePathText = new Text(composite, SWT.BORDER);
//	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//	    filePathText.setLayoutData(gridData);
	    
	    filePathTextField = new TextFieldShower(
	    		composite,
	    		"File path to the ontology file (optional)",
	    		"Specify the file path to the RDF ontology file or to the directory containing such files, " +
	    				"which INQLE should load into this ontology dataset.  INQLE will check this file or " +
	    				"directory each time it starts up.  If it finds any change from last start-up, it will replace " +
	    				"the data in the dataset with the new data found in the file(s).",
	    		null,
	    		SWT.NONE
	    );
	    
	    setControl(composite);

		}

	}
	
	public Delete_OntologyDatasetWizard(int mode, Delete_OntologyDataset startingDataset, DatabasePart databasePart) {
		this.mode = mode;
		this.databasePart = databasePart;
		this.startingDataset = startingDataset;
		this.connection = databasePart.getConnection();
		resetModel();
	}

	/**
	 * Optionally set a DatabasePart object which will be used as the base.
	 * If this is not set, we will create a new connection afresh.
	 * @param databasePart
	 */
//	public void setModelPart(ModelPart modelPart) {
//		//this.modelPart  = modelPart;
//		this.startingModel = modelPart.getRdbModel();
//	}

	@Override
	public void addPages() {		
		OntologyDatasetInfoPage datasetInfoPage = new OntologyDatasetInfoPage("Ontology Dataset Info");
		addPage(datasetInfoPage);
		
		//TODO add description field
//		NameDescriptionPage nameDescriptionPage = new NameDescriptionPage(sampler, "Name and Description", null);
//		addPage(nameDescriptionPage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		dataset.setId(datasetIdText.getText());
		if (dataset.getId() == null || dataset.getId().length() == 0) {
			MessageDialog.openWarning(parent.getShell(), "Please enter a value for Dataset ID", "");
			return false;
		}
		
		Persister persister = Persister.getInstance();
		
		//if (databasePart.hasModelNamed(dataset.getId())) {
		if (persister.externalDatasetExists(dataset.getId())) {
			MessageDialog.openInformation(parent.getShell(), "Dataset name already exists", 
					"This database already has a dataset named '" + dataset.getId() + "'.\nPlease choose a different name.");
			return false;
		}
		DBConnector connector = new DBConnector(connection);
		boolean connectionSucceeds = connector.testConnection();
		
		if (! connectionSucceeds) {
			MessageDialog.openInformation(parent.getShell(), "Connection Fails", 
					"Unable to connect to this database.  Cannot create new dataset in this database.");
			return true;
		}

		persister.persist(dataset); 
		log.info("Saved dataset Dataset=" + JenabeanWriter.toString(dataset));
		if (this.mode == DatasetWizardAction.MODE_NEW || this.mode == DatasetWizardAction.MODE_CLONE) {
			Model newModel = persister.createDBModel(connection, dataset.getId());
			//persister.persist(dataset, newModel, false);
			log.info("Created new model " + newModel);
			databasePart.fireUpdate(databasePart);
		} else if (this.mode == DatasetWizardAction.MODE_EDIT) {
			databasePart.fireUpdatePart();
		}
		
		//next load the model with any files located there.
		
		
		//close wizard regardless
		return true;
	}
	
	public final void resetModel() {
		if (mode == DatasetWizardAction.MODE_EDIT) {
			dataset = startingDataset.createReplica();
		//} else if (mode == DatasetWizardAction.MODE_CLONE) {
		} else {
			dataset = startingDataset.createClone();
//		} else {
//			dataset = new Dataset();
//			dataset.setConnection(this.connection);
		}
		assert(dataset != null);
	}

	public String getFilePath() {
		if (filePathTextField == null) return null;
		return filePathTextField.getValue();
	}
}

  /*
  // Initiating the realm once sets the default session Realm
  if( Realm.getDefault() == null ) {
    SWTObservables.getRealm( Display.getCurrent() );
  }
  // Strategy to convert elements from list widget to model
  UpdateListStrategy targetToModel = new UpdateListStrategy();
  targetToModel.setConverter( new StringToLocaleConverter() );
  // Strategy to convert elements from model to list widget
  UpdateListStrategy modelToTarget = new UpdateListStrategy();
  modelToTarget.setConverter( new LocaleToStringConverter() );
  // 
  DataBindingContext context = new DataBindingContext();
  Realm realm = Realm.getDefault();
  IObservableList observedModel 
    = BeansObservables.observeList( realm, model, "locales", Locale.class );
  context.bindList( SWTObservables.observeItems( list ), 
                    observedModel, 
                    targetToModel,
                    modelToTarget );
     */
