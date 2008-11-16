package org.inqle.data.rdf.jenabean;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.uri.UriMapper;

import thewebsemantic.Namespace;


/**
 * This class represents an individual step in an Arc
 * @author David Donohue
 * Feb 22, 2008
 */
@Namespace(RDF.INQLE)
public class ArcStep extends GlobalJenabean {
	public static final int OUTGOING = 0;
	public static final int INCOMING = 1;
	
	private String predicate;
	private int stepType = OUTGOING;

	public ArcStep() {
	}
	
	public ArcStep(String predicate) {
		this.predicate = predicate;
	}
	
//		public ArcStep(String predicate, RDFNode object, int stepType) {
//			this.predicate = predicate;
//			this.object = object;
//			this.stepType  = stepType;
//		}

	public String getPredicate() {
		return predicate;
	}

//		public RDFNode getObject() {
//			return object;
//		}

	public int getStepType() {
		return stepType;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public void setStepType(int stepType) {
		this.stepType = stepType;
	}

	public void clone(ArcStep objectToClone) {
		setStepType(objectToClone.getStepType());
		setPredicate(objectToClone.getPredicate());
//			setObject(objectToClone.getObject());
		super.clone(objectToClone);
	}
	
	public ArcStep createClone() {
		ArcStep newArcStep = new ArcStep();
		newArcStep.clone(this);
		return newArcStep;
	}
	
	public String getStringRepresentation() {
		String s = "";
		if (stepType == INCOMING) {
			s += "<--";
		} else {
			s += "-->";
		}
		s += "<" + predicate + ">";
		return s;
	}

	public String getQnameRepresentation() {
		UriMapper uriMapper = UriMapper.getInstance();
		String s = "";
		if (stepType == INCOMING) {
			s += "<-";
		} else {
			s += "->";
		}
		s += uriMapper.getQname(predicate);
		return s;
	}
	
}