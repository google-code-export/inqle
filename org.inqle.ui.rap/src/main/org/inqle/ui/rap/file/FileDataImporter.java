package org.inqle.ui.rap.file;

import java.net.URI;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.mapping.*;
import org.inqle.ui.rap.csv.CsvReader;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class FileDataImporter {

	private CsvReader csvReader;
	private TableMapping tableMapping;
	private OntModel ontModel;

	private OntClass dataSuperClass;
	private static Logger log = Logger.getLogger(FileDataImporter.class);
	public FileDataImporter(CsvReader csvReader, TableMapping tableMapping, OntModel ontModel) {
		this.csvReader = csvReader;
		this.tableMapping = tableMapping;
		this.ontModel = ontModel;
		
		dataSuperClass = ontModel.createClass(RDF.DATA);
	}
	
	public void doImport() {
		ontModel.begin();
		OntClass tableDataClass = ontModel.createClass(RDF.randomInstanceUri(RDF.DATA));
		tableDataClass.setSuperClass(dataSuperClass);
		
		//TODO add date and other top level data (place, investigator)
		
		//first import the SubjectMappings which are 
		for (SubjectMapping subjectMapping: tableMapping.getSubjectMappings()) {
			//if the subject mapping identifies an individual, import values into this instance
			if (subjectMapping.getSubjectInstance() != null) {
				Individual dataInstance = ontModel.createIndividual(RDF.randomInstanceUri(RDF.DATA), tableDataClass);
				importInstanceSubjectMapping(subjectMapping, dataInstance);
			}
		}
		
		//next loop thru each row and create subject, add values from mapping and from appropriate column
		ontModel.commit();
	}

	/**
	 * do all data import for a single subject instance
	 * 
	 * TODO add date info to each tableClass or rowInstance
	 */
	private void importInstanceSubjectMapping(SubjectMapping subjectMapping, Individual dataInstance) {
		
		String subjectInstanceUri = subjectMapping.getSubjectInstance().toString();
	
		Individual subjectInstance = ontModel.createIndividual(
				subjectInstanceUri,
				ResourceFactory.createResource(subjectMapping.getSubjectClass().toString()));
		
		dataInstance.addProperty(ResourceFactory.createProperty(RDF.HAS_SUBJECT), subjectInstance);
//		subjectInstance.addProperty(ResourceFactory.createProperty(RDF.HAS_DATA), dataInstance);
	
		importValues(subjectMapping, subjectInstance, dataInstance);
		
		//TODO loop thru each row and add values from appropriate column to appropriate individual
	}
	
	private void importValues(SubjectMapping subjectMapping, Individual subjectInstance, Individual dataInstance) {
		for (DataMapping dataMapping: subjectMapping.getDataMappings()) {
			if (dataMapping.getMapsValue() == null || dataMapping.getMapsValue().toString().trim().length()==0) {
				continue;
			}
			URI propertyType = dataMapping.getMapsPropertyType();
			if (URI.create(RDF.SUBJECT_PROPERTY).equals(propertyType)) {
				//import into the subject instance
				importValue(dataMapping, subjectInstance);
			} else {
				//import into the data table class
				importValue(dataMapping, dataInstance);
			}
		}
	}

	//TODO when generating the mapping, create an object of the appropriate type &/or store the property type as a field
	private void importValue(DataMapping dataMapping, OntResource individual) {
		if (dataMapping.getMapsPredicate()==null) {
			log .warn("Unable to import value '" + dataMapping.getMapsValue() + "' for individual " + individual + ". The property is null.");
		}
		Property property = ontModel.createProperty(dataMapping.getMapsPredicate().toString());
		Object value = dataMapping.getMapsValue();
		individual.addLiteral(property, value);
	}
	
}
