/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.tree.parts.DatabasePart;
import org.inqle.data.rdf.jena.Connection;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class DatabaseWizardAction extends Action {
	public static final int MODE_NEW = 0;
	public static final int MODE_EDIT = 1;
	public static final int MODE_CLONE = 2;
	private String menuText;
	
	private String id = null;
	private IWorkbenchWindow window;
	//private Persister persister = null;
	private int mode = MODE_NEW;
	private IPartType parentPart = null;
	private IPart thisPart = null;
	private DatabasePart databasePart;
	private Connection connection;
	
	private static final Logger log = Logger.getLogger(DatabaseWizardAction.class);
	
	public DatabaseWizardAction(int mode, String menuText, IPartType parentPart, IWorkbenchWindow window) {
		this.mode = mode;
		this.menuText = menuText;
		this.parentPart = parentPart;
		this.window = window;
		//this.persister = persister;
	}
	
	/**
	 * Optionally set the DatabasePart which we are editing or cloning.
	 * If this is not set, we will create a new connection afresh.
	 * @param databasePart
	 */
	public void setDatabasePart(DatabasePart databasePart) {
		this.databasePart  = databasePart;
	}
	
	@Override
	public String getText() {
		return menuText;
	}
	
	@Override
	public void runWithEvent(Event event) {
		//MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Opening new Wizard", event.toString());
		DatabaseWizard wizard = new DatabaseWizard(mode, parentPart, window.getShell());
		//wizard.setConnection(connection);
		
		//for MODE_NEW, do not add a starting base Connection
		if (databasePart != null) {
			wizard.setDatabasePart(databasePart);
		}
		
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
