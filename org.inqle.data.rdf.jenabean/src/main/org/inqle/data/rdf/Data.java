package org.inqle.data.rdf;

import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;

/**
 * This class is used for importing Data.
 * It represents a generic and flexible class of data, especially for data imported into INQLE
 * 
 * @author David Donohue
 * Jul 12, 2008
 * 
 * TODO consider replacing this with simple declaration
 * of the class URI of this OWL class, as a constant in
 * class RDF
 */
@Namespace(RDF.INQLE)
public class Data {

	private static final String DATAMODEL_NAME_FOR_DATA_SUBJECT = "_DataSubject";
	private static final String DATAMODEL_NAME_FOR_DATA_PROPERTY = "_DataProperty";
	public static final String DATAMODEL_ID_FOR_DATA_PROPERTY = Persister.CORE_DATABASE_ID + "/" + IDBConnector.SUBDATABASE_SYSTEM + "/" + DATAMODEL_NAME_FOR_DATA_PROPERTY;
	public static final String DATAMODEL_ID_FOR_DATA_SUBJECT = Persister.CORE_DATABASE_ID + "/" + IDBConnector.SUBDATABASE_SYSTEM + "/" + DATAMODEL_NAME_FOR_DATA_SUBJECT;
}
