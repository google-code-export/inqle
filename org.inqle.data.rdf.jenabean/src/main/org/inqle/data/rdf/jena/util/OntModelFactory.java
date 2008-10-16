package org.inqle.data.rdf.jena.util;

import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class OntModelFactory {

	public static OntModel asOntModel(Model model, String rulesText) {
//		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_RDFS_INF);
//		List<?> rules = Rule.parseRules(rulesText);
//	  GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
//		reasoner.setTransitiveClosureCaching(transitiveClosureCaching);
//	  spec.setReasoner(reasoner);
//	  //get the Model of new, inferred statements
//	  OntModel ontModel = ModelFactory.createOntologyModel(spec, model);
		
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
//  Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
	
		List<?> rules = Rule.parseRules(rulesText);
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
}
