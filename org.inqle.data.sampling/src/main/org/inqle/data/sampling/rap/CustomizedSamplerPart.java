/**
 * 
 */
package org.inqle.data.sampling.rap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.ui.rap.IPart;

/**
 * This class represents a customization
 * @author David Donohue
 * Feb 28, 2008
 */
public class CustomizedSamplerPart extends SamplerPart {

	public CustomizedSamplerPart(ISamplerFactory samplerFactory) {
		super(samplerFactory);
	}
	
	static Logger log = Logger.getLogger(CustomizedSamplerPart.class);

	@Override
	public IPart[] getChildren() {
		CustomizedSamplerPart[] nullPart = {};
		return nullPart;
	}
	
	@Override
//public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		List<IAction> actions = super.getActions(workbenchWindow);
		
		if (!samplerFactory.hasWizard()) {
			return actions;
		}
		
		//"Open this Sampler" action.  This wizard works with a replica of the base sampler
		//log.info("CustomizedSamplerPart.addActions()...");
		SamplerWizardAction editSamplerWizardAction = new SamplerWizardAction(SamplerWizardAction.MODE_OPEN, "Edit this sampler...", this, workbenchWindow);
		actions.add(editSamplerWizardAction);
		
		//Delete action
		//ISampler replicaOfSampler = samplerFactory.replicateSampler();
		log.debug("Created replica of sampler:" + JenabeanWriter.toString(samplerFactory.getStartingSampler()));
		DeleteSamplerAction deleteSamplerAction = new DeleteSamplerAction("Delete", this, workbenchWindow);
		actions.add(deleteSamplerAction);
		
		return actions;
	}
	
}
