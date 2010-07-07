package org.inqle.data.rdf.jenabean.util;

import java.io.ByteArrayOutputStream;

import org.inqle.data.rdf.jena.load.Loader;
import org.inqle.rdf.RDF;

import thewebsemantic.Bean2RDF;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.PrintUtil;

public class JenabeanWriter {

	public static final String DEFAULT_RDF_LANG = Loader.LANG_N3;

	public static String toString(Object bean) {
		return toString(bean, DEFAULT_RDF_LANG);	
	}
	
	public static String toString(Object bean, String lang) {
		if (bean==null) return "[NULL]";
		registerPrefixes();

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
		registerPrefixes();
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		model.write(outputStream, lang);
		return outputStream.toString();
	}
	
	public static void registerPrefixes() {
		PrintUtil.registerPrefix("inqle", RDF.INQLE);
		PrintUtil.registerPrefix("xsd", RDF.XSD);
	}
}