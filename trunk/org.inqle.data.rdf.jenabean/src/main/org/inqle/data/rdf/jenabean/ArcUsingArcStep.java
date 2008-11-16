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
	 * 
	 * This version represents steps using the ArcStep object, which supports incoming &
	 * outgoing direction.  Not currently in use due to a bug in Jenabean
	 * Feb 22, 2008
	 */
	@Namespace(RDF.INQLE)
	public class ArcUsingArcStep extends GlobalJenabean {
		private List<ArcStep> arcStepList = new ArrayList<ArcStep>();
		
//		private transient Object value;

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
		
		public void clone(ArcUsingArcStep objectToBeCloned) {
			setArcSteps(objectToBeCloned.getArcSteps());
			super.clone(this);
		}
		
		public ArcUsingArcStep createClone() {
			ArcUsingArcStep arc = new ArcUsingArcStep();
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

		public String getQNameRepresentation() {
			UriMapper uriMapper = UriMapper.getInstance();
			String s = "";
			if (arcStepList.size() == 1) {
				String uri = arcStepList.get(0).toString();
				return uriMapper.getQname(uri);
			}
			for (ArcStep arcStep: arcStepList) {
				s += arcStep.getQnameRepresentation();
			}
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