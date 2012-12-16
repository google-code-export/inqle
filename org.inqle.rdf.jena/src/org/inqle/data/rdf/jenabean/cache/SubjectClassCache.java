package org.inqle.data.rdf.jenabean.cache;

import java.net.URI;
import java.util.Collection;

import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.rdf.RDF;
import org.inqle.rdf.annotations.TargetModelName;
import org.inqle.rdf.beans.AJenabean;

import thewebsemantic.Namespace;

@TargetModelName(Persister.DATAMODEL_SUBJECT_CLASSES_CACHE)
@Namespace(RDF.INQLE)
public class SubjectClassCache extends AJenabean {

	private String datamodelId;
	private Collection<URI> subjectClasses;

	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[datamodelId=" + datamodelId + "]\n";
		s += "[subjectClasses=" + subjectClasses.toString() + "]\n";
		s += "}";
		return s;
	}
	
//	public void clone(SubjectClassCache objectToBeCloned) {
//		setSubjectClasses(objectToBeCloned.getSubjectClasses());
//		setDatasetId(objectToBeCloned.getDatasetId());
//		super.clone(objectToBeCloned);
//	}

	public void setSubjectClasses(Collection<URI> subjectClasses) {
		this.subjectClasses = subjectClasses;
	}

	public Collection<URI> getSubjectClasses() {
		return subjectClasses;
	}

	public void setDatasetId(String datamodelId) {
		this.datamodelId = datamodelId;
	}

	public String getDatasetId() {
		return datamodelId;
	}

//	public SubjectClassCache createClone() {
//		SubjectClassCache subjectClassCache = new SubjectClassCache();
//		subjectClassCache.clone(this);
//		return subjectClassCache;
//	}

}
