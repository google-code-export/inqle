package org.inqle.data.rdf.jenabean;

import java.io.ByteArrayOutputStream;

import thewebsemantic.Bean2RDF;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenabeanWriter {

	public static String toString(Object bean) {
		return toString(bean, "N3");	
	}
	
	public static String toString(Object bean, String lang) {
		OntModel model = ModelFactory.createOntologyModel();
		Bean2RDF writer = new Bean2RDF(model);
		writer.saveDeep(bean);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		model.write(outputStream, lang);
		String beanString = outputStream.toString();
		return beanString;
	}
}
