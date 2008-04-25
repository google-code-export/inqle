/**
 * 
 */
package org.inqle.experiment.rapidminer;

import org.inqle.ui.rap.Part;

/**
 * @author David Donohue
 * Apr 14, 2008
 */
public class RapidMinerExperimentPart extends Part {

	private IRapidMinerExperiment rapidMinerExperiment;

	private static final String ICON_PATH = "org/inqle/experiment/images/experiment.jpeg";
	
	public RapidMinerExperimentPart(IRapidMinerExperiment experiment) {
		assert(experiment != null);
		this.rapidMinerExperiment = experiment;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	@Override
	public String getName() {
		return rapidMinerExperiment.getName();
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}
	
}
