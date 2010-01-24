package org.inqle.data.rdf.jena.rules;

import java.util.Collection;

import org.inqle.data.rdf.jena.uri.UriMapping;
import org.inqle.rdf.RDF;
import org.inqle.rdf.beans.UniqueJenabean;

import thewebsemantic.Namespace;

/**
 * This class represents a single rule.  
 * This rule can serialized as a SPARQL CONSTRUCT query,
 * and thereby executed against a semantic data store.
 * A rule may also be serialized as a SPARQL ASK query, and used to test
 * whether it applies.
 * 
 * To construct a rule, you must construct a Rule object, and then do any of these 
 * optional steps:<ul>
 * <li>add prefix mappings, using the addPrefixMapping() method</li>
 * <li>add the antecedent of the rule (the IF portion) as a sparql clause</li>
 * <li>add the consequedent of the rule (the THEN portion) as a sparql clause</li>
 * <li>add any SPARQL modifier like ORDER BY, LIMIT, OFFSET.  Usually this is not done.</li>
 * </ul>
 * @author David Donohue
 * September 8, 2009
 */
@Namespace(RDF.INQLE)
public class Rule extends UniqueJenabean {

	private Collection<UriMapping> prefixMappings;
	private String antecedentClause;
	private String consequentClause;
	private String modifierClause;
	

//	public void clone(Rule objectToClone) {
//		setPrefixMappings(objectToClone.getPrefixMappings());
//		setAntecedentClause(objectToClone.getAntecedentClause());
//		setConsequentClause(objectToClone.getConsequentClause());
//		setModifierClause(objectToClone.getModifierClause());
//		super.clone(objectToClone);
//	}
//	
//	public void replicate(Rule objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//	}
//	
//	public Rule createClone() {
//		Rule newRule = new Rule();
//		newRule.clone(this);
//		return newRule;
//	}
//	
//	@Override
//	public Rule createReplica() {
//		Rule newRule = new Rule();
//		newRule.replicate(this);
//		return newRule;
//	}
	
	public String getStringRepresentation() {
		return getConstructQuery();
	}

	public String getPrefixClause() {
		String prefix = "";
		for (UriMapping mapping: prefixMappings) {
			prefix += "PREFIX " + mapping.getString() + ": <" + mapping.getUri() + "> \n";
		}
		return prefix;
	}
	
	public void addPrefixMapping(UriMapping mapping) {
		prefixMappings.add(mapping);
	}
	
	public Collection<UriMapping> getPrefixMappings() {
		return prefixMappings;
	}

	public void setPrefixMappings(Collection<UriMapping> prefixMappings) {
		this.prefixMappings = prefixMappings;
	}

	
	public void setAntecedentClause(String antecedentClause) {
		this.antecedentClause = antecedentClause;
	}

	public String getAntecedentClause() {
		return antecedentClause;
	}

	public void setConsequentClause(String consequentClause) {
		this.consequentClause = consequentClause;
	}

	public String getConsequentClause() {
		return consequentClause;
	}

	public String getModifierClause() {
		return modifierClause;
	}

	public void setModifierClause(String modifierClause) {
		this.modifierClause = modifierClause;
	}
	
	/**
	 * This returns a SPARQL ASK query 
	 * which can be applied to the default graph
	 * @return
	 */
	public String getAskQuery() {
		String sparql = "";
		if (getPrefixClause() != null) sparql += getPrefixClause();
		sparql += "ASK { \n";
		if (getAntecedentClause() != null) sparql += getAntecedentClause();
		sparql += "} \n";
		if (getModifierClause() != null) sparql += getModifierClause();
		return sparql;
	}
	
	/**
	 * This returns a SPARQL ASK query 
	 * which can be applied across 1 or more named graphs
	 * @return
	 */
	public String getAskQueryAcrossNamedGraphs() {
		String sparql = "";
		if (getPrefixClause() != null) sparql += getPrefixClause();
		sparql += "ASK {GRAPH ?graphName { \n";
		if (getAntecedentClause() != null) sparql += getAntecedentClause();
		sparql += "} } \n";
		if (getModifierClause() != null) sparql += getModifierClause();
		return sparql;
	}
	
	/**
	 * This returns a SPARQL CONSTRUCT query 
	 * which can be applied to the default graph
	 * @return
	 */
	public String getConstructQuery() {
		String sparql = "";
		if (getPrefixClause() != null) sparql += getPrefixClause();
		sparql += "CONSTRUCT {";
		if (getConsequentClause() != null) sparql += getConsequentClause();
		sparql += "} \n";
		sparql += "WHERE { \n";
		if (getAntecedentClause() != null) sparql += getAntecedentClause();
		sparql += "} \n";
		if (getModifierClause() != null) sparql += getModifierClause();
		
		return sparql;
	}
	
	/**
	 * This returns a SPARQL CONSTRUCT query 
	 * which can be applied across 1 or more named graphs
	 * @return
	 */
	public String getConstructQueryAcrossNamedGraphs() {
		String sparql = "";
		if (getPrefixClause() != null) sparql += getPrefixClause();
		sparql += "CONSTRUCT {";
		if (getConsequentClause() != null) sparql += getConsequentClause();
		sparql += "} \n";
		sparql += "{GRAPH ?graphName { \n";
		if (getAntecedentClause() != null) sparql += getAntecedentClause();
		sparql += "} } \n";
		if (getModifierClause() != null) sparql += getModifierClause();
		
		return sparql;
	}
	
}