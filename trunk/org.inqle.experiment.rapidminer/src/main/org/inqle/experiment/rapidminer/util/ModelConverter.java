package org.inqle.experiment.rapidminer.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcSet;
import org.inqle.data.rdf.jenabean.ArcStep;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.MemoryExampleTable;

/**
 * Converts Jena Model into RapidMiner ExampleTable
 * @author David Donohue
 * Oct 10, 2008
 */
public class ModelConverter {

	private OntModel ontModel;
	private Resource subjectClass;

	public ModelConverter(OntModel ontModel, Resource subjectClass) {
		this.ontModel = ontModel;
		this.subjectClass = subjectClass;
	}
	
	public MemoryExampleTable createExampleTable() {
		List<ArcSet> rows = new ArrayList<ArcSet>();
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		ExtendedIterator subjectEI = ontModel.listIndividuals(subjectClass);
		while (subjectEI.hasNext()) {
			Individual individual = (Individual)subjectEI.next();
			ArcSet rowArcSet = new ArcSet();
			rowArcSet.setSubject(URI.create(individual.toString()));
			addArcs(rowArcSet, individual);
			rows.add(rowArcSet);
		}//next subject row
		
		//we now have an ArcSet for each row.  Each ArcSet contains 1 Arc for each column (or 0 when the column
		//has missing value for the row)
		
		
		MemoryExampleTable table = new MemoryExampleTable(attributes);
		
		
		return table;
	}

	private void addArcs(ArcSet arcSet, Resource startingResource) {
		
		//loop thru each property of each class
		StmtIterator stmtIterator = startingResource.listProperties();
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
