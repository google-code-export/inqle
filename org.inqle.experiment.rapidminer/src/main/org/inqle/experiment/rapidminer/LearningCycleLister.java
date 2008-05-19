package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.inqle.data.rdf.jenabean.Persister;

public class LearningCycleLister {

	//public static List<LearningCycle> listCustomizedLearningCycles(Persister persister) {
	public static List<LearningCycle> listCustomizedLearningCycles() {
		//assert(persister != null);
		Persister persister = Persister.getInstance();
		List<LearningCycle> learningCycles = new ArrayList<LearningCycle>();
		
		//add any customized LearningCycle objects
		for (Object lcObject: persister.reconstituteAll(LearningCycle.class)) {
			LearningCycle learningCycle = (LearningCycle)lcObject;
			//learningCycle.setPersister(persister);
			learningCycles.add(learningCycle);
		}
		return learningCycles;
	}
	
	public static List<LearningCycle> listAllLearningCycles() {
		List<LearningCycle> learningCycles = new ArrayList<LearningCycle>();
		//add the base (uncustomized) LearningCycle
		LearningCycle baseLC = new LearningCycle();
		learningCycles.add(baseLC);
		//add all customized learning cycles
		learningCycles.addAll(listCustomizedLearningCycles());
		return learningCycles;
	}
}
