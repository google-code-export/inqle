package org.inqle.data.rdf.jenabean;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.uri.UriMapper;

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
	public class Arc extends GlobalJenabean implements Comparable<Arc> {
		private List<ArcStep> arcStepList = new ArrayList<ArcStep>();
		
//		private transient Object value;

		public void addArcStep(ArcStep arcStep) {
			arcStepList.add(arcStep);
		}

		public void setArcSteps(ArcStep[] arcSteps) {
			this.arcStepList = new ArrayList<ArcStep>();
			if (arcSteps==null) return;
			for (ArcStep arcStep: arcSteps) {
				arcStepList.add(arcStep);
			}
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
				s += arcStep.toString();
			}
			s += "}";
			return s;
		}

		public String getQNameRepresentation() {
//			UriMapper uriMapper = UriMapper.getInstance();
			String s = "";
//			if (arcStepList.size() == 1) {
//				String uri = arcStepList.get(0).toString();
//				return uriMapper.getQname(uri);
//			}
			for (ArcStep arcStep: arcStepList) {
				s += arcStep.getQNameRepresentation();
			}
			return s;
		}
		
		public int compareTo(Arc otherArc) {
			return this.toString().compareTo(otherArc.toString());
		}
		
//		@Override
//		public int hashCode() {
//			return this.toString().hashCode();
//		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Arc)) return false;
			Arc otherArc = (Arc)o;
			return this.toString().equals(otherArc.toString());
		}

		@Override
		public String toString() {
			return getQNameRepresentation();
		}
//		public Object getValue() {
//			return value;
//		}
//
//		public void setValue(Object value) {
//			this.value = value;
//		}
	}