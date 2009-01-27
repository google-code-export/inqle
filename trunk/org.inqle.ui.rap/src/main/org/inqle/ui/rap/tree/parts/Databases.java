/**
 * 
 */
package org.inqle.ui.rap.tree.parts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.BasicDatabase;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;
import org.inqle.ui.rap.actions.SDBDatabaseWizardAction;

/**
 * @author David Donohue
 * Feb 4, 2008
 */
public class Databases extends PartType {

	private static final String ICON_PATH = "org/inqle/ui/rap/images/monitor.gif";

	private static final Class<?> DATABASE_CLASS = BasicDatabase.class;

	private List<DatabasePart> dbList = new ArrayList<DatabasePart>();

	private List<Connection> connections = new ArrayList<Connection>();

	private boolean childrenIntialized = false;

	private Class<?> databaseClass = DATABASE_CLASS;
	
	static Logger log = Logger.getLogger(Databases.class);
	
	public void initChildren() {
		Persister persister = Persister.getInstance();
		IDBConnector connector = DBConnectorFactory.getDBConnector();
		List<String> databaseIds = connector.listDatabases();
		List<IDatabase> databases = new ArrayList<IDatabase>();
		for (String databaseId: databaseIds) {
			persister.reconstitute(databaseClass, databaseId, true);
		}
//		Collection<?> connectionObjects = persister.reconstituteAll(Connection.class);
//		for (Object connectionObject: connectionObjects) {
//			connections.add((Connection)connectionObject);
//		}
		for (Connection connection: connections) {
			DatabasePart dbPart = new DatabasePart(connection);
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
			//log.info("No Connection objects found.");
			return nullDBPart;
		}
		//log.info("Found these Connection objects:" + dbList);
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
		SDBDatabaseWizardAction sDBDatabaseWizardAction = new SDBDatabaseWizardAction(SDBDatabaseWizardAction.MODE_NEW, "Create new database....", this, workbenchWindow);
		actions.add(sDBDatabaseWizardAction);
		return actions;
	}
}
