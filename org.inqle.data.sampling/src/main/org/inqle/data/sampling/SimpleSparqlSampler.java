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


	/**
	 * Select the predicates to use.  When predicates are already specified, 
	 * use them.
	 * 
	 * TODO Test each pre-selected predicate, to ensure it is available in the data models.  When not available, add reandom attributes to compensate.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> selectPredicates(Collection<String> modelsToUse, Persister persister) {
		//if predicates already selected, return
		if (selectedPredicates != null && selectedPredicates.size() > 0) {
			return selectedPredicates;
		}
		List<String> availablePredicates = selectAvailablePredicates(modelsToUse, persister);
		return (Collection<String>) RandomListChooser.chooseRandomItems(new ArrayList(availablePredicates), getMinimumNumberOfPredicates(), getMaximumNumberOfPredicates());
	}


	private int getMaximumNumberOfPredicates() {
		return MAXIMUM_LEARNABLE_PREDICATES;
	}


	private int getMinimumNumberOfPredicates() {
		return MINIMUM_LEARNABLE_PREDICATES;
	}


	public List<String> selectAvailablePredicates(Collection<String> modelsToUse, Persister persister) {
		QueryCriteria queryCriteria = new QueryCriteria(persister);
		queryCriteria.addNamedModelIds(modelsToUse);
		queryCriteria.setQuery(SPARQL_GET_DISTINCT_PREDICATES);
		return Queryer.selectUriList(queryCriteria);
	}

	@Override
	public List<DataColumn> selectDataColumns(Collection<String> modelsToUse,
			Persister persister) {
		List<DataColumn> dataColumnsList = new ArrayList<DataColumn>();
		//first step: select predicates, if not already done
		Collection<String> predicatesToUse = selectPredicates(modelsToUse, persister);
		log.info("predicatesToUse=" + predicatesToUse);
		if (predicatesToUse == null || predicatesToUse.size() < getMinimumNumberOfPredicates() || predicatesToUse.size() > getMaximumNumberOfPredicates()) {
			log.error("Expect selection of between " + getMinimumNumberOfPredicates() + " and " + getMaximumNumberOfPredicates() + " predicates.");
			//clear the previously selected attributes
			return dataColumnsList;
		}
		
		int i=0;
		
		dataColumnsList.add(new DataColumn(SUBJECT_LABEL, ISampler.URI_SUBJECT_CONTAINING_COMMON_ATTRIBUTES));
		for (String predicateToUse: predicatesToUse) {
			
			//add attribute to SELECT clause
			String predicateLabel = "attribute" + i;
			
			//add statement to WHERE clause
			//create column for this predicate and add to list of columns
			log.trace("Adding predicate " + predicateLabel + " = " + predicateToUse);
			DataColumn dataColumn = new DataColumn(predicateLabel, predicateToUse);
			dataColumnsList.add(dataColumn);
			
			i++;
		}
		return dataColumnsList;
	}
	
	/**
	 * Given a set of selected predicates, generate a SPARQL query, which will 
	 * retrieve the data to be mined.  As a side effect, populate the dataColumns attributes
	 * The first item in the List of DataColumns is the subject
	 * @return the generated SPARQL query
	 * 
	 * TODO for algorithms which walk across arcs, add corresponding SPARQL statements to the query
	 * TODO provide this method in abstract class (complete w/ Arc support) such that implmeenters must only identify a list of DataColumns
	 */
	@Override
	public String generateSparql(List<DataColumn> dataColumns) {
		assert(dataColumns != null & dataColumns.size() > 0);
		//construct SPARQL: The first column is the subject
		//String sparql = "SELECT $" + dataColumns.get(0).getQueryLabel();
		String subjectLabel = dataColumns.get(0).getQueryLabel();
		String sparql = "SELECT ";
		int i=0;
		String whereClause = "";
		
		for (DataColumn dataColumn: dataColumns) {
			
			//add attribute to SELECT clause
			sparql += " $" + dataColumn.getQueryLabel();
			
			if (i < 1) {
				i++;
				continue;
			}
			
			//add statement to WHERE clause
			if (i > 1) {
				whereClause += " . ";
			}
			
			whereClause += " $" + subjectLabel + " <" +
			dataColumn.getColumnUri().toString() +
				"> $" +
				dataColumn.getQueryLabel() +
				" \n";
			i++;
		}

		sparql += "\n{ GRAPH ?anyGraph { \n ";
		sparql += whereClause;
		sparql += "} } LIMIT " + MAXIMUM_ROWS_DATATABLE;
		
		return sparql;
		
	}
	
	
	
//	/**
//	 * Given a set of selected predicates, generate a SPARQL query, which will 
//	 * retrieve the data to be mined.  As a side effect, populate the dataColumns attributes
//	 * @return the generated SPARQL query
//	 */
//	@Override
//	public String generateSparql(Collection<String> modelsToUse, Persister persister) {
//		//first step: select predicates, if not already done
//		Collection<String> predicatesToUse = selectPredicates(modelsToUse, persister);
//
//		if (predicatesToUse == null || predicatesToUse.size() < MINIMUM_LEARNABLE_PREDICATES || selectedPredicates.size() > MAXIMUM_LEARNABLE_PREDICATES) {
//			log.error("Expect selection of between " + MINIMUM_LEARNABLE_PREDICATES + " and " + MAXIMUM_LEARNABLE_PREDICATES + " predicates.");
//			//clear the previously selected attributes
//			return null;
//		}
//		//construct SPARQL:
//		String sparql = "SELECT $" + SUBJECT_LABEL;
//		
//		int i=0;
//		String whereClause = "";
//		List<DataColumn> dataColumnsList = new ArrayList<DataColumn>();
//		dataColumnsList.add(new DataColumn(SUBJECT_LABEL, ISampler.URI_SUBJECT_CONTAINING_COMMON_ATTRIBUTES));
//		for (String predicateToUse: predicatesToUse) {
//			
//			//add attribute to SELECT clause
//			String predicateLabel = "attribute" + i;
//			sparql += " $" + predicateLabel;
//			
//			//add statement to WHERE clause
//			if (i > 0) {
//				whereClause += " . ";
//			}
//			whereClause += " $" + SUBJECT_LABEL + " <" +
//				predicateToUse.toString() +
//				"> $" +
//				predicateLabel +
//				" \n";
//			
//			//create column for this predicate and add to list of columns
//			DataColumn dataColumn = new DataColumn(predicateLabel, predicateToUse);
//			dataColumnsList.add(dataColumn);
//			
//			i++;
//		}
//		DataColumn[] dc = new DataColumn[]{};
//		setDataColumns((DataColumn[]) dataColumnsList.toArray(dc));
//
//		sparql += "\n{ GRAPH ?anyGraph { \n ";
//		sparql += whereClause;
//		sparql += "} } LIMIT " + MAXIMUM_ROWS_DATATABLE;
//		
//		return sparql;
//		
//	}
	
	
	


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
