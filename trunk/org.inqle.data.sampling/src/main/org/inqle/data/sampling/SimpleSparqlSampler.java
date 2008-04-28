/**
 * 
 */
package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

/** 
 * This simple sampler does the following:
 * (1) Select 1-2 NamedModels from which to extract data
 * (2) From those NamedModels, get all distinct predicates
 * (3) Randomly select 2-3 predicates form among those
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
@Namespace(RDF.INQLE)
public class SimpleSparqlSampler extends ASparqlSampler {

	//TODO ensure that rows are retrieved randomly
	public static final String MAXIMUM_ROWS_INITIAL_QUERY = "1000";
	public static final String MAXIMUM_ROWS_DATATABLE = "1000";
	
	//TODO permit these to be configurable
	public static final int MAXIMUM_LEARNABLE_PREDICATES = 3;
	public static final int MINIMUM_LEARNABLE_PREDICATES = 2;
	
	static Logger log = Logger.getLogger(SimpleSparqlSampler.class);
	
	//TODO add clause to exclude those columns which are non-minable
	public static final String SPARQL_GET_DISTINCT_PREDICATES = 
		"SELECT DISTINCT ?predicate \n " +
		"{ GRAPH ?anyGraph { \n " +
		"?subject ?predicate ?object \n " +
		"} } \n " +
		" LIMIT " + MAXIMUM_ROWS_INITIAL_QUERY;
	protected Collection<String> selectedPredicates;
	//protected Collection<String> availablePredicates;


	public Collection<String> selectPredicates(Collection<String> modelsToUse, Persister persister) {
		//if predicates already selected, return
		if (selectedPredicates != null) {
			return selectedPredicates;
		}
		List<String> availablePredicates = selectAvailablePredicates(modelsToUse, persister);
		return (Collection<String>) RandomListChooser.chooseRandomItems(new ArrayList(availablePredicates), MINIMUM_LEARNABLE_PREDICATES, MAXIMUM_LEARNABLE_PREDICATES);
	}


	public List<String> selectAvailablePredicates(Collection<String> modelsToUse, Persister persister) {
		QueryCriteria queryCriteria = new QueryCriteria(persister);
		queryCriteria.addNamedModelIds(selectedNamedModels);
		queryCriteria.setQuery(SPARQL_GET_DISTINCT_PREDICATES);
		return Queryer.selectUriList(queryCriteria);
	}

	/**
	 * Given a set of selected predicates, generate a SPARQL query, which will 
	 * retrieve the data to be mined.  Populate the query and dataColumns attributes
	 * @return
	 */
	public String generateSparql(Collection<String> modelsToUse, Persister persister) {
		//first step: select predicates, if not already done
		selectPredicates(modelsToUse, persister);

		if (selectedPredicates == null || selectedPredicates.size() < MINIMUM_LEARNABLE_PREDICATES || selectedPredicates.size() > MAXIMUM_LEARNABLE_PREDICATES) {
			log.error("Expect selection of between " + MINIMUM_LEARNABLE_PREDICATES + " and " + MAXIMUM_LEARNABLE_PREDICATES + " predicates.");
			//clear the previously selected attributes
			return null;
		}
		//construct SPARQL:
		String sparql = "SELECT $" + SUBJECT_LABEL;
		
		int i=0;
		String whereClause = "";
		List<DataColumn> dataColumnsList = new ArrayList<DataColumn>();
		dataColumnsList.add(new DataColumn(SUBJECT_LABEL, ISampler.URI_UNKNOWN_SUBJECT));
		for (String selectedPredicate: selectedPredicates) {
			
			//add attribute to SELECT clause
			String predicateLabel = "attribute" + i;
			sparql += " $" + predicateLabel;
			
			//add statement to WHERE clause
			if (i > 0) {
				whereClause += " . ";
			}
			whereClause += " $" + SUBJECT_LABEL + " <" +
				selectedPredicate.toString() +
				"> $" +
				predicateLabel +
				" \n";
			
			//create column for this predicate and add to list of columns
			DataColumn dataColumn = new DataColumn(predicateLabel, selectedPredicate);
			dataColumnsList.add(dataColumn);
			
			i++;
		}
		DataColumn[] dc = new DataColumn[]{};
		setDataColumns((DataColumn[]) dataColumnsList.toArray(dc));

		sparql += "\n{ GRAPH ?anyGraph { \n ";
		sparql += whereClause;
		sparql += "} } LIMIT " + MAXIMUM_ROWS_DATATABLE;
		
		//query = sparql;
		return sparql;
		
	}


//	@Override
//	public void removeInterimData() {
//		super.removeInterimData();
//		this.availablePredicates = null;
//	}
	
	//@RdfProperty(RDF.INQLE + "selectedPredicates")
	public Collection<String> getSelectedPredicates() {
		return selectedPredicates;
	}

	public void setSelectedPredicates(Collection<String> selectedPredicates) {
		this.selectedPredicates = selectedPredicates;
	}

	/**
	 * Add the values from the provided template to this
	 * @param sampler
	 */
	@Override
	public void clone(ISampler templateSampler) {
		super.clone(templateSampler);
		setSelectedPredicates(((SimpleSparqlSampler)templateSampler).getSelectedPredicates());
		//setAvailablePredicates(((SimpleSparqlSampler)templateSampler).getAvailablePredicates());
	}

//	public Collection<String> getAvailablePredicates() {
//		return availablePredicates;
//	}
//
//
//	public void setAvailablePredicates(Collection<String> availablePredicates) {
//		this.availablePredicates = availablePredicates;
//	}

	public SimpleSparqlSampler createClone() {
		SimpleSparqlSampler newSampler = new SimpleSparqlSampler();
		newSampler.clone(this);
		return newSampler;
	}
	
	public SimpleSparqlSampler createReplica() {
		SimpleSparqlSampler newSampler = new SimpleSparqlSampler();
		newSampler.replicate(this);
		return newSampler;
	}
}
