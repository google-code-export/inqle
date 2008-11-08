package org.inqle.data.rdf.jena.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcSet;

import com.hp.hpl.jena.rdf.model.Resource;

public class ArcSparqlBuilder {

	private static final String SUBJECT_VARIABLE_NAME = "subject";
	private Collection<Arc> arcs;
	private Map<String, String> arcStepMappings;
	private int attributeCounter;
	
	private static Logger log = Logger.getLogger(ArcSparqlBuilder.class);
	
	public ArcSparqlBuilder(Collection<Arc> arcs) {
		this.arcs = arcs;
		arcStepMappings = new HashMap<String, String>();
		attributeCounter = 0;
	}

	public String getSparqlWhereFromArcs(String subject) {
		String sparql = "";
		String subjectStr ="?" + SUBJECT_VARIABLE_NAME;
		sparql += subjectStr + " a <" + subject + ">";
		for (Arc arc: arcs) {
			sparql += getSparqlWhereFromArc(subject, subjectStr, arc, null);
		}
		return sparql;
	}

	public String getSparqlWhereFromArc(String subject, String subjectVariableName, Arc arc, Object object) {
			String sparql = "";
	//		String subjectStr = "";
	//		String newNode = subject;
			
	//		for (int i=0; i < arc.getArcSteps().length; i++) {
			List<String> stepsSoFar = new ArrayList<String>();
			String subjectStr = subjectVariableName;
			log.info("Adding where lines for Arc: " + arc);
			for (String predicate: arc.getArcSteps()) {
				stepsSoFar.add(predicate);
				if (variableNameExists(stepsSoFar)) {
					subjectStr = getVariableName(stepsSoFar);
					log.info("Step already stored: " + stepsSoFar + "; setting next subjectStr to =" + subjectStr);
					continue;
				}
				String objectStr = null;
				
				if (object == null) {
					objectStr = getVariableName(stepsSoFar);
					log.info("Step NOT stored: " + stepsSoFar + "; setting next subjectStr to =" + objectStr);
				} else if (object instanceof URI) {
					objectStr = "<" + ((URI)object).toString() + ">";
				} 
//				else if (object instanceof String) {
//					objectStr = "\"" + object.toString() + "\"";
//				} else {
//					objectStr = object.toString();
//				}

				if (objectStr==null) {
					log.warn("Arc object is null for steps: " + stepsSoFar + "; object=" + object);
					continue;
				}
				sparql += "\n . ";
				sparql += subjectStr + " <" + predicate + "> " + objectStr;
				subjectStr = objectStr;
			}
			return sparql;
		}

	private boolean variableNameExists(List<String> key) {
		if (key==null) return false;
		return arcStepMappings.containsKey(key.toString());
	}

	private String getVariableName(List<String> arcSteps) {
		if (arcSteps == null || arcSteps.size()==0) return "?" + SUBJECT_VARIABLE_NAME;
		String variableName = arcStepMappings.get(arcSteps.toString());
		if (variableName==null) {
			variableName = "?a_" + attributeCounter;
			attributeCounter++;
			arcStepMappings.put(arcSteps.toString(), variableName);
		}
		return variableName;
	}

	/**
	 * Generate SPARQL to conduct a CONSTRUCT query, given a starting Resource class, 
	 * the List of Arcs, 
	 * @param subjectClass
	 * @param arcs the List of Arcs
	 * @param randomize if true, the order will be randomized
	 * @param offset the number of records to skip, when paginating.  Note paginating does not work with randomizing.
	 * @param limit the number of records to retrieve
	 * @return
	 */
	public String generateSparqlConstruct(Resource subjectClass, boolean randomize, int offset, int limit) {
		String where = getSparqlWhereFromArcs(subjectClass.toString());
		String sparql = "";
		if (randomize) {
			sparql += "PREFIX inqle-fn: <java:org.inqle.data.rdf.jena.fn.> \n";
		}
		sparql += "CONSTRUCT {\n" + where + "}\n";
		sparql += "{ GRAPH ?anyGraph {\n" + where + "\n} }\n";
		if (randomize) {
			sparql += "ORDER BY inqle-fn:Rand() \n";
		}
		sparql += "LIMIT " + limit + " OFFSET " + offset + "\n";
		
		return sparql;
	}

	
	
	
	
	
	public static String getSparqlWhereFromArcs(String subject, ArcSet arcSet) {
		String sparql = "";
		int k=0;
		for (Arc arc: arcSet.getArcList()) {
			Object value = arcSet.getValue(arc);
			sparql += getStaticSparqlWhereFromArc(subject, arc, value, String.valueOf(k));
			k++;
		}
		return sparql;
	}
	
	public static String getStaticSparqlWhereFromArc(String subject, Arc arc, Object object, String identifier) {
		String sparql = "";
//		String subjectStr = "";
//		String newNode = subject;
		String subjectStr = "?subject_" + identifier;
		sparql += subjectStr + " a <" + subject + ">";
		int i=0;
//		for (int i=0; i < arc.getArcSteps().length; i++) {
		for (String predicate: arc.getArcSteps()) {
			i++;
			String objectStr = "";
			if (object == null) {
//				newNode = UUID.randomUUID().toString();
				objectStr = "?attribute" + identifier + "_" + i;
			} else if (object instanceof URI) {
				objectStr = "<" + ((URI)object).toString() + ">";
			} else if (object instanceof String) {
				objectStr = "\"" + object.toString() + "\"";
			} else {
				objectStr = object.toString();
			}
//			if (i > 0) {
				sparql += "\n . ";
//			}
			sparql += subjectStr + " <" + predicate + "> " + objectStr;
			subjectStr = objectStr;
		}
		return sparql;
	}
}
