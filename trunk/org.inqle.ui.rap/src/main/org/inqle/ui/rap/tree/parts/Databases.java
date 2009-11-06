/**
 * 
 */
package org.inqle.ui.rap.tree.parts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.LocalFolderDatabase;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;
import org.inqle.ui.rap.actions.DatabaseWizardAction;

/**
 * @author David Donohue
 * Feb 4, 2008
 */
public class Databases extends PartType {

	private static final String ICON_PATH = "org/inqle/ui/rap/images/monitor.gif";

//	private static final Class<?> DATABASE_CLASS = LocalFolderDatabase.class;

	private List<DatabasePart> dbList = new ArrayList<DatabasePart>();

//	private List<SDBDatabase> connections = new ArrayList<SDBDatabase>();

	private boolean childrenIntialized = false;

	private LocalFolderDatabase baseDatabaseObject = new LocalFolderDatabase();
	
	static Logger log = Logger.getLogger(Databases.class);
	
	public void initChildren() {
		Persister persister = Persister.getInstance();
		IDBConnector connector = DBConnectorFactory.getDBConnector();
		List<String> databaseIds = connector.listDatabases();
		List<IDatabase> databases = new ArrayList<IDatabase>();
		for (String databaseId: databaseIds) {
			String targetDatamodelId = Persister.getTargetDatamodelId(baseDatabaseObject.getClass(), databaseId);
			IDatabase database = persister.reconstitute(baseDatabaseObject.getClass(), databaseId, targetDatamodelId, true);
			//if the database object is not stored in the metarepository, create it
			if (database==null) {
				database = new LocalFolderDatabase();
				database.setId(databaseId);
			}
			databases.add(database);
		}
//		Collection<?> connectionObjects = persister.reconstituteAll(SDBDatabase.class);
//		for (Object connectionObject: connectionObjects) {
//			connections.add((SDBDatabase)connectionObject);
//		}
//		for (SDBDatabase connection: connections) {
//			DatabasePart dbPart = new DatabasePart(connection);
//			dbPart.setParent(this);
//			dbPart.addListener(this.listener);
//			dbList.add(dbPart);
//			//log.info("Added DatabasePart " + dbPart.getName());
//		}
		
		for (IDatabase database: databases) {
			DatabasePart dbPart = new DatabasePart(database);
			dbPart.setParent(this);
			dbPart.addListener(this.listener);
			dbList.add(dbPart);
			//log.info("Added DatabasePart " + dbPart.getName());
		}
		this.childrenIntialized = true;
	}
	
	@Override
	public IPart[] getChildren() {
		if (! this.childrenIntialized) {
			initChildren();
		}
		DatabasePart[] nullDBPart = {};
		if (dbList.size() == 0) {
			//log.info("No SDBDatabase objects found.");
			return nullDBPart;
		}
		//log.info("Found these SDBDatabase objects:" + dbList);
		return dbList.toArray(nullDBPart);
	}
	
	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.IPart#getPartName()
	 */
	@Override
	public String getName() {
		return "Databases";
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}
	
	@Override
//public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		List<IAction> actions = new ArrayList<IAction>();
//		DatabaseWizardAction databaseWizardAction = new DatabaseWizardAction(DatabaseWizardAction.MODE_NEW, "Create new database....", baseDatabaseObject, this, workbenchWindow);
//		actions.add(databaseWizardAction);
		return actions;
	}
}
