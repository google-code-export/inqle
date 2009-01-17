package org.inqle.experiment.rapidminer;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPartType;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class DeleteLearningCycleAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister;
	private LearningCycle learningCycleToDelete = null;
	private LearningCyclePart learningCyclePart = null;
	
	private static final Logger log = Logger.getLogger(DeleteLearningCycleAction.class);
			
	public DeleteLearningCycleAction(String menuText, LearningCyclePart learningCyclePart, IWorkbenchWindow window) {
		this.window = window;
		this.menuText = menuText;
		this.learningCyclePart = learningCyclePart;
		this.learningCycleToDelete  = learningCyclePart.getLearningCycle();
		//this.persister = persister;
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void run() {
		boolean confirmDelete = false;
		
		if (learningCycleToDelete != null) {
			confirmDelete = MessageDialog.openConfirm(window.getShell(), "Delete this Learning Cycle", "Are you sure you want to delete Learning Cycle\n'" + learningCycleToDelete.getName() + "'?\nTHIS CANNOT BE UNDONE!");
		}
		if (confirmDelete) {
			Persister persister = Persister.getInstance();
			persister.remove(learningCycleToDelete);
			IPartType parentPart = learningCyclePart.getParent();
			parentPart.fireUpdate(parentPart);
		}
	}
}
