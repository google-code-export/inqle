package org.inqle.ui.rap.wiki.parts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.LocalFolderDatabase;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.UserDatamodel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;
import org.inqle.ui.rap.actions.DatabaseWizardAction;

/**
 * @author David Donohue
 * Feb 4, 2008
 */
public class Wikis extends PartType {

	private static final String ICON_PATH = "org/inqle/ui/rap/images/wikis.gif";

	public static final String DATAMODEL_FUNCTION_WIKI = "org.inqle.datamodelFunctions.wiki";

	private List<WikiPart> wikiList = new ArrayList<WikiPart>();

	private boolean childrenIntialized = false;

	private IDatabase baseDatabaseObject = new LocalFolderDatabase();;
	
	static Logger log = Logger.getLogger(Wikis.class);
	
	public void initChildren() {
		Persister persister = Persister.getInstance();
		List<UserDatamodel> wikiDatamodels = persister.listUserDatamodelsOfFunction(DATAMODEL_FUNCTION_WIKI);
		for (UserDatamodel datamodel: wikiDatamodels) {
			WikiPart wikiPart = new WikiPart(datamodel);
			wikiPart.setParent(this);
			wikiPart.addListener(this.listener);
			wikiList.add(wikiPart);
		}
		this.childrenIntialized = true;
	}
	
	@Override
	public IPart[] getChildren() {
		if (! this.childrenIntialized) {
			initChildren();
		}
		WikiPart[] nullPart = {};
		if (wikiList.size() == 0) {
			//log.info("No SDBDatabase objects found.");
			return nullPart;
		}
		//log.info("Found these SDBDatabase objects:" + wikiList);
		return wikiList.toArray(nullPart);
	}
	
	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.IPart#getPartName()
	 */
	@Override
	public String getName() {
		return "Wiki";
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
