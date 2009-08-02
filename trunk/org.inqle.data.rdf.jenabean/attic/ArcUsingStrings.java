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
	public class ArcUsingStrings extends GlobalCloneableJenabean implements Comparable<ArcUsingStrings> {
		private List<String> arcStepList = new ArrayList<String>();
		
//		private transient Object value;

		public void addArcStep(String arcStep) {
			arcStepList.add(arcStep);
		}

		public void setArcSteps(String[] arcSteps) {
			this.arcStepList = new ArrayList<String>();
			if (arcSteps==null) return;
			for (String arcStep: arcSteps) {
				arcStepList.add(arcStep);
			}
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
			int i=0;
			for (String arcStep: arcStepList) {
				if (i>0) s += " --> ";
				s += arcStep;
				i++;
			}
			s += "}";
			return s;
		}

		public String getQNameRepresentation() {
			UriMapper uriMapper = UriMapper.getInstance();
			String s = "";
			if (arcStepList.size() == 1) {
				String uri = arcStepList.get(0).toString();
				return uriMapper.getQname(uri);
			}
			int i=0;
			for (String arcStep: arcStepList) {
				if (i>0) s += " -> ";
				s += uriMapper.getQname(arcStep);
				i++;
			}
			return s;
		}
		
		public int compareTo(ArcUsingStrings otherArc) {
			return this.toString().compareTo(otherArc.toString());
		}
		
//		@Override
//		public int hashCode() {
//			return this.toString().hashCode();
//		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof ArcUsingStrings)) return false;
			ArcUsingStrings otherArc = (ArcUsingStrings)o;
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