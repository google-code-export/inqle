package org.inqle.data.rdf.jenabean;

import com.hp.hpl.jena.rdf.model.RDFNode;


/**
	 * This class represents an individual step in an Arc
	 * @author David Donohue
	 * Feb 22, 2008
	 */
	public class ArcStep {
		public static final int OUTGOING = 0;
		public static final int INCOMING = 1;
		
		private String predicate;
		private RDFNode object;
		private int stepType = OUTGOING;

		public ArcStep(String predicate, RDFNode object) {
			this.predicate = predicate;
			this.object = object;
		}
		
		public ArcStep(String predicate, RDFNode object, int stepType) {
			this.predicate = predicate;
			this.object = object;
			this.stepType  = stepType;
		}

		public String getPredicate() {
			return predicate;
		}

		public RDFNode getObject() {
			return object;
		}

		public int getStepType() {
			return stepType;
		}
	}