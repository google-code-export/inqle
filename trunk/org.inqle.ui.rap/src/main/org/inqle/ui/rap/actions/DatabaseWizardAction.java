/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.tree.parts.DatabasePart;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class DatabaseWizardAction extends Action {
	public static final int MODE_NEW = 0;
	public static final int MODE_EDIT = 1;
	public static final int MODE_CLONE = 2;
	private String menuText;
	
	private IWorkbenchWindow window;
	//private Persister persister = null;
	private int mode = MODE_NEW;
	private IPartType parentPart = null;
	private DatabasePart databasePart;
	private IDatabase database;
	
	private static final Logger log = Logger.getLogger(DatabaseWizardAction.class);
	
	public DatabaseWizardAction(int mode, String menuText, IDatabase database, IPartType parentPart, IWorkbenchWindow window) {
		this.mode = mode;
		this.menuText = menuText;
		this.database = database;
		this.window = window;
		this.parentPart = parentPart;
		//this.persister = persister;
	}
	
	@Override
	public String getText() {
		return menuText;
	}
	
	@Override
	public void run() {
		IWizard wizard = null;
		//MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Opening new Wizard", event.toString());
//		if (database instanceof SDBDatabase) {
//			wizard = new SDBDatabaseWizard(mode, parentPart, databasePart, window.getShell());
//			//wizard.setConnection(connection);
//		} else {
			wizard = new TDBDatabaseWizard(mode, parentPart, databasePart, window.getShell());
//		}
		
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();
	}

	public void setDatabase(IDatabase database) {
		this.database = database;
	}

	public DatabasePart getDatabasePart() {
		return databasePart;
	}

	public void setDatabasePart(DatabasePart databasePart) {
		this.databasePart = databasePart;
	}
}
