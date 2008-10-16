package org.inqle.data.rdf.jena.sdb;

import java.util.List;

import org.inqle.data.rdf.jena.util.OntModelFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class OntModelMerger {

	/**
	 * This adds statements to persistentModel, representaing all new statements that
	 * can be inferred given the rules defined by Resource reasonerConfiguration
	 * @param persistentModel
	 * @param reasonerConfiguration
	 * 
	 * @see http://jena.sourceforge.net/inference/
	 */
	public static void mergeInferredStatements(Model persistentModel, Resource reasonerConfiguration) {
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_RDFS_INF);
//    Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
		Reasoner reasoner = GenericRuleReasonerFactory.theInstance().create(reasonerConfiguration);
		reasoner.setParameter(ReasonerVocabulary.PROPtraceOn, Boolean.TRUE);

		spec.setReasoner(reasoner);
   
    OntModel model = ModelFactory.createOntologyModel(spec, persistentModel);
  
    //write model back to DB
    persistentModel.add(model);
	}
	
	/**
	 * This adds statements to persistentModel, representaing all new statements that
	 * can be inferred given the rules defined by rulesText
	 * @param persistentModel
	 * @param rulesText
	 * @param transitiveClosureCaching
	 */
	public static void mergeInferredStatements(Model persistentModel, String rulesText) {
//		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
////    Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
//		
//		List<?> rules = Rule.parseRules(rulesText);
//	  
//	  GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
//	  reasoner.setParameter(ReasonerVocabulary.PROPtraceOn, Boolean.TRUE);
//
//	  //not sure whether to do this
//		reasoner.setTransitiveClosureCaching(transitiveClosureCaching);
//		
//    spec.setReasoner(reasoner);
//   
//    //get the Model of new, inferred statements
//    //hopefully no memory problem
//    OntModel model = ModelFactory.createOntologyModel(spec, persistentModel);
  
		OntModel model = OntModelFactory.asOntModel(persistentModel, rulesText);
			
    //add this inferred model back to DB
    persistentModel.add(model);
	}
}
