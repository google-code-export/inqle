package org.inqle.experiment.rapidminer;

import org.inqle.core.extensions.util.IExtensionSpec;

public class RapidMinerExperimentFactory {

	public static IRapidMinerExperiment createRapidMinerExperiment(IExtensionSpec extensionSpec) {
		RapidMinerExperiment experiment = new RapidMinerExperiment();
		experiment.setId(extensionSpec.getAttribute(IRapidMinerExperiment.ID_ATTRIBUTE));
		experiment.setExperimentClassPath(extensionSpec.getAttribute(IRapidMinerExperiment.FILE_ATTRIBUTE));
		experiment.setName(extensionSpec.getAttribute(IRapidMinerExperiment.FILE_ATTRIBUTE));
		experiment.setDescription(extensionSpec.getAttribute(IRapidMinerExperiment.DESCRIPTION_ATTRIBUTE));
		experiment.setExperimentType(extensionSpec.getAttribute(IRapidMinerExperiment.TYPE_ATTRIBUTE));
		return experiment;
	}
}
