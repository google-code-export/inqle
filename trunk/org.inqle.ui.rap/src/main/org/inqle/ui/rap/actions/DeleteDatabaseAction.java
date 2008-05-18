/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.tree.parts.DatabasePart;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class DeleteDatabaseAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister;
	private Connection connectionToDelete = null;
	private IPart databasePart = null;
	
	private static final Logger log = Logger.getLogger(DeleteDatabaseAction.class);
	
	public DeleteDatabaseAction(String menuText, DatabasePart databasePart, IWorkbenchWindow window) {
		this.window = window;
		this.menuText = menuText;
		this.databasePart = databasePart;
		this.connectionToDelete  = databasePart.getConnection();
		//this.persister = persister;
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void runWithEvent(Event event) {
		boolean deleteObject = false;
		
		if (this.databasePart instanceof IPartType) {
			IPartType thisPartType = (IPartType) databasePart;
			if (thisPartType.hasChildren()) {
				MessageDialog.openInformation(window.getShell(), "Unable to delete", "Please remove all child objects before deleting this Database");
				return;
			}
		}
		if (connectionToDelete != null) {
			deleteObject = MessageDialog.openConfirm(window.getShell(), "Delete this database", "Are you sure you want to delete database\n'" + databasePart.getName() + "'?\nTHIS CANNOT BE UNDONE!");
		}
		if (deleteObject) {
			Persister persister = Persister.getInstance();
			persister.deleteConnection(connectionToDelete);
			IPartType parentPart = databasePart.getParent();
			parentPart.fireUpdate(parentPart);
		}
	}
}
