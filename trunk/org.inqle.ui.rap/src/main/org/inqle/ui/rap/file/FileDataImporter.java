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
		
		for (SubjectMapping subjectMapping: tableMapping.getSubjectMappings()) {
			//if the subject mapping identifies an individual, import values into this instance
			if (subjectMapping.getSubjectInstance() != null) {
				//import the SubjectMappings which represent specific individuals
				Individual dataInstance = ontModel.createIndividual(RDF.randomInstanceUri(RDF.DATA), tableDataClass);
				importInstanceSubjectMapping(subjectMapping, dataInstance);
			} else {
				//import the SubjectMappings which represent 1 row per individual
				importRowSubjectMapping(subjectMapping, ontModel, tableDataClass);
			}
		}
		
		
		//next loop thru each row and create subject, add values from mapping and from appropriate column
		ontModel.commit();
	}

	/**
	 * import a SubjectMapping which specifyies creating new instance of inqle:Data and inqle:Subject for each row
	 * @param subjectMapping
	 * @param ontModel
	 * @param dataSuperClass
	 */
	private void importRowSubjectMapping(SubjectMapping subjectMapping, OntModel ontModel, OntClass dataSuperClass) {
		String[][] rows = csvReader.getRawData();
		OntClass subjectClass = ontModel.createClass(subjectMapping.getSubjectClass().toString());
		for (int i = csvReader.getHeaderIndex()+1; i<rows.length; i++) {
			//for each row, create a inqle:Data, an inqle:Subject, and add mapped values to each
			String[] row = rows[i];
			String rowSubjectInstanceUri = generateDataInstanceUri(subjectMapping, row);
			Individual rowSubjectInstance = ontModel.createIndividual(rowSubjectInstanceUri, subjectClass);
			Individual rowDataInstance = ontModel.createIndividual(RDF.randomInstanceUri(RDF.DATA), dataSuperClass);
			rowDataInstance.addProperty(ResourceFactory.createProperty(RDF.HAS_SUBJECT), rowSubjectInstance);
			importRowValues(subjectMapping, row, rowSubjectInstance, rowDataInstance);
		}
	}

	private String generateDataInstanceUri(SubjectMapping subjectMapping, String[] row) {
		String uriPrefix = subjectMapping.getSubjectUriPrefix().toString();
		if (uriPrefix==null || subjectMapping.getSubjectUriType()==SubjectMapping.getSubjectUriCreationIndex(SubjectMapping.URI_TYPE_INQLE_GENERATED)) {
			uriPrefix = RDF.SUBJECT + "/"; 
		}
		String uriSuffix = UUID.randomUUID().toString();
		if (subjectMapping.getSubjectUriType()==SubjectMapping.getSubjectUriCreationIndex(SubjectMapping.URI_TYPE_COLUMN_VALUE)) {
			int subjectSuffixColumnIndex = csvReader.getColumnIndex(subjectMapping.getSubjectHeader());
			uriSuffix = row[subjectSuffixColumnIndex];
		}
		return uriPrefix + uriSuffix;
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
	
		importStaticValues(subjectMapping, subjectInstance, dataInstance);
		
		//loop thru each row and add values from appropriate column to appropriate individual
		String[][] rows = csvReader.getRawData();
		for (int i = csvReader.getHeaderIndex()+1; i<rows.length; i++) {
			String[] row = rows[i];
			importRowValues(subjectMapping, row, subjectInstance, dataInstance);
		}
	}
	
	
	/**
	 * To a given instance of inqle:Data or the associated instance of inqle:Subject,
	 * add a value for each header from a single SubjectMapping, for the given row of the data file 
	 * @param subjectMapping
	 * @param row
	 * @param subjectInstance
	 * @param dataInstance
	 */
	private void importRowValues(SubjectMapping subjectMapping, String[] row,
			Individual subjectInstance, Individual dataInstance) {
		for (DataMapping dataMapping: subjectMapping.getDataMappings()) {
			if (dataMapping.getMapsHeader() == null) {
				
			} else {
				String mappedHeader = dataMapping.getMapsHeader();
				int columnIndex = csvReader.getColumnIndex(mappedHeader);
				if (columnIndex < 0) {
					log.warn("Header '" + mappedHeader + "' not found in the data file.  Skipping import of this DataMapping.");
					continue;
				}
				String value = row[columnIndex];
				if (URI.create(RDF.SUBJECT_PROPERTY).equals(dataMapping.getMapsPropertyType())) {
					importValue(subjectInstance, ResourceFactory.createProperty(dataMapping.getMapsPredicate().toString()), value);
				} else {
					importValue(dataInstance, ResourceFactory.createProperty(dataMapping.getMapsPredicate().toString()), value);
				}
			}
		}
		
	}

	/**
	 * Import all static mapped values, for a given SubjectMapping
	 * @param subjectMapping
	 * @param subjectInstance
	 * @param dataInstance
	 */
	private void importStaticValues(SubjectMapping subjectMapping, Individual subjectInstance, Individual dataInstance) {
		for (DataMapping dataMapping: subjectMapping.getDataMappings()) {
			if (dataMapping.getMapsValue() == null || dataMapping.getMapsValue().toString().trim().length()==0) {
				continue;
			}
			URI propertyType = dataMapping.getMapsPropertyType();
			if (URI.create(RDF.SUBJECT_PROPERTY).equals(propertyType)) {
				//import into the subject instance
				importStaticValue(dataMapping, subjectInstance);
			} else {
				//import into the data table class
				importStaticValue(dataMapping, dataInstance);
			}
		}
	}

	/**
	 * 
	 * @param dataMapping
	 * @param individual
	 */
	private void importStaticValue(DataMapping dataMapping, OntResource individual) {
		if (dataMapping.getMapsPredicate()==null) {
			log.warn("Unable to import value '" + dataMapping.getMapsValue() + "' for individual " + individual + ". The property is null.");
		}
		Property property = ontModel.createProperty(dataMapping.getMapsPredicate().toString());
		Object value = dataMapping.getMapsValue();
		importValue(individual, property, value);
	}

	private void importValue(OntResource individual, Property property,
			Object value) {
		
		
		individual.addLiteral(property, value);
		
	}
	
}