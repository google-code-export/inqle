package org.inqle.data.rdf;

import thewebsemantic.Namespace;

/**
 * This class is used for importing Data.
 * It represents a OWL class, and is not used
 * programmatically.
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

	public static final String DATA_SUBJECT_DATASET_ROLE_ID = "org.inqle.datamodels.dataSubject";
	public static final String DATA_PROPERTY_DATASET_ROLE_ID = "org.inqle.datamodels.dataProperty";
}
