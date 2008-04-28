package org.inqle.experiment.rapidminer;

import java.util.List;

import org.inqle.data.rdf.jenabean.Persister;

public class LearningCycleLister {

	@SuppressWarnings("unchecked")
	public static List<LearningCycle> listLearningCycles(Persister persister) {
		return (List<LearningCycle>)persister.reconstituteList(LearningCycle.class);
	}
}
