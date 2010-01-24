package org.inqle.test.data;

import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Bean2RDF;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class SimpleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//get a Model
		AppInfo appInfo = AppInfoProvider.getAppInfo();
		Persister persister = Persister.getInstance(appInfo);
		Model repositoryModel = persister.getMetarepositoryModel();
		System.out.println("BEFORE: Repository model has " + repositoryModel.size() + " statements");
		
		//save a Jenabean to the model
		System.out.println("Saving this jenabean to the model:" + JenabeanWriter.toString(appInfo));
		Bean2RDF writer = new Bean2RDF(repositoryModel);
		writer.save(appInfo);
		System.out.println("AFTER: Repository model has " + repositoryModel.size() + " statements");
	}

}
