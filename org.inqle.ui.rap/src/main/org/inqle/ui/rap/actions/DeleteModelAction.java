/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.tree.parts.ModelPart;

/**
 * @author David Donohue
 * Feb 8, 2008
 * TODO make deleting models work
 */
@Deprecated
public class DeleteModelAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister;
	private Datamodel namedModelToDelete = null;
	private ModelPart modelPart = null;
	
	private static final Logger log = Logger.getLogger(DeleteModelAction.class);
	
	public DeleteModelAction(String menuText, ModelPart modelPart, IWorkbenchWindow window) {
		this.window = window;
		this.menuText = menuText;
		this.modelPart = modelPart;
		this.namedModelToDelete  = modelPart.getDataset();
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
			if (thisPartType.hasChildren()) {
				MessageDialog.openInformation(window.getShell(), "Unable to delete", "Please remove all child objects before deleting this datamodel");
				return;
			}
		}
		if (namedModelToDelete != null) {
			deleteObject = MessageDialog.openConfirm(window.getShell(), "Delete this database", "Are you sure you want to delete datamodel\n'" + modelPart.getName() + "'?\nTHIS CANNOT BE UNDONE!");
		}
		if (deleteObject) {
//			Persister persister = Persister.getInstance();
//			TODO: get deleting models to work, when TDB supports it
//			persister.deleteModel(namedModelToDelete);
			IPartType parentPart = modelPart.getParent();
			parentPart.fireUpdate(parentPart);
		}
	}
}
