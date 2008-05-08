package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.inqle.data.rdf.jenabean.Persister;

public class LearningCycleLister {

	public static List<LearningCycle> listLearningCycles(Persister persister) {
		assert(persister != null);
		List<LearningCycle> learningCycles = new ArrayList<LearningCycle>();
		//add the base (uncustomized) LearningCycle
		LearningCycle baseLC = new LearningCycle();
		baseLC.setPersister(persister);
		learningCycles.add(baseLC);
		
		//add any customized LearningCycle objects
		for (Object lcObject: persister.reconstituteList(LearningCycle.class)) {
			LearningCycle learningCycle = (LearningCycle)lcObject;
			learningCycle.setPersister(persister);
			learningCycles.add(learningCycle);
		}
		return learningCycles;
	}
}
