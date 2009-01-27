/**
 * 
 */
package org.inqle.ui.rap.actions;


import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.sdb.SDBConnector;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.pages.ConnectionPage;
import org.inqle.ui.rap.tree.parts.DatabasePart;

/**
 * This class creates a new SDB Database
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class SDBDatabaseWizard extends Wizard {

	
	private Connection startingConnection = null;
	private Connection database = null;
	//private Persister persister;
	static Logger log = Logger.getLogger(SDBDatabaseWizard.class);
	Composite composite;
	int mode;
	
	Shell shell;
	private IPartType parentPart = null;
	private IPart databasePart = null;
	
	public SDBDatabaseWizard(int mode, IPartType parentPart, Shell parentShell) {
		this.mode = mode;
		this.parentPart = parentPart;
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
		this.startingConnection = (Connection) databasePart.getDatabase();
		resetConnection();
	}
	


	
	@Override
	public void addPages() {
		
		resetConnection();
		log.info("addPages() using Connection:\n" + JenabeanWriter.toString(database));
		ConnectionPage connectionPage = new ConnectionPage("Database Connection Info", database, shell);
		addPage(connectionPage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		Persister persister = Persister.getInstance();
//		SDBConnector connector = new SDBConnector(database);
		IDBConnector connector = DBConnectorFactory.getDBConnector(database);
		boolean connectionSucceeds = connector.testConnection();
		boolean confirmSave = true;
		
		if (! connectionSucceeds) {
			confirmSave = MessageDialog.openConfirm(shell.getShell(), "Connection Fails", "Unable to connect to this database.  Save it anyway?");
		}
		if (confirmSave) {
			if (this.mode == SDBDatabaseWizardAction.MODE_NEW || this.mode == SDBDatabaseWizardAction.MODE_CLONE) {
				persister.createNewDBConnection(database);
				parentPart.fireUpdate(parentPart);
			} else if (this.mode == SDBDatabaseWizardAction.MODE_EDIT) {
				persister.persist(database);
				databasePart.fireUpdatePart();
			}
		}
		//close wizard regardless
		return true;
	}

	/**
	 * Retrieve the database object to edit in the ConnectionPage
	 * @return
	 */
	public final void resetConnection() {
		if (mode == SDBDatabaseWizardAction.MODE_EDIT) {
			database = startingConnection.createReplica();
		} else if (mode == SDBDatabaseWizardAction.MODE_CLONE) {
			database = startingConnection.createClone();
		} else {
			database = new Connection().createClone();
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
