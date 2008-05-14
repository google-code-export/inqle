/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.RDF;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;

/**
 * @author David Donohue
 * Apr 18, 2008
 */
public class LearningCycleParts extends PartType {

	private Logger log = Logger.getLogger(LearningCycleParts.class);

	private boolean childrenIntialized = false;

	private List<LearningCyclePart> childParts = new ArrayList<LearningCyclePart>();
	
	private static final String ICON_PATH = "org/inqle/experiment/images/learning_cycle.gif";
	
	//this part contains the base learning cycle
	private LearningCycle learningCycle = new LearningCycle();
	
	@Override
	public IPart[] getChildren() {
		if (! this.childrenIntialized ) {
			initChildren();
		}
		LearningCyclePart[] nullPart = {};
		if (childParts.size() == 0) {
			log.debug("No customizations found.");
			return nullPart;
		}
		return childParts.toArray(nullPart);
	}
	
	public void initChildren() {
		childParts = new ArrayList<LearningCyclePart>();
		for (LearningCycle learningCycle: LearningCycleLister.listCustomizedLearningCycles(persister)) {
			if (learningCycle == null) {
				log.warn("Found Learning Cycle that is null; skipping");
			}
			LearningCyclePart childPart = new LearningCyclePart(learningCycle);
			childPart.setParent(this);
			childPart.setPersister(persister);
			childPart.addListener(listener);
			childParts.add(childPart);
		}
		this.childrenIntialized = true;
	}

	/**
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	@Override
	public String getName() {
		return "Learning Cycle";
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}
	
	@Override
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
		LearningCycleWizardAction newExperimentWizardAction = 
			new LearningCycleWizardAction(
					"Create a new customized learning cycle...", 
					workbenchWindow, 
					persister);
		newExperimentWizardAction.setPartToRefresh(this);
		manager.add(newExperimentWizardAction);
	}
}
