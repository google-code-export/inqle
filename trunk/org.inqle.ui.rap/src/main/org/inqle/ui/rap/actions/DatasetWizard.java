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
import org.inqle.data.rdf.jena.sdb.DBConnector;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.tree.parts.DatabasePart;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 * 
 * TODO extend DynaWizard instead of Wizard
 */
public class DatasetWizard extends Wizard {

	private Connection connection = null;
	//private Persister persister;
	static Logger log = Logger.getLogger(DatasetWizard.class);
	Composite composite;
	int mode;

	Composite parent;
	private DatabasePart databasePart = null;
	//private ModelPart modelPart;
	private Dataset startingDataset;
	private Dataset dataset;
	private Text datasetIdText;
	
	/**
	 * This generates the wizard page for creating a database connection
	 * @author David Donohue
	 * Feb 8, 2008
	 */
	public class DatasetInfoPage extends WizardPage {

		DatasetInfoPage(String pageName) {
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
			
	    /*
	    Button testConnection = new Button(composite, SWT.PUSH);
	    testConnection.setText("Test Connection");

	    testConnection.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					showTestConnectionDialog();
				}
			});
	    */
	    
	    /* TODO get refresh button to work
	    Button resetConnection = new Button(composite, SWT.PUSH);
	    resetConnection.setText("Reset");
	    resetConnection.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					resetConnection();
					composite.redraw();
				}
			});
	    */
	    
	    //add data binding
	    // Initiating the realm once sets the default session Realm
//	    if( Realm.getDefault() == null ) {
//	      SWTObservables.getRealm( Display.getCurrent() );
//	    }
//	    Realm realm = Realm.getDefault();
//	    
//	    DataBindingContext bindingContext = new DataBindingContext();
//	    
//	    //TODO change BeansObservables to PojoObservables, when available:  http://fire-change-event.blogspot.com/2007/10/getting-rid-of-those-pesky-could-not.html
//	    IObservableValue modelNameObserveWidget = SWTObservables.observeText(datasetIdText, SWT.FocusOut);
//			IObservableValue modelNameObserveValue = BeansObservables.observeValue(realm, dataset, "id");
//			bindingContext.bindValue(modelNameObserveWidget, modelNameObserveValue, null, null);
	    
	    setControl(composite);

		}

	}
	
	public DatasetWizard(int mode, Dataset startingModel, DatabasePart databasePart) {
		this.mode = mode;
		this.databasePart = databasePart;
		this.startingDataset = startingModel;
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
		DatasetInfoPage datasetInfoPage = new DatasetInfoPage("Dataset Info");
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
		if (persister.datasetExists(dataset.getId())) {
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

		persister.persist(dataset, persister.getMetarepositoryModel()); 
		log.info("Saved dataset Dataset=" + JenabeanWriter.toString(dataset));
		if (this.mode == DatasetWizardAction.MODE_NEW || this.mode == DatasetWizardAction.MODE_CLONE) {
			Model newModel = persister.createDBModel(connection, dataset.getId());
			//persister.persist(dataset, newModel, false);
			log.info("Created new model " + newModel);
			databasePart.fireUpdate(databasePart);
		} else if (this.mode == DatasetWizardAction.MODE_EDIT) {
			databasePart.fireUpdatePart();
		}
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
