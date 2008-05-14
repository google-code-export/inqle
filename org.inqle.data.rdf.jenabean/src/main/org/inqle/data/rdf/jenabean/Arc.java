package org.inqle.data.rdf.jenabean;

import org.inqle.data.rdf.RDF;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thewebsemantic.Namespace;


/**
	 * This class represents an individual Arc
	 * @author David Donohue
	 * Feb 22, 2008
	 */
	@Namespace(RDF.INQLE)
	public class Arc extends GlobalJenabean {
		private List<ArcStep> arcStepList = new ArrayList<ArcStep>();
		public Arc() {
		}
//		public Arc(String predicate, RDFNode object) {
//			addArcStep(new ArcStep(predicate, object));
//		}
		public void addArcStep(ArcStep arcStep) {
			arcStepList.add(arcStep);
		}

		public void setArcSteps(ArcStep[] arcSteps) {
			this.arcStepList = Arrays.asList(arcSteps);
		}
		
		public ArcStep[] getArcSteps() {
			ArcStep[] arcStepArr = new ArcStep[] {};
			return arcStepList.toArray(arcStepArr);
		}
		
		public void clone(Arc objectToBeCloned) {
			setArcSteps(objectToBeCloned.getArcSteps());
			super.clone(this);
		}
		
		public Arc createClone() {
			Arc arc = new Arc();
			arc.clone(this);
			return arc;
		}
		
		public String getStringRepresentation() {
			String s = "";
			if (arcStepList.size() == 1) {
				s = arcStepList.get(0).toString();
				return s;
			}
			s = "Arc: {";
			for (ArcStep arcStep: arcStepList) {
				s += arcStep + " --> ";
			}
			s += "}";
			return s;
		}
	}