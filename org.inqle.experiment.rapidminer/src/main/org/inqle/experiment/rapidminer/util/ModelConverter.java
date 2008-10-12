package org.inqle.experiment.rapidminer.util;

import java.util.ArrayList;
import java.util.List;

import org.inqle.data.rdf.jena.util.ArcSetExtractor;
import org.inqle.data.rdf.jenabean.ArcSet;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
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
		List<Attribute> attributes = new ArrayList<Attribute>();
		ArcSetExtractor arcSetExtractor = new ArcSetExtractor(ontModel);
		List<ArcSet> arcSets = arcSetExtractor.extract(subjectClass);
		
		//we now have an ArcSet for each row.  Each ArcSet contains 1 Arc for each column (or 0 when the column
		//has missing value for the row)
		
		
		MemoryExampleTable table = new MemoryExampleTable(attributes);
		
		
		return table;
	}

}
