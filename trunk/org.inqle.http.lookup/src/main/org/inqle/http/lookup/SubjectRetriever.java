package org.inqle.http.lookup;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

public class SubjectRetriever {

	private static final Logger log = Logger.getLogger(SubjectsSearcher.class);
	
	public static Model getSubject(String datamodelId, String subjectUri) {
		Persister persister = Persister.getInstance();
		Model model = persister.getModel(datamodelId);
		Resource subject = model.getResource(subjectUri);
		Model displayableModel = ModelFactory.createDefaultModel();
		displayableModel.add(subject.listProperties());
		return displayableModel;
	}
}
