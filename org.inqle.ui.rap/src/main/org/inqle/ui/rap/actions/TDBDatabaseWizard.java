/**
 * 
 */
package org.inqle.ui.rap.actions;


import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.LocalFolderDatabase;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.pages.BasicNameDescriptionPage;
import org.inqle.ui.rap.pages.DatabaseInfoPage;
import org.inqle.ui.rap.tree.parts.DatabasePart;

/**
 * This class creates a new SDB Database
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class TDBDatabaseWizard extends Wizard {

	private LocalFolderDatabase startingDatabase = null;
	private LocalFolderDatabase database = null;
	private DatabaseInfoPage databaseInfoPage = null;
	//private Persister persister;
	static Logger log = Logger.getLogger(TDBDatabaseWizard.class);
	Composite composite;
	int mode;
	
	Shell shell;
	private IPartType parentPart = null;
	private IPart databasePart = null;
	
	public TDBDatabaseWizard(int mode, IPartType parentPart, DatabasePart databasePart, Shell parentShell) {
		this.mode = mode;
		this.parentPart = parentPart;
		this.databasePart = databasePart;
		//this.persister = persister;
		this.shell = parentShell;
	}




	/**
	 * Optionally set a DatabasePart object which will be used as the base.
	 * If this is not set, we will create a new database afresh.
	 * @param databasePart
	 */
	public void setDatabasePart(DatabasePart databasePart) {
		this.databasePart  = databasePart;
		this.startingDatabase = (LocalFolderDatabase) databasePart.getDatabase();
		resetConnection();
	}
	


	
	@Override
	public void addPages() {
		
		resetConnection();
		log.info("addPages() using Database:\n" + JenabeanWriter.toString(database));
		if (mode == DatabaseWizardAction.MODE_NEW) {
			databaseInfoPage = new DatabaseInfoPage("Database SDBDatabase Info", null, null);
		} else {
			databaseInfoPage = new DatabaseInfoPage("Database SDBDatabase Info", database.getName(), database.getDescription());
		}
		addPage(databaseInfoPage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		database.setName(databaseInfoPage.getName());
		
		
		Persister persister = Persister.getInstance();
//		SDBConnector connector = new SDBConnector(database);
		IDBConnector connector = DBConnectorFactory.getDBConnector(database);
		boolean connectionSucceeds = connector.testConnection();
		
		if (this.mode == DatabaseWizardAction.MODE_NEW || this.mode == DatabaseWizardAction.MODE_CLONE) {
			if (connectionSucceeds) {
				MessageDialog.openConfirm(shell.getShell(), "Unable to Create Database", "There is already a database of the same ID.");
				return false;
			} else {
//				connector.createDatabase();
				persister.createNewDatabase(database);
				parentPart.fireUpdate(parentPart);
				return true;
			}
		}
		
		//mode must = EDIT
		boolean confirmSave = true;
		
		if (! connectionSucceeds) {
			confirmSave = MessageDialog.openConfirm(shell.getShell(), "SDBDatabase Fails", "Unable to connect to this database.  Save it anyway?");
		}
		if (confirmSave) {
//			if (this.mode == DatabaseWizardAction.MODE_NEW || this.mode == DatabaseWizardAction.MODE_CLONE) {
//				persister.createNewDBConnection(database);
//				parentPart.fireUpdate(parentPart);
//			} else if (this.mode == DatabaseWizardAction.MODE_EDIT) {
				persister.persist(database);
				databasePart.fireUpdatePart();
//			}
		}
		//close wizard regardless
		return true;
	}

	/**
	 * Retrieve the database object to edit in the ConnectionPage
	 * @return
	 */
	public final void resetConnection() {
		if (mode == DatabaseWizardAction.MODE_EDIT) {
			database = startingDatabase.createReplica();
		} else if (mode == DatabaseWizardAction.MODE_CLONE) {
			database = startingDatabase.createClone();
		} else {
			database = new LocalFolderDatabase().createClone();
		}
		assert(database != null);
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
