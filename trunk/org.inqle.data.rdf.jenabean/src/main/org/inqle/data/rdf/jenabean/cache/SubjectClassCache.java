package org.inqle.data.rdf.jenabean.cache;

import java.net.URI;
import java.util.Collection;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;

@TargetDataset(Persister.DATASET_SUBJECT_CLASSES_CACHE)
@Namespace(RDF.INQLE)
public class SubjectClassCache extends BasicJenabean {

	private String datasetId;
	private Collection<URI> subjectClasses;

	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[datasetId=" + datasetId + "]\n";
		s += "[subjectClasses=" + subjectClasses.toString() + "]\n";
		s += "}";
		return s;
	}
	
	public void clone(SubjectClassCache objectToBeCloned) {
		setSubjectClasses(objectToBeCloned.getSubjectClasses());
		setDatasetId(objectToBeCloned.getDatasetId());
		super.clone(objectToBeCloned);
	}

	public void setSubjectClasses(Collection<URI> subjectClasses) {
		this.subjectClasses = subjectClasses;
	}

	public Collection<URI> getSubjectClasses() {
		return subjectClasses;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public String getDatasetId() {
		return datasetId;
	}

	public SubjectClassCache createClone() {
		SubjectClassCache subjectClassCache = new SubjectClassCache();
		subjectClassCache.clone(this);
		return subjectClassCache;
	}

}
