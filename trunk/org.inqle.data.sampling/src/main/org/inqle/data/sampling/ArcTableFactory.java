package org.inqle.data.sampling;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.util.TypeConverter;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcSet;
import org.inqle.data.rdf.jenabean.ArcStep;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.util.BeanTool;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class ArcTableFactory {

	private OntModel ontModel;

	private ArcSet arcSet;

	private static Logger log = Logger.getLogger(ArcTableFactory.class);
	
	public ArcTableFactory(OntModel ontModel) {
		this.ontModel = ontModel;
//		log.info("Created ArcTableFactory using model:" + JenabeanWriter.modelToString(ontModel));
	}
	
	/**
	 * This class constructs an ArcTable, given a Jena RDF OntModel and the URI of the 
	 * subject class, as a Jena Resource
	 * It extracts from the OntModel a List of ArcSet objects,
	 * 1 for each row.
	 * @param subjectClass the URI of the class, to use as the starting point.
	 * Each instance of this class will be represented by a row
	 * @return
	 */
	public ArcTable createArcTable(Resource subjectClass) {
		//log.info("createArcTable(" + subjectClass + ")...");
		ArcTable arcTable = new ArcTable();
		arcTable.setSubjectClass(subjectClass.toString());
//		List<ArcSet> rows = new ArrayList<ArcSet>();
		
		ExtendedIterator subjectEI = ontModel.listIndividuals(subjectClass);
		while (subjectEI.hasNext()) {
			Individual individual = (Individual)subjectEI.next();
			ArcSet theArcSet = getArcSet(individual);
			if (theArcSet == null || theArcSet.getArcList() == null || theArcSet.getArcList().size()==0) {
				continue;
			}
			//log.info("Adding ArcSet:" + theArcSet + "...");
			arcTable.addArcSet(theArcSet);
			
		}//next subject row
		return arcTable;
	}
	
	/**
	 * Starting from the startingSubjectInstance, walk out thru the graph and assemble an
	 * ArcSet
	 * @param startingSubjectInstance
	 * @return
	 */
	public ArcSet getArcSet(Individual startingSubject) {
		String startingSubjectUri = startingSubject.getURI();
//		log.info("getArcSet(" + startingSubjectUri + ")...");
		arcSet = new ArcSet();
		if (startingSubjectUri != null) {
			arcSet.setSubject(URI.create(startingSubjectUri));
		}
		addArcAndValues(startingSubject);
		return arcSet;
	}

	/**
	 * Starting at the startingResource, add to the ArcSet all Arcs present in the OntModel
	 * @param arcSet
	 * @param startingResource
	 */
	private void addArcAndValues(Individual startingResource) {
//		log.info("addArcAndValues()...");
		//loop thru each property of each class
//		Property nullProperty = null;
//		RDFNode nullObject = null;
//		StmtIterator stmtIterator = ontModel.listStatements(startingResource, nullProperty, nullObject);
		StmtIterator stmtIterator = startingResource.listProperties();
		while (stmtIterator.hasNext()) {
			Statement statement = stmtIterator.nextStatement();
			
			Arc arc = new Arc();
			addArcSteps(arc, statement, new ArrayList<Resource>());
			 
		}//next property column
		
	}

	/**
	 * Add ArcSteps to the Arc.  End the Arc by setting its value.  If no value is set,
	 * the Arc will be ignored
	 * @param arc
	 * @param arcSet
	 * @param startingStatement
	 * @param excludeResources the Collection of resources to skip, or those which were already passed in the arc
	 */
	private void addArcSteps(Arc arc, Statement startingStatement, Collection<Resource> excludeResources) {
//		log.info("Adding to Arc: " + arc + " the step for statement: " + startingStatement);
		Resource resource = null;
		RDFNode rdfNode = startingStatement.getObject();
		
		if (! (rdfNode instanceof Literal) && (!(rdfNode instanceof Resource))) {
			return;
		}
		
		Property property = startingStatement.getPredicate();
		String propStr = property.toString();
		
		if (rdfNode instanceof Literal) {
//			log.info("Adding final ArcStep:" + propStr);
			arc.addArcStep(new ArcStep(propStr));
			Literal literal = (Literal) rdfNode;
//			arcSet.addArcAndValue(arc, literal.getValue());
			arcSet.addArcAndValue(arc, TypeConverter.getObjectFromLiteral(literal));
//			log.info("Added to ArcSet: \n" + arc + "\n= " + literal.getValue());
			return;
		}
		
		resource = (Resource)rdfNode;
		//if this resource has already been traversed (i.e. we have stepped backward) then return without adding
	  if (excludeResources.contains(resource)) {
	  	return;
	  }
	  arc.addArcStep(new ArcStep(propStr));
	  excludeResources.add(resource);
	  StmtIterator stmtIterator = resource.listProperties();
  	
  	//if this is a non-blank resource with no properties, add the URI as a value
	  if (!stmtIterator.hasNext() && !resource.isAnon()) {
	  	arcSet.addArcAndValue(arc, resource.toString());
			return;
	  }

  	while (stmtIterator.hasNext()) {
  		Statement statement = stmtIterator.nextStatement();
//  		addArcSteps(arc.createClone(), statement, excludeResources);
  		addArcSteps(BeanTool.clone(arc), statement, excludeResources);
	  }
		
	}
	
}
