/**
 * 
 */
package org.inqle.ui.rap.actions;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.ExternalDataset;
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
public class DatasetWizard extends Wizard {

	public static final String DEFAULT_CHECKED_ATTRIBUTE = "checkedByDefault";
	private Connection connection = null;
	//private Persister persister;
	static Logger log = Logger.getLogger(DatasetWizard.class);
	
	int mode;

	private DatabasePart databasePart = null;
	//private ModelPart modelPart;
	private ExternalDataset startingDataset;
	private ExternalDataset dataset;
//	private Text datasetIdText;
	Composite parent;
	public List<Button> datasetFunctionCheckboxes = new ArrayList<Button>();
	private DatasetInfoPage datasetInfoPage;
	private DatasetFunctionsPage datasetFunctionsPage;
	
	/**
	 * This generates the wizard page for creating a database connection
	 * @author David Donohue
	 * Feb 8, 2008
	 */
	public class DatasetInfoPage extends WizardPage {

		private Composite composite;
		private TextFieldShower datasetIdTextField;
		private TextFieldShower datasetDescriptionTextField;
		
		DatasetInfoPage(String pageName) {
			super(pageName);
		}
		
		public void createControl(Composite parentComposite) {
			parent = parentComposite;
			log.trace("DatasetInfoPage.createControl()");
//			parent = pageParent;
			
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
			
			//cannot change the ID once created
			int textFieldStyle = SWT.BORDER;
			if (mode != DatasetWizardAction.MODE_NEW) {
				textFieldStyle = textFieldStyle | SWT.READ_ONLY;
			}
			
			datasetIdTextField = new TextFieldShower(
					composite,
					"Dataset ID",
					"Must be a unique name for this server, and must not contain spaces or special characters.",
					null,
					textFieldStyle
			);
			if (mode != DatasetWizardAction.MODE_NEW && dataset!=null && dataset.getId() != null) {
				datasetIdTextField.setTextValue(dataset.getId());
			}
//			new Label (composite, SWT.NONE).setText("Dataset ID (must be a unique name)");	
//	    datasetIdText = new Text(composite, SWT.BORDER);
//	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//	    datasetIdText.setLayoutData(gridData);
			
			datasetDescriptionTextField = new TextFieldShower(
					composite,
					"Description",
					"Describe the use of this dataset.",
					null,
					SWT.MULTI | SWT.BORDER
			);
			if (dataset!=null && dataset.getDescription() != null) {
				datasetDescriptionTextField.setTextValue(dataset.getDescription());
			}
			
	    setControl(composite);

		}
		
		String getDatasetId() {
			return datasetIdTextField.getValue();
		}
		
		String getDatasetDescription() {
			return datasetDescriptionTextField.getValue();
		}

	}
	
	public class DatasetFunctionsPage extends WizardPage {

		private static final String PAGE_NAME = "Dataset Functions";
		private static final String PAGE_DESCRIPTION = "Select which functions this dataset will fulfill.";
		private Composite composite;
		
		protected DatasetFunctionsPage() {
			super(PAGE_NAME);
			setMessage(PAGE_DESCRIPTION);
		}

		public void createControl(Composite parent) {
			
			composite = new Composite(parent, SWT.NONE);
	    // create the desired layout for this wizard page
			GridLayout gl = new GridLayout(2, false);
			composite.setLayout(gl);
	    
	    List<IExtensionSpec> datasetFunctionExtensions = ExtensionFactory.getExtensionSpecs(Persister.EXTENSION_POINT_DATASET_FUNCTIONS);
//	    log.info("Got datasetFunctionExtensions of size: " + datasetFunctionExtensions.size());
	    //add the checkboxes
			for (IExtensionSpec datasetFunctionExtension: datasetFunctionExtensions) {
				addDatasetFunctionCheckbox(datasetFunctionExtension, composite);
			}
			setControl(composite);
		}

		private void addDatasetFunctionCheckbox(IExtensionSpec datasetFunctionExtension, Composite composite) {
//			log.info("addDatasetFunctionCheckbox()...");
			Button checkbox = new Button(composite, SWT.CHECK);
			String extensionId = datasetFunctionExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
			String extensionName = datasetFunctionExtension.getAttribute(InqleInfo.NAME_ATTRIBUTE);
			String extensionDescription = datasetFunctionExtension.getAttribute(InqleInfo.DESCRIPTION_ATTRIBUTE);
			checkbox.setText(extensionName);
			checkbox.setData(datasetFunctionExtension);
			
			Text descriptionText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
			descriptionText.setText(extensionDescription);
			GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			descriptionText.setLayoutData(gridData);
			String defaultCheckedVal = datasetFunctionExtension.getAttribute(DEFAULT_CHECKED_ATTRIBUTE);
			boolean defaultChecked = false;
			if (defaultCheckedVal != null && defaultCheckedVal.toLowerCase().equals("true")) {
				defaultChecked = true;
			}
			Collection<String> datasetFunctions = dataset.getDatasetFunctions();
			log.info("Compare dataset function: " + extensionId + " to PRE-SELECTED functions: " + datasetFunctions);
			if (datasetFunctions != null && datasetFunctions.contains(extensionId)) {
				checkbox.setSelection(true);
			} else if (datasetFunctions==null && defaultChecked) {
				checkbox.setSelection(true);
			}
//			log.info("Add checkbox:" + extensionId + "...");
			datasetFunctionCheckboxes.add(checkbox);
		}
	}
	
	public DatasetWizard(int mode, ExternalDataset startingDataset, DatabasePart databasePart) {
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
//		log.info("Dataset Wizard: adding DatasetInfoPage...");
		datasetInfoPage = new DatasetInfoPage("Dataset Info");
		addPage(datasetInfoPage);
		
//		log.info("Dataset Wizard: creating DatasetFunctionsPage...");
		datasetFunctionsPage = new DatasetFunctionsPage();
//		log.info("Dataset Wizard: adding DatasetFunctionsPage...");
		addPage(datasetFunctionsPage);
	}
	
	
	@Override
	public boolean canFinish() {
		if (this.getContainer().getCurrentPage().equals(datasetFunctionsPage)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean performFinish() {
//		dataset.setId(datasetIdText.getText());
		dataset.setId(datasetInfoPage.getDatasetId());
		dataset.setDescription(datasetInfoPage.getDatasetDescription());
		if (dataset.getId() == null || dataset.getId().length() == 0) {
			MessageDialog.openWarning(parent.getShell(), "Please enter a value for Dataset ID", "");
			return false;
		}
		
		Persister persister = Persister.getInstance();
		
		if (mode != DatasetWizardAction.MODE_EDIT && persister.datasetExists(dataset.getId())) {
			MessageDialog.openInformation(parent.getShell(), "Dataset name already exists", 
					"This database already has a dataset named '" + dataset.getId() + "'.\nPlease choose a different name.");
			return false;
		}
		
		//get the dataset functions assigned to this dataset
		List<String> datasetFunctionIds = new ArrayList<String>();
		for (Button checkbox: datasetFunctionCheckboxes) {
			if (! checkbox.getSelection()) continue;
			IExtensionSpec datasetFunctionSpec = (IExtensionSpec)checkbox.getData();
			String datasetFunctionId = datasetFunctionSpec.getAttribute(InqleInfo.ID_ATTRIBUTE);
			log.info("adding to dataset: function " + datasetFunctionId);
			datasetFunctionIds.add(datasetFunctionId);
		}
		if (datasetFunctionIds.size() > 0) {
			dataset.setDatasetFunctions(datasetFunctionIds);
		} else {
			dataset.setDatasetFunctions(null);
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
