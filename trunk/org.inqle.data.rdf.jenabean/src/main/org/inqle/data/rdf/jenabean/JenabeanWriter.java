package org.inqle.data.rdf.jenabean;

import java.io.ByteArrayOutputStream;

import org.inqle.data.rdf.jena.load.Loader;

import thewebsemantic.Bean2RDF;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenabeanWriter {

	private static final String DEFAULT_RDF_LANG = Loader.LANG_N3;

	public static String toString(Object bean) {
		return toString(bean, DEFAULT_RDF_LANG);	
	}
	
	public static String toString(Object bean, String lang) {
		OntModel model = ModelFactory.createOntologyModel();
		Bean2RDF writer = new Bean2RDF(model);
		writer.saveDeep(bean);
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		model.write(outputStream, lang);
		String beanString = modelToString(model, lang);
		return beanString;
	}
	
	public static String modelToString(Model model) {
		return modelToString(model, DEFAULT_RDF_LANG);	
	}
	
	public static String modelToString(Model model, String lang) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		model.write(outputStream, lang);
		return outputStream.toString();
	}
}
