package org.inqle.data.rdf;

import java.net.URI;
import java.util.UUID;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class RDF {

	public static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String XSD = "http://www.w3.org/2001/XMLSchema#";
	public static final String OWL = "http://www.w3.org/2002/07/owl#";
	public static final String DC = "http://purl.org/dc/elements/1.1/";
	public static final String GEO = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	public static final String SKOS = "http://www.w3.org/2004/02/skos/core#";
	public static final String TYPE = RDF + "type";
	public static final String JA = "http://jena.hpl.hp.com/2005/11/Assembler#";
	public static final String PF = "http://jena.hpl.hp.com/ARQ/property#";
	public static final String FRESNEL = "http://www.w3.org/2004/09/fresnel#";
	public static final String UMBEL = "http://umbel.org/umbel#";
	public static final String UMBEL_SC = "http://umbel.org/umbel/sc/";
	public static final String UMBEL_AC = "http://umbel.org/umbel/ac/";
	public static final String UMBEL_NE = "http://umbel.org/umbel/ne/";
	public static final String INQLE = "http://inqle.org/ns/0.1/";
	
	public static final String SDB = "http://jena.hpl.hp.com/2007/sdb#";	
	public static final String THEWEBSEMANTIC = "http://thewebsemantic.com/";
	public static final String JAVA_CLASS = THEWEBSEMANTIC + "javaclass";
	//public static final String JAVA_CLASS = INQLE + "javaClass";
	public static final String HAS_PERSISTABLE_MEMBER_ATTRIBUTE = INQLE + "hasPersistableMemberAttribute";
	public static final String JENABEAN_ID_ATTRIBUTE = "id";
	public static final String UNKNOWN_SUBJECT = INQLE + "unknownSubject";
	
	public static final String DESCRIPTION_PREDICATE = RDFS + "comment";
	//public static final String NAME_PREDICATE = DC + "name";
	public static final String NAME_PREDICATE = RDFS + "label";
	public static final String FOAF = "http://xmlns.com/foaf/0.1/";
	public static final Property LABEL_PROPERTY = ResourceFactory.createProperty(NAME_PREDICATE);
	public static final Property COMMENT_PROPERTY = ResourceFactory.createProperty(DESCRIPTION_PREDICATE);
	public static final String DATA = INQLE + "Data";
	public static final String SUBJECT = INQLE + "Subject";
	public static final String HAS_SUBJECT = INQLE + "subject";
	public static final String HAS_DATA = INQLE + "data";
	public static final String DATA_PROPERTY = INQLE + "DataProperty";
	public static final String SUBJECT_PROPERTY = INQLE + "SubjectProperty";
	public static final String DATE_PROPERTY = INQLE + "date";
	public static final String IDENTIFIER_PROPERTY = INQLE + "identifier";
	public static final String IS_DATA_PROPERTY_OF = INQLE + "isDataPropertyOf";
	
	
	/**
	 * This method is intended to be called in cases where the URI fragment 
	 * is guaranteed to be a legitimate string for the purposes
	 * of creating a URI.  E.g. any of the above URI constants can
	 * be passed as an argument.
	 * @param uriFragment the string to append at end of the new URI.
	 * @return
	 */
	public static URI getUri(String uriFragment) {
		URI newUri = URI.create(INQLE + uriFragment);
		return newUri;
	}
	
	public static URI classUri(Class clazz) {
		URI newUri = URI.create(INQLE + clazz.getName());
		return newUri;
	}

	public static URI instanceUri(String string) {
		URI newUri = URI.create(INQLE + string);
		return newUri;
	}
	
	public static URI parameterUri(String attribute) {
		URI newUri = URI.create(INQLE + attribute);
		return newUri;
	}

	public static URI classPropertyUri(Class<?> clazz, String property) {
		URI newUri = URI.create(INQLE + clazz.getName() + "_" + property);
		return newUri;
	}
	
	public static String getPrefixClause(String prefix, String uri) {
		String str = "PREFIX " + prefix.trim() + ": <" + uri.trim() + ">\n";
		return str;
	}

	public static String randomInstanceUri(String baseUri) {
		return baseUri + "/" + UUID.randomUUID().toString();
	}
}
