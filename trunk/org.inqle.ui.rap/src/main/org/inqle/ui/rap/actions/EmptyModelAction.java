/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.Jenamodel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.tree.parts.ModelPart;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class EmptyModelAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister;
	private Jenamodel namedModelToEmpty = null;
	private ModelPart modelPart = null;
	
	private static final Logger log = Logger.getLogger(EmptyModelAction.class);
	
	public EmptyModelAction(String menuText, ModelPart modelPart, IWorkbenchWindow window) {
		this.window = window;
		this.menuText = menuText;
		this.modelPart = modelPart;
		this.namedModelToEmpty  = modelPart.getDataset();
		//this.persister = persister;
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void run() {
		boolean deleteObject = false;
		
		if (this.modelPart instanceof IPartType) {
			IPartType thisPartType = (IPartType)modelPart;
//			if (thisPartType.hasChildren()) {
//				MessageDialog.openInformation(window.getShell(), "Unable to delete", "Please remove all child objects before deleting this datamodel");
//				return;
//			}
		}
		if (namedModelToEmpty != null) {
			deleteObject = MessageDialog.openConfirm(window.getShell(), "Clear this database", "Are you sure you want to delete all data in this datamodel\n'" + modelPart.getName() + "'?\nTHIS CANNOT BE UNDONE!");
		}
		if (deleteObject) {
			Persister persister = Persister.getInstance();
			persister.emptyModel(namedModelToEmpty);
//			IPartType parentPart = modelPart.getParent();
//			parentPart.fireUpdate(parentPart);
		}
	}
}
