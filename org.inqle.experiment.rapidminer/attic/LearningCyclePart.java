package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.ui.rap.Part;

public class LearningCyclePart extends Part {

	private Logger log = Logger.getLogger(LearningCyclePart.class);
	private static final String ICON_PATH = "org/inqle/experiment/images/learning_cycle.gif";
	private LearningCycle learningCycle;

	public LearningCyclePart(LearningCycle learningCycle) {
		this.learningCycle = learningCycle;
	}
	
	/**
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	@Override
	public String getName() {
		if (learningCycle == null || learningCycle.getName() == null) {
			return "Customized Learning Cycle";
		}
		return learningCycle.getName();
	}
	
	@Override
//public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		List<IAction> actions = new ArrayList<IAction>();
		LearningCycleWizardAction editLCWizardAction = 
			new LearningCycleWizardAction(
					"Edit this learning cycle...", 
					workbenchWindow);
		editLCWizardAction.setPartToRefresh(this);
		editLCWizardAction.setLearningCycle(learningCycle);
		actions.add(editLCWizardAction);
		
		DeleteLearningCycleAction deleteLCAction = new DeleteLearningCycleAction("Delete", this, workbenchWindow);
		actions.add(deleteLCAction);
		
		return actions;
	}

	public LearningCycle getLearningCycle() {
		return learningCycle;
	}

	@Override
	public Object getObject() {
		return learningCycle;
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}
}
