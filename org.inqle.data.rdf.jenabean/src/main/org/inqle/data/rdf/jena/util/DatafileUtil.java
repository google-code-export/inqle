package org.inqle.data.rdf.jena.util;

import java.io.File;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.Datafile;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
/**
 * Adds Datafiles to a 
 * @author David Donohue
 * Aug 11, 2008
 */
public class DatafileUtil {

	private static Logger log = Logger.getLogger(DatafileUtil.class);

	/**
	 * Adds all files present in the specified folder path to the 
	 * provided QueryCriteria.
	 * Adds any files in any subfolders as well.
	 * @param folderPath
	 */
	public static void addDatafiles(QueryCriteria queryCriteria, String folderPath) {
		File folder = new File(folderPath);
		for (File file: folder.listFiles()) {
			if (file.isDirectory()) {
				addDatafiles(queryCriteria, file.getAbsolutePath());
			} else {
				Datafile datafile = new Datafile();
				datafile.setFileUrl(file.getAbsolutePath());
				queryCriteria.addNamedModel(datafile);
			}
		}
	}
	
	/**
	 * Gets an OntModel, containing all data files contained in the specified 
	 * folder path or in any subfolders.
	 * @param folderPath
	 * @return
	 */
	public static OntModel getOntModel(String folderPath) {
		OntModel ontModel = ModelFactory.createOntologyModel();
		addModel(ontModel, folderPath);
		return ontModel;
	}
	
	/**
	 * Gets an Model, containing all data files contained in the specified 
	 * folder path or in any subfolders.
	 * @param folderPath
	 * @return
	 */
	public static Model getModel(String folderPath) {
		Model model = ModelFactory.createDefaultModel();
		addModel(model, folderPath);
		return model;
	}
	
	/**
	 * Gets a Jena Model, for all files inside the specified folder path
	 */
	public static void addModel(Model model, String folderPath) {
		File folder = new File(folderPath);
		for (File file: folder.listFiles()) {
			if (file.isDirectory()) {
				addModel(model, file.getAbsolutePath());
			} else {
				log.info("Getting model: " + file.getAbsolutePath());
				model.add(Persister.getModelFromFile(file.getAbsolutePath()));
			}
		}
	}
}
