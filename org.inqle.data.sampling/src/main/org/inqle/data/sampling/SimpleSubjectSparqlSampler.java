/**
 * 
 */
package org.inqle.data.sampling;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.data.rdf.jena.util.ArcLister;
import org.inqle.data.rdf.jena.util.SubjectClassLister;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcStep;

import thewebsemantic.Namespace;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Resource;

/** 
 * This simple sampler does the following:
 * (1) Select 1-2 NamedModels from which to extract data
 * (2) From those NamedModels, get all distinct subject classes
 * (3) Randomly select a subject form among those
 * (4) Randomly select 2 or more Arcs, starting from this subject
 * (4) Dynamically generate a SPARQL query which selects for all
 * RDF data which contain all of the selected predicates.
 * 
 * Thus this sampler is agnostic of the nature of each subject 
 * (each line of the example set).  Instead, each subject
 * in a sample produced by this sampler have only the following in common: 
 * they each have in common the same set of RDF attributes
 * 
 * @author David Donohue
 * Dec 26, 2007
 * 
 * TODO add elements which define how to render the UI elements, for manual execution mode
 */
@TargetDataset(ISampler.SAMPLER_DATASET)
@Namespace(RDF.INQLE)
public class SimpleSubjectSparqlSampler extends AConstructSparqlSampler {

	//TODO ensure that rows are retrieved randomly
	public static final String MAXIMUM_ROWS_INITIAL_QUERY = "1000";
	public static final String MAXIMUM_ROWS_DATATABLE = "1000";
	
	//TODO permit these to be configurable
	public static final int MAXIMUM_LEARNABLE_PREDICATES = 3;
	public static final int MINIMUM_LEARNABLE_PREDICATES = 2;
	private static final int MAX_NUMBER_OF_ROWS = 500;
	
	static Logger log = Logger.getLogger(SimpleSubjectSparqlSampler.class);

	public SimpleSubjectSparqlSampler createClone() {
		SimpleSubjectSparqlSampler newSampler = new SimpleSubjectSparqlSampler();
		newSampler.clone(this);
		return newSampler;
	}
	
	public SimpleSubjectSparqlSampler createReplica() {
		SimpleSubjectSparqlSampler newSampler = new SimpleSubjectSparqlSampler();
		newSampler.replicate(this);
		return newSampler;
	}


	@Override
	/**
	 * Select a random set of Arcs, beginning with the provided subjectClass.
	 * This implementation is capable of finding arcs that are 1, 2, or 3 
	 * steps away from the subject.
	 * 
	 */
	protected List<Arc> selectRandomArcs(Collection<String> modelsToUse, Resource subjectClass, int numberToSelect) {
		return ArcLister.listRandomArcs(modelsToUse, subjectClass.toString(), numberToSelect);
	}


	@Override
	protected URI selectRandomSubjectClass(Collection<String> modelsToUse) {
		
		String sparql = Queryer.decorateSparql(SubjectClassLister.SPARQL_SELECT_CLASSES, true, 0, 1);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(modelsToUse);
		queryCriteria.setQuery(sparql);
		List<String> results = Queryer.selectSimpleList(queryCriteria, "classUri");
		if (results==null || results.size()==0) return null;
		String classUri = results.get(0);
		return URI.create(classUri);
	}


	@Override
	protected String generateSparql(Resource subjectClass, Collection<Arc> dataColumns) {
		return Queryer.generateSparqlDescribe(subjectClass, dataColumns, true, 0, MAX_NUMBER_OF_ROWS);
	}

}
