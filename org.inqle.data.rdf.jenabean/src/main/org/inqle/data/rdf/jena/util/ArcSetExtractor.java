package org.inqle.data.rdf.jena.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcSet;
import org.inqle.data.rdf.jenabean.ArcStep;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class ArcSetExtractor {

	private OntModel ontModel;

	public ArcSetExtractor(OntModel ontModel) {
		this.ontModel = ontModel;
	}
	
	/**
	 * extracts from the OntModel a List of ArcSet objects,
	 * 1 for each row.
	 * @param subjectClass the URI of the class, to use as the starting point.
	 * Each instance of this class will be represented by a row
	 * @return
	 */
	public List<ArcSet> extract(Resource subjectClass) {
		List<ArcSet> rows = new ArrayList<ArcSet>();
		
		ExtendedIterator subjectEI = ontModel.listIndividuals(subjectClass);
		while (subjectEI.hasNext()) {
			Individual individual = (Individual)subjectEI.next();
			ArcSet arcSet = getArcSet(individual);
			rows.add(arcSet);
		}//next subject row
		return rows;
	}
	
	/**
	 * Starting from the startingSubjectInstance, walk out thru the graph and assemble an
	 * ArcSet
	 * @param startingSubjectInstance
	 * @return
	 */
	public ArcSet getArcSet(Resource startingSubjectInstance) {
		ArcSet arcSet = new ArcSet();
		arcSet.setSubject(URI.create(startingSubjectInstance.toString()));
		addArcs(arcSet, startingSubjectInstance);
		return arcSet;
	}

	/**
	 * Starting at the startingResource, add to the ArcSet all Arcs present in the OntModel
	 * @param arcSet
	 * @param startingResource
	 */
	private void addArcs(ArcSet arcSet, Resource startingResource) {
		
		//loop thru each property of each class
		Property nullProperty = null;
		RDFNode nullObject = null;
		StmtIterator stmtIterator = ontModel.listStatements(startingResource, nullProperty, nullObject);
		while (stmtIterator.hasNext()) {
			Statement statement = stmtIterator.nextStatement();
			
			Arc arc = new Arc();
			addArcSteps(arc, arcSet, statement);
			 
		}//next property column
		
	}

	/**
	 * Add ArcSteps to the Arc.  End the Arc by setting its value.  If no value is set,
	 * the Arc will be ignored
	 * @param arc
	 * @param arcSet
	 * @param startingStatement
	 */
	private void addArcSteps(Arc arc, ArcSet arcSet, Statement startingStatement) {
		ArcStep arcStep = new ArcStep();
		Property property = startingStatement.getPredicate();
		String propStr = property.toString();
		arcStep.setPredicate(propStr);
		arc.addArcStep(arcStep);
		
		RDFNode rdfNode = startingStatement.getObject();
		if (rdfNode instanceof Literal) {
			Literal literal = (Literal) rdfNode;
			arc.setValue(literal.getValue());
			arcSet.addArc(arc);
			return;
		}
		if (! (rdfNode instanceof Resource)) {
			return;
		}
	  Resource resource = (Resource)rdfNode;

  	StmtIterator stmtIterator = resource.listProperties();
  	
  	//if this is a non-blank resource with no properties, add the URI as a value
	  if (!stmtIterator.hasNext() && !resource.isAnon()) {
	  	arc.setValue(resource.toString());
			arcSet.addArc(arc);
			return;
	  }
  	int i=0;
  	while (stmtIterator.hasNext()) {
  		Arc theArc = arc;
  		if (i > 0) {
  			theArc = arc.createClone();
  		}
  		Statement statement = stmtIterator.nextStatement();
  		addArcSteps(theArc, arcSet, statement);
  		
  		i++;
	  }
	}
}
