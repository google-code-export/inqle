/**
 * 
 */
package org.inqle.experiment.rapidminer;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.actions.DynaWizardDialog;

/**
 * @author David Donohue
 * Apr 15, 2008
 */
public class LearningCycleWizardAction extends Action {

	public static int NEW = 0;
	public static int EDIT = 1;
	
	private IWorkbenchWindow window;
	private String menuText;
	//private Persister persister;
	//private int mode = NEW;
	private LearningCycle learningCycle;
	
	private static Logger log = Logger.getLogger(LearningCycleWizardAction.class);
	private IPart partToRefresh;
	
	public LearningCycleWizardAction(String menuText,	IWorkbenchWindow workbenchWindow) {
		//this.mode = mode;
		this.window = workbenchWindow;
		this.menuText = menuText;
		//this.persister = persister;
		learningCycle = new LearningCycle();
		//learningCycle.setPersister(persister);
		//initialize a unique id now, so we can see an ID in logging
		learningCycle.getId();
	}
	
	@Override
	public String getText() {
		return menuText;
	}

	
	@Override
	public void run() {
		LearningCycleWizard learningCycleWizard = new LearningCycleWizard(learningCycle, null, window.getShell());
		if (partToRefresh != null) {
			learningCycleWizard.setPart(partToRefresh);
		}
		DynaWizardDialog dialog = new DynaWizardDialog(window.getShell(), learningCycleWizard);
		dialog.open();
	}

	public void setLearningCycle(LearningCycle learningCycle) {
		this.learningCycle = learningCycle.createReplica();
	}

	public IPart getPartToRefresh() {
		return partToRefresh;
	}

	public void setPartToRefresh(IPart partToRefresh) {
		this.partToRefresh = partToRefresh;
	}

}


























