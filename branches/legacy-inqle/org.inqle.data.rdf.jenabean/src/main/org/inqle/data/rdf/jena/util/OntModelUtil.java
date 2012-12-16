package org.inqle.data.rdf.jena.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.TriplePattern;
import com.hp.hpl.jena.reasoner.rulesys.ClauseEntry;
import com.hp.hpl.jena.reasoner.rulesys.Functor;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class OntModelUtil {

	private static final Logger log = Logger.getLogger(OntModelUtil.class);
	private static final int SPARQL_LIMIT = 1000;
	
	/**
	 * Converts a (persistent) model into an OntModel, using the provided text of Jena
	 * reasoning rules
	 * @param model
	 * @param rulesText
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static OntModel asOntModel(Model model, String rulesText) {
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
	
		List rules = getRulesList(rulesText);
		
	  GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
	  reasoner.setParameter(ReasonerVocabulary.PROPtraceOn, Boolean.TRUE);
	
	  //not sure whether to do this
		reasoner.setTransitiveClosureCaching(true);
		
	  spec.setReasoner(reasoner);
	 
	  //get the Model of new, inferred statements
	  //hopefully no memory problem
	  OntModel ontModel = ModelFactory.createOntologyModel(spec, model);
  
	  return ontModel;
	}

	public static List<?> getRulesList(String rulesText) {
		List<?> rules = Rule.parseRules(
				Rule.rulesParserFromReader (
				new BufferedReader(new StringReader( rulesText ) )));
		return rules;
	}

	/**
		 * This adds statements to persistentModel, representaing all new statements that
		 * can be inferred given the rules defined by rulesText.  As an interim step, this method stores a 
		 * large part of the final model in memory.  As a consequence, this emthod produces an OutOfMemoryError
		 * when inferring on large RDF datasets (e.g. 250,000 triples, running w/ JVM RAm set to 512 MB, 
		 * -Xmx512m)
		 * @param persistentModel
		 * @param rulesText
		 * @param transitiveClosureCaching
		 */
		public static void mergeInferredStatementsInMemory(Model persistentModel, String rulesText) {
			OntModel model = null;
			boolean errorOccurred = false;
			try {
				model = asOntModel(persistentModel, rulesText);
			} catch (Exception e) {
				log.error("Unable to generate an OntModel from existing model + rules: " + rulesText, e);
				errorOccurred = true;
			}
			
			if (errorOccurred) {
				mergeInferredStatementsUsingSparql(persistentModel, rulesText);
			}
	    //add this inferred model back to DB
	    persistentModel.add(model);
		}

	public static void mergeInferredStatementsUsingSparql(Model model,
			String rulesText) {
		
		List<?> rules = getRulesList(rulesText);
		for (Object ruleObj: rules) {
			Rule rule = (Rule)ruleObj;
			String sparql = ruleToSparql(rule);
			log.info("Rule " + rule.getName() + ":\n" + sparql);
			
			constructAndAddToModel(model, sparql, true);
		}
	}

	private static void constructAndAddToModel(Model model, String baseSparql, boolean repeatUntilNoNewStatements) {
		Model memoryModel = ModelFactory.createDefaultModel();
		int offset = 0;
		boolean repeatPagination = true;
		while (repeatPagination) {
			repeatPagination = repeatUntilNoNewStatements;
			//paginate through the SPARQL
			while (true) {
				String sparql = baseSparql + " LIMIT " + SPARQL_LIMIT + " OFFSET " + offset;
				Query query = QueryFactory.create(sparql);
				log.info("Querying:\n" + sparql);
				QueryExecution qe = QueryExecutionFactory.create(query, model);
				Model resultModel = qe.execConstruct();
				if (resultModel.size()>0) {
					log.info(offset + ": Adding " + resultModel.size() + " new statements to memory model.");
					
	//				memoryModel.add(resultModel);
					
					//add only new statements to the memory model.  
					//Remove any statements that are already represented in the original model
					memoryModel.add(resultModel.difference(model));
				} else {
					repeatPagination = false;
					log.info("Found no matching triples.");
					break;
				}
				offset = offset + SPARQL_LIMIT;
			}//next page of results
			if (memoryModel==null || memoryModel.size()==0) {
				repeatPagination = false;
			} else {
				log.info("Adding memory model of size " + memoryModel.size() + " to the persistent model.");
				model.add(memoryModel);
			}
			
		}//repeat pagination until finished
		
		
	}

	private static String ruleToSparql(Rule rule) {
		String construct = "CONSTRUCT {\n";
		String where = "WHERE {\n";
		ClauseEntry[] body = rule.getBody();
		int i=0;
		for (ClauseEntry entry: body) {
			//ignore nested rules
			if (entry instanceof Rule) {
				continue;
			}
			if (i > 0) where += ". ";
			where += entryToString(entry) + "\n";
			i++;
		}
		
		ClauseEntry[] head = rule.getHead();
		int j=0;
		for (ClauseEntry entry: head) {
			//ignore nested rules
			if (entry instanceof Rule) {
				continue;
			}
			if (j > 0) construct += ". ";
			construct += entryToString(entry) + " \n";
			j++;
		}
		return construct + "}\n" + where + "}\n";
	}

	private static String entryToString(ClauseEntry entry) {
		if (entry instanceof Functor) {
			return ((Functor)entry).toString();
		}
		TriplePattern triple = (TriplePattern)entry;
		Node s = triple.getSubject();
		Node p = triple.getPredicate();
		Node o = triple.getObject();
		return nodeToString(s) + " " + nodeToString(p) + " " + nodeToString(o);
		
	}

	private static String nodeToString(Node node) {
		if (node.isURI()) {
			return "<" + node.getURI() + ">";
		}
		
		if (node.isLiteral()) {
			return node.getLiteralLexicalForm();
		}
		return node.toString(); 
	}
}
