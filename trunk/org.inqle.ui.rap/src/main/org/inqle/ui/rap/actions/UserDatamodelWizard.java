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
import org.eclipse.swt.widgets.Text;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.UserDatamodel;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.tree.parts.DatabasePart;
import org.inqle.ui.rap.widgets.TextFieldShower;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Creates a new datamodel, in the specified IDatabase.  Supports 
 * any type of IDatabase
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 * 
 * TODO extend DynaWizard instead of Wizard
 */
public class UserDatamodelWizard extends Wizard {

	public static final String DEFAULT_CHECKED_ATTRIBUTE = "checkedByDefault";
	private IDatabase database = null;
	//private Persister persister;
	static Logger log = Logger.getLogger(UserDatamodelWizard.class);
	
	int mode;

	private DatabasePart databasePart = null;
	//private ModelPart modelPart;
	private UserDatamodel startingDatamodel;
	private UserDatamodel datamodel;
//	private Text datasetIdText;
	Composite parent;
	public List<Button> datasetFunctionCheckboxes = new ArrayList<Button>();
	private DatamodelInfoPage datasetInfoPage;
	private DatamodelFunctionsPage datasetFunctionsPage;
	
	/**
	 * This generates the wizard page for creating a database database
	 * @author David Donohue
	 * Feb 8, 2008
	 */
	public class DatamodelInfoPage extends WizardPage {

		private Composite composite;
		private TextFieldShower datasetIdTextField;
		private TextFieldShower datasetDescriptionTextField;
		
		DatamodelInfoPage(String pageName) {
			super(pageName);
		}
		
		public void createControl(Composite parentComposite) {
			parent = parentComposite;
			log.trace("DatamodelInfoPage.createControl()");
//			parent = pageParent;
			
			//initialize the Datamodel to the base starting Datamodel
			resetModel();
			
			composite = new Composite(parent, SWT.NONE);
	    // create the desired layout for this wizard page
			GridLayout gl = new GridLayout(2, false);
			composite.setLayout(gl);
			
			//cannot change the ID once created
			int textFieldStyle = SWT.BORDER;
			if (mode != DatamodelWizardAction.MODE_NEW) {
				textFieldStyle = textFieldStyle | SWT.READ_ONLY;
			}
			
			datasetIdTextField = new TextFieldShower(
					composite,
					"Datamodel ID",
					"Must be a unique name for this server, and must not contain spaces or special characters.",
					null,
					textFieldStyle
			);
			if (mode != DatamodelWizardAction.MODE_NEW && datamodel!=null && datamodel.getId() != null) {
				datasetIdTextField.setTextValue(datamodel.getId());
			}
			
			datasetDescriptionTextField = new TextFieldShower(
					composite,
					"Description",
					"Describe the use of this datamodel.",
					null,
					SWT.MULTI | SWT.BORDER
			);
			if (datamodel!=null && datamodel.getDescription() != null) {
				datasetDescriptionTextField.setTextValue(datamodel.getDescription());
			}
			
	    setControl(composite);

		}
		
		String getDatamodelId() {
			return datasetIdTextField.getValue();
		}
		
		String getDatamodelDescription() {
			return datasetDescriptionTextField.getValue();
		}

	}
	
	public class DatamodelFunctionsPage extends WizardPage {

		private static final String PAGE_NAME = "Datamodel Functions";
		private static final String PAGE_DESCRIPTION = "Select which functions this datamodel will fulfill.";
		private Composite composite;
		
		protected DatamodelFunctionsPage() {
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
				addDatamodelFunctionCheckbox(datasetFunctionExtension, composite);
			}
			setControl(composite);
		}

		private void addDatamodelFunctionCheckbox(IExtensionSpec datasetFunctionExtension, Composite composite) {
//			log.info("addDatamodelFunctionCheckbox()...");
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
			
			Collection<String> datasetFunctions = datamodel.getDatamodelFunctions();
//			log.info("Compare datamodel function: " + extensionId + " to PRE-SELECTED functions: " + datasetFunctions);
//			log.info("Default checked=" + defaultChecked);
			if (datasetFunctions != null && datasetFunctions.contains(extensionId)) {
				checkbox.setSelection(true);
			} else if ((datasetFunctions==null || datasetFunctions.size()==0) && defaultChecked) {
				checkbox.setSelection(true);
			}
//			log.info("Add checkbox:" + extensionId + "...");
			datasetFunctionCheckboxes.add(checkbox);
		}
	}
	
	public UserDatamodelWizard(int mode, UserDatamodel startingDatamodel, DatabasePart databasePart) {
		this.mode = mode;
		this.databasePart = databasePart;
		this.startingDatamodel = startingDatamodel;
		this.database = databasePart.getDatabase();
		resetModel();
	}

	/**
	 * Optionally set a DatabasePart object which will be used as the base.
	 * If this is not set, we will create a new database afresh.
	 * @param databasePart
	 */
//	public void setModelPart(ModelPart modelPart) {
//		//this.modelPart  = modelPart;
//		this.startingModel = modelPart.getRdbModel();
//	}

	@Override
	public void addPages() {		
//		log.info("Datamodel Wizard: adding DatamodelInfoPage...");
		datasetInfoPage = new DatamodelInfoPage("Datamodel Info");
		addPage(datasetInfoPage);
		
//		log.info("Datamodel Wizard: creating DatamodelFunctionsPage...");
		datasetFunctionsPage = new DatamodelFunctionsPage();
//		log.info("Datamodel Wizard: adding DatamodelFunctionsPage...");
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
//		datamodel.setId(datasetIdText.getText());
		datamodel.setId(datasetInfoPage.getDatamodelId());
		datamodel.setDescription(datasetInfoPage.getDatamodelDescription());
		if (datamodel.getId() == null || datamodel.getId().length() == 0) {
			MessageDialog.openWarning(parent.getShell(), "Please enter a value for Datamodel ID", "");
			return false;
		}
		
		Persister persister = Persister.getInstance();
		
		if (mode != DatamodelWizardAction.MODE_EDIT && persister.userDatamodelExists(datamodel.getId())) {
			MessageDialog.openInformation(parent.getShell(), "Datamodel name already exists", 
					"This database already has a datamodel named '" + datamodel.getId() + "'.\nPlease choose a different name.");
			return false;
		}
		
		//get the datamodel functions assigned to this datamodel
		List<String> datasetFunctionIds = new ArrayList<String>();
		for (Button checkbox: datasetFunctionCheckboxes) {
			if (! checkbox.getSelection()) continue;
			IExtensionSpec datasetFunctionSpec = (IExtensionSpec)checkbox.getData();
			String datasetFunctionId = datasetFunctionSpec.getAttribute(InqleInfo.ID_ATTRIBUTE);
			log.info("adding to datamodel: function " + datasetFunctionId);
			datasetFunctionIds.add(datasetFunctionId);
		}
		if (datasetFunctionIds.size() > 0) {
			datamodel.setDatamodelFunctions(datasetFunctionIds);
		} else {
			datamodel.setDatamodelFunctions(null);
		}
		
		IDBConnector connector = DBConnectorFactory.getDBConnector(database);
		boolean connectionSucceeds = connector.testConnection();
		
		if (! connectionSucceeds) {
			MessageDialog.openInformation(parent.getShell(), "SDBDatabase Fails", 
					"Unable to connect to this database.  Cannot create new datamodel in this database.");
			return true;
		}

//		persister.persist(datamodel); 
//		log.info("Saved datamodel Datamodel=" + JenabeanWriter.toString(datamodel));
		if (this.mode == DatamodelWizardAction.MODE_NEW || this.mode == DatamodelWizardAction.MODE_CLONE) {
//			Persister.createDBModel(database, datamodel.getId());
			//persister.persist(datamodel, newModel, false);
			persister.createDatabaseBackedModel(datamodel);
			log.info("Created new model ");
			databasePart.fireUpdate(databasePart);
		} else if (this.mode == DatamodelWizardAction.MODE_EDIT) {
			persister.persist(datamodel);
			databasePart.fireUpdatePart();
		}
		//close wizard regardless
		return true;
	}
	
	public final void resetModel() {
		if (mode == DatamodelWizardAction.MODE_EDIT) {
			datamodel = startingDatamodel.createReplica();
		//} else if (mode == DatamodelWizardAction.MODE_CLONE) {
		} else {
			datamodel = startingDatamodel.createClone();
//		} else {
//			datamodel = new Datamodel();
//			datamodel.setConnection(this.connection);
		}
		assert(datamodel != null);
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
