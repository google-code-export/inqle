package org.inqle.experiment.rapidminer;

import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.util.InqleInfo;

public class RapidMinerExperimentFactory {

	public static IRapidMinerExperiment createRapidMinerExperiment(IExtensionSpec extensionSpec) {
		RapidMinerExperiment experiment = new RapidMinerExperiment();
		experiment.setId(extensionSpec.getAttribute(InqleInfo.ID_ATTRIBUTE));
		experiment.setExperimentClassPath(extensionSpec.getAttribute(InqleInfo.FILE_ATTRIBUTE));
		experiment.setName(extensionSpec.getAttribute(IRapidMinerExperiment.NAME_ATTRIBUTE));
		experiment.setDescription(extensionSpec.getAttribute(IRapidMinerExperiment.DESCRIPTION_ATTRIBUTE));
		experiment.setExperimentType(extensionSpec.getAttribute(InqleInfo.TYPE_ATTRIBUTE));
		return experiment;
	}
}
