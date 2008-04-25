/**
 * 
 */
package org.inqle.ui.rap.actions;


import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.sdb.DBConnector;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.tree.parts.DatabasePart;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class DatabaseWizard extends Wizard {

	
	private Connection startingConnection = null;
	private Connection connection = null;
	private Persister persister;
	static Logger log = Logger.getLogger(DatabaseWizard.class);
	Composite composite;
	int mode;
	
	Shell shell;
	private IPartType parentPart = null;
	private IPart databasePart = null;
	
	public DatabaseWizard(int mode, IPartType parentPart,	Persister persister, Shell parentShell) {
		this.mode = mode;
		this.parentPart = parentPart;
		this.persister = persister;
		this.shell = parentShell;
	}




	/**
	 * Optionally set a DatabasePart object which will be used as the base.
	 * If this is not set, we will create a new connection afresh.
	 * @param databasePart
	 */
	public void setDatabasePart(DatabasePart databasePart) {
		this.databasePart  = databasePart;
		this.startingConnection = databasePart.getConnection();
	}
	


	
	@Override
	public void addPages() {
		resetConnection();
		
		ConnectionPage connectionPage = new ConnectionPage("Database Connection Info", connection, shell);
		addPage((IWizardPage) connectionPage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		DBConnector connector = new DBConnector(connection);
		boolean connectionSucceeds = connector.testConnection();
		boolean saveConnection = true;
		
		if (! connectionSucceeds) {
			saveConnection = MessageDialog.openConfirm(shell.getShell(), "Connection Fails", "Unable to connect to this database.  Save it anyway?");
		}
		if (saveConnection) {
			if (this.mode == DatabaseWizardAction.MODE_NEW || this.mode == DatabaseWizardAction.MODE_CLONE) {
				persister.createNewDBConnection(connection);
				parentPart.fireUpdate(parentPart);
			} else if (this.mode == DatabaseWizardAction.MODE_EDIT) {
				persister.persist(connection, persister.getMetarepositoryModel());
				databasePart.fireUpdatePart();
			}
		}
		//close wizard regardless
		return true;
	}

	/**
	 * Retrieve the connection object to edit in the ConnectionPage
	 * @return
	 */
	public final void resetConnection() {
		if (mode == DatabaseWizardAction.MODE_EDIT) {
			connection = startingConnection.createReplica();
		} else if (mode == DatabaseWizardAction.MODE_CLONE) {
			connection = startingConnection.createClone();
		} else {
			connection = new Connection();
		}
		assert(connection != null);
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
