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
	 * 
	 * As of 10/31/08, switching this to use String to represent the predicate of each step.
	 * Was using the ArcStep class, but this gave Jenabean related errors.
	 * So now Arcs only support outgoing direction
	 */
	@Namespace(RDF.INQLE)
	public class ArcUsingStrings extends GlobalJenabean {
		private List<String> arcStepList = new ArrayList<String>();
		
//		private transient Object value;

		public void addArcStep(String arcStep) {
			arcStepList.add(arcStep);
		}

		public void setArcSteps(String[] arcSteps) {
			this.arcStepList = Arrays.asList(arcSteps);
		}
		
		public String[] getArcSteps() {
			String[] arcStepArr = new String[] {};
			return arcStepList.toArray(arcStepArr);
		}
		
		public void clone(ArcUsingStrings objectToBeCloned) {
			setArcSteps(objectToBeCloned.getArcSteps());
			super.clone(this);
		}
		
		public ArcUsingStrings createClone() {
			ArcUsingStrings arc = new ArcUsingStrings();
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
			for (String arcStep: arcStepList) {
				s += arcStep + " --> ";
			}
			s += "}";
			return s;
		}

//		public Object getValue() {
//			return value;
//		}
//
//		public void setValue(Object value) {
//			this.value = value;
//		}
	}