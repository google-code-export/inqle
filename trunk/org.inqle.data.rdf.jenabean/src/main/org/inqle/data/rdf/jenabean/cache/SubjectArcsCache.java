package org.inqle.data.rdf.jenabean.cache;

import java.net.URI;
import java.util.Collection;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.AJenabean;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.TargetModelName;

import thewebsemantic.Namespace;

@TargetModelName(Persister.DATAMODEL_ARCS_CACHE)
@Namespace(RDF.INQLE)
public class SubjectArcsCache extends AJenabean {

	private String datamodelId;
	private URI subjectClass;
	private Collection<Arc> arcs;
	private int depth;
	private String type;

//	public String getStringRepresentation() {
//		String s = getClass().toString() + " {\n";
//		s += "[type=" + type + "]\n";
//		s += "[depth=" + depth + "]\n";
//		s += "[datamodelId=" + datamodelId + "]\n";
//		s += "[subjectClass=" + subjectClass.toString() + "]\n";
//		s += "[arcs=" + arcs.toString() + "]\n";
//		s += "}";
//		return s;
//	}
	
//	public void clone(SubjectArcsCache objectToBeCloned) {
//		setSubjectClass(objectToBeCloned.getSubjectClass());
//		setDatamodelId(objectToBeCloned.getDatamodelId());
//		setArcs(objectToBeCloned.getArcs());
//		setType(objectToBeCloned.getType());
//		setDepth(objectToBeCloned.getDepth());
//		super.clone(objectToBeCloned);
//	}

	public Collection<Arc> getArcs() {
		return arcs;
	}

	public void setSubjectClass(URI subjectClass) {
		this.subjectClass = subjectClass;
	}

	public URI getSubjectClass() {
		return subjectClass;
	}

	public void setDatamodelId(String datamodelId) {
		this.datamodelId = datamodelId;
	}

	public String getDatamodelId() {
		return datamodelId;
	}

//	public SubjectArcsCache createClone() {
//		SubjectArcsCache subjectArcsCache = new SubjectArcsCache();
//		subjectArcsCache.clone(this);
//		return subjectArcsCache;
//	}

	public void setArcs(Collection<Arc> arcs) {
		this.arcs = arcs;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
