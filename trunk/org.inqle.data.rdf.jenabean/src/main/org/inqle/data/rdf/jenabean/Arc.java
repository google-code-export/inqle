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
		
		public Arc() {}
		
		public Arc(ArcStep firstArcStep) {
			addArcStep(firstArcStep);
		}
		
		public Arc(String firstArcStep) {
			addArcStep(firstArcStep);
		}

		public void addArcStep(ArcStep arcStep) {
			arcStepList.add(arcStep);
		}
		
		public void addArcStep(String arcStepString) {
			arcStepList.add(new ArcStep(arcStepString));
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
			setQnameRepresentation(objectToBeCloned.getQnameRepresentation());
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

		/**
		 * Get shortest representation of this Arc, using QNames where prefix abbreviation is available
		 * @return
		 */
		public String getQnameRepresentation() {
//			UriMapper uriMapper = UriMapper.getInstance();
			String s = "";
//			if (arcStepList.size() == 1) {
//				String uri = arcStepList.get(0).toString();
//				return uriMapper.getQname(uri);
//			}
			for (ArcStep arcStep: arcStepList) {
				s += arcStep.getQnameRepresentation();
			}
			return s;
		}
		
		/**
		 * Add the set method, to make the qnameRepresentation field into a Javabean field,
		 * permitting Jenabean to persist it to RDF store.
		 * @param stringRepresentation
		 */
		public void setQnameRepresentation(String stringRepresentation) {
			//do nothing, let the QName representation be generated from the fields
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
			return this.getStringRepresentation().equals(otherArc.getStringRepresentation());
		}

		@Override
		public String toString() {
			return getQnameRepresentation();
		}
//		public Object getValue() {
//			return value;
//		}
//
//		public void setValue(Object value) {
//			this.value = value;
//		}
	}