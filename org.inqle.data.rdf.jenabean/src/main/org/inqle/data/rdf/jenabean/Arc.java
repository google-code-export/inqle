package org.inqle.data.rdf.jenabean;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.RDFNode;


/**
	 * This class represents an individual Arc
	 * @author David Donohue
	 * Feb 22, 2008
	 */
	public class Arc {
		private List<ArcStep> arcSteps = new ArrayList<ArcStep>();
		public Arc() {
		}
		public Arc(String predicate, RDFNode object) {
			addArcStep(new ArcStep(predicate, object));
		}
		public void addArcStep(ArcStep arcStep) {
			arcSteps.add(arcStep);
		}

		public List<ArcStep> getArcSteps() {
			return arcSteps;
		}
	}