/**
 * 
 */
package org.inqle.data.sampling.rap;

import org.apache.log4j.Logger;
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
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
		super.addActions(manager, workbenchWindow);
		
		if (!samplerFactory.hasWizard()) {
			return;
		}
		
		//"Open this Sampler" action.  This wizard works with a replica of the base sampler
		//log.info("CustomizedSamplerPart.addActions()...");
		SamplerWizardAction editSamplerWizardAction = new SamplerWizardAction(SamplerWizardAction.MODE_OPEN, "Edit this sampler...", this, workbenchWindow);
		manager.add(editSamplerWizardAction);
		
		//Delete action
		//ISampler replicaOfSampler = samplerFactory.replicateSampler();
		log.debug("Created replica of sampler:" + JenabeanWriter.toString(samplerFactory.getBaseSampler()));
		DeleteSamplerAction deleteSamplerAction = new DeleteSamplerAction("Delete", this, workbenchWindow);
		manager.add(deleteSamplerAction);
	}
	
}
