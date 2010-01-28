package org.inqle.rdf;

import java.net.URI;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.inqle.rdf.annotations.TargetDatabaseId;
import org.inqle.rdf.annotations.TargetModelName;
import org.inqle.rdf.annotations.TargetModelType;

public class RDF {

	private static final Logger log = Logger.getLogger(RDF.class);
	
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
	public static final String INQLE = "http://ns.inqle.org/";
	
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
	public static final String DATA = INQLE + "Data";
	public static final String SUBJECT = INQLE + "Subject";
	public static final String HAS_SUBJECT = INQLE + "hasSubject";
	public static final String HAS_DATA = INQLE + "hasData";
	public static final String DATA_PROPERTY = INQLE + "DataProperty";
	public static final String SUBJECT_PROPERTY = INQLE + "SubjectProperty";
	public static final String DATE_PROPERTY = INQLE + "date";
	public static final String IDENTIFIER_PROPERTY = INQLE + "identifier";
	public static final String IS_DATA_PROPERTY_OF = INQLE + "isDataPropertyOf";
	public static final String TABLE_DATA = INQLE + "TableData";
	
	public static final String SUBDATABASE_SYSTEM = "system";
	public static final String SUBDATABASE_DATA = "data";

	public static final String TEXT_MATCH = PF + "textMatch";
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

	public static String getDatamodelId(String dbid,
			String modelType, String modelName) {
		return dbid + "/" + modelType + "/" + modelName;
	}

	/**
	 * Get the ID of the database to which the provided class is supposed to be persisted, per its
	 * annotation TargetDatabaseId
	 * @param persistableClass
	 * @return
	 */
	public static String getTargetDatabaseId(Class<?> persistableClass) {
		TargetDatabaseId targetDatabaseId = persistableClass.getAnnotation(TargetDatabaseId.class);
		if (targetDatabaseId == null) {
			log.warn("Unable to retrieve database id for " + persistableClass + ".  Perhaps the class definition for this class lacks the TargetDatamodel annotation.");
			return null;
		}
		return targetDatabaseId.value();
	}

	/**
	 * Get the name of the datamodel to which the provided class is supposed to be persisted, per its
	 * annotation TargetDatamodelName
	 * @param persistableClass
	 * @return
	 */
	public static String getTargetModelName(Class<?> persistableClass) {
		TargetModelName targetModel = persistableClass.getAnnotation(TargetModelName.class);
		if (targetModel == null) {
			log.warn("Unable to retrieve model name for " + persistableClass + ".  Perhaps the class definition for this class lacks the TargetModelName annotation.");
			return null;
		}
		return targetModel.value();
	}

	/**
		 * Get the name of the datamodel to which the provided class is supposed to be persisted, per its
		 * annotation TargetDatamodelName
		 * @param persistableClass
		 * @return
		 */
		public static String getTargetModelType(Class<?> persistableClass) {
			TargetModelType targetModelType = persistableClass.getAnnotation(TargetModelType.class);
			if (targetModelType == null) {
				log.warn("Unable to retrieve model type for " + persistableClass + ".  Perhaps the class definition for this class lacks the TargetModelType annotation.  By default, assume it to be 'system' type.");
				return SUBDATABASE_SYSTEM;
			}
			return targetModelType.value();
		}

	/**
	 * For classes with both annotations TargetDatabaseId and TargetDatamodelName
	 * @return the datamodelId
	 */
	public static String getTargetDatamodelId(Class<?> persistableClass) {
		String databaseId = getTargetDatabaseId(persistableClass);
		if (databaseId==null) return null;
		return getTargetDatamodelId(persistableClass, databaseId);
	}

	/**
	 * For classes with the annotation TargetDatamodelName
	 * @param databaseId
	 * @return the datamodelId
	 */
	public static String getTargetDatamodelId(Class<?> persistableClass, String databaseId) {
		String modelName = getTargetModelName(persistableClass);
		String modelType = getTargetModelType(persistableClass);
		if (modelName==null) return null;
		return getDatamodelId(databaseId, modelType, modelName);
	}

	public static final String MINIMUM_SCORE_THRESHOLD = "0.01";
}
