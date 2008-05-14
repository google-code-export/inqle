package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.inqle.data.rdf.jenabean.Persister;

public class LearningCycleLister {

	public static List<LearningCycle> listCustomizedLearningCycles(Persister persister) {
		assert(persister != null);
		List<LearningCycle> learningCycles = new ArrayList<LearningCycle>();
		
		//add any customized LearningCycle objects
		for (Object lcObject: persister.reconstituteAll(LearningCycle.class)) {
			LearningCycle learningCycle = (LearningCycle)lcObject;
			learningCycle.setPersister(persister);
			learningCycles.add(learningCycle);
		}
		return learningCycles;
	}
	
	public static List<LearningCycle> listAllLearningCycles(Persister persister) {
		List<LearningCycle> learningCycles = new ArrayList<LearningCycle>();
		//add the base (uncustomized) LearningCycle
		LearningCycle baseLC = new LearningCycle();
		baseLC.setPersister(persister);
		learningCycles.add(baseLC);
		learningCycles.addAll(listCustomizedLearningCycles(persister));
		return learningCycles;
	}
}
