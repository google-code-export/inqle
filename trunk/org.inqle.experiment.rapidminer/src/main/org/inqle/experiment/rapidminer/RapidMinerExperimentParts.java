/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;

/**
 * @author David Donohue
 * Apr 14, 2008
 */
public class RapidMinerExperimentParts extends PartType {

	private Logger log = Logger.getLogger(RapidMinerExperimentParts.class);
	
	private static final String ICON_PATH = "org/inqle/experiment/images/experiments.jpeg";
	/* 
	 * 
	 * The children = the list of all IRapidMinerExperiment plugins
	 * add to each child a reference to the Persister object,
	 * such that the entire tree may use common database connections
	 * @see org.inqle.ui.rap.IPartType#getChildren()
	 */
	public IPart[] getChildren() {
		List<IExtensionSpec> extensionSpecs =  ExtensionFactory.getExtensionSpecs(IRapidMinerExperiment.ID);
		IPart[] nullIPartArr = new IPart[] {};
		if (extensionSpecs == null) {
			return nullIPartArr;
		}
		List<IPart> parts = new ArrayList<IPart>();
		for (IExtensionSpec extensionSpec: extensionSpecs) {
			if (extensionSpec == null) continue;
			IRapidMinerExperiment experiment = RapidMinerExperimentFactory.createRapidMinerExperiment(extensionSpec);
			RapidMinerExperimentPart part = new RapidMinerExperimentPart(experiment);
			
			//part.setPersister(persister);
			part.addListener(this.listener);
			parts.add(part);
		}
		IPart[] ipartArr = parts.toArray(nullIPartArr);
		
		return ipartArr;
	}

	/**
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	public String getName() {
		return "RapidMiner Experiments";
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}
	
}
