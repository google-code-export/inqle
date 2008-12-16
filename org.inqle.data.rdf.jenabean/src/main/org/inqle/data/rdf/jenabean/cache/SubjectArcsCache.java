package org.inqle.data.rdf.jenabean.cache;

import java.net.URI;
import java.util.List;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.mapping.DataMapping;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.rdf.model.Resource;

import thewebsemantic.Namespace;

@TargetDataset(Persister.DATASET_ARCS_CACHE)
@Namespace(RDF.INQLE)
public class SubjectArcsCache extends GlobalJenabean {

	private String datasetId;
	private Resource subjectClass;
	private List<Arc> arcs;

	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[datasetId=" + datasetId + "]\n";
		s += "[subjectClass=" + subjectClass.toString() + "]\n";
		s += "[arcs=" + arcs.toString() + "]\n";
		s += "}";
		return s;
	}
	
	public void clone(SubjectArcsCache objectToBeCloned) {
		setSubjectClass(objectToBeCloned.getSubjectClass());
		setDatasetId(objectToBeCloned.getDatasetId());
		setArcs(objectToBeCloned.getArcs());
		super.clone(objectToBeCloned);
	}

	public List<Arc> getArcs() {
		return arcs;
	}

	public void setSubjectClass(Resource subjectClass) {
		this.subjectClass = subjectClass;
	}

	public Resource getSubjectClass() {
		return subjectClass;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public String getDatasetId() {
		return datasetId;
	}

	public SubjectArcsCache createClone() {
		SubjectArcsCache subjectArcsCache = new SubjectArcsCache();
		subjectArcsCache.clone(this);
		return subjectArcsCache;
	}

	public void setArcs(List<Arc> arcs) {
		this.arcs = arcs;
	}

}
