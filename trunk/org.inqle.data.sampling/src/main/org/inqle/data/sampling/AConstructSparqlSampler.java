package org.inqle.data.sampling;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.JenabeanConverter;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * Base class for possible Samplers which employ a SPARQL CONSTRUCT query
 * to assemble the final DataTable. 
 * Subclass this class to create a Sampler which generates its
 * final IDataTable (e.g., ArcTable) by generating a SPARQL query to define the data to pull.
 * 
 * This helper class assists with creating Sampler which do the following:
 * (1) Select a set of datasets to mine.  This class supports mining of 
 * datamodels which are registered in the metarepository
 * (2) Generate a SPARQL CONSTRUCT query, which will create a minable Model of data
 * (3) Execute the SPARQL against the set of datamodels.
 * (4) Convert the Model to a IDataTable (e.g., an ArcTable)
 * (5) Specify whichcolumns in the IDataTable are the ID and which is the label
 * 
 * At a minimum, implementing classes must implement the generateSparql() method,
 * to generate the SPARQL query, which will define the final DataSet.  The code for this
 * method should do the following
 *  * To the arcList field, add each DataColumn object, representing each column of 
 *    data to appear in the final DataTable
 *  * set the query field to a SPARQL query, which will retrieve the resultset of data
 *    to mine
 * 
 * @author David Donohue
 * Dec 26, 2007
 */
@Namespace(RDF.INQLE)
public abstract class AConstructSparqlSampler extends ASampler {

	public static final String SUBJECT_LABEL = "subject";
	
	//public static final String URI_DATA_EXTRACTION_STATUS = new ASparqlSamplingStatus().getClassUri();
	
	//TODO permit implementing Sampler to specify minimum and maximum number of datamodels
	protected static final int DEFAULT_MINIMUM_NUMBER_OF_DATAMODELS = 1;
	protected static final int DEFAULT_MAXIMUM_NUMBER_OF_DATAMODELS = 2;

	//protected Persister persister;

	//protected String query;
	
	static Logger log = Logger.getLogger(AConstructSparqlSampler.class);

	private URI subjectClass;

	private Collection<Arc> arcs;

	private int numberOfAttributes;
	
//	public void setPersister(Persister persister) {
//		this.persister = persister;
//	}

	/**
	 * Perform all steps to get to final resultant DataTable.
	 * Honor any existing fields, and otherwise make random decisions
	 */
	public ArcTable execute() {
		Collection<String> modelsToUse = selectNamedModels();
		log.debug("modelsToUse=" + modelsToUse);
		
		Resource subjectClass = selectSubjectClass(modelsToUse);
		if (subjectClass == null) return null;
		log.info("Subject class=" + subjectClass);
		
		Collection<Arc> dataColumnsToUse = selectDataColumns(modelsToUse, subjectClass);
		log.debug("dataColumnsToUse=" + dataColumnsToUse);
//		String sparql = generateSparql(modelsToUse, persister);
		String sparql = generateSparql(subjectClass, dataColumnsToUse);
		log.info("Generated sparql for sampling:" + sparql);
		Model resultModel = doQuery(modelsToUse, dataColumnsToUse, sparql);
		OntModel ontModel = ModelFactory.createOntologyModel();
		ontModel.add(resultModel);
		ArcTableFactory factory = new ArcTableFactory(ontModel);
		ArcTable resultDataTable = factory.createArcTable(subjectClass);
		return resultDataTable;
	}
	
	protected Resource selectSubjectClass(Collection<String> modelsToUse) {
		if (subjectClass == null) {
			subjectClass = selectRandomSubjectClass(modelsToUse);
		}
		if (subjectClass == null) {
			log.error("Unable to select a subject class.  Perhaps the input Model had no recognizable OWL classes.");
			return null;
		}
		return ResourceFactory.createResource(subjectClass.toString());
		
	}

	protected abstract URI selectRandomSubjectClass(Collection<String> modelsToUse);

	public Collection<Arc> selectDataColumns(Collection<String> modelsToUse, Resource subjectClass) {
		if (arcs != null) {
			return arcs;
		}
		
		return selectRandomArcs(modelsToUse, subjectClass, getNumberOfAttributes());
	}

	/**
	 * How many learnable attributes to use 
	 * (exclusing ID or label attributes)?
	 * @return
	 */
	public int getNumberOfAttributes() {
		return numberOfAttributes;
	}

	/**
	 * Select a List of Arcs to use as data columns in the table
	 * @param modelsToUse the IDs of the NamedModels to query
	 * @param numberToSelect the number of Arcs to select
	 * @return
	 */
	protected abstract Collection<Arc> selectRandomArcs(Collection<String> modelsToUse, Resource subjectClass, int numberToSelect);

	/**
	 * Get the Collection of NamedModels from which to extract data.  Implementations should
	 * return the user-specified values, if present.  Otherwise selects automatically 
	 * from the provided list
	 * @param datamodelOptions the Collection of available data models to consider
	 * @return
	 */
	public Collection<String> selectAvailableNamedModels() {
		Persister persister = Persister.getInstance();
//		if (getAvailableNamedModels() != null) {
//			return getAvailableNamedModels();
//		}
		List<NamedModel> allNamedModels = persister.listNamedModels();
		log.debug("allNamedModels=" + allNamedModels);
		if (allNamedModels != null) {
			List<String> allNamedModelIds = JenabeanConverter.getIds(allNamedModels);
			return allNamedModelIds;
		}
		return new ArrayList<String>();
	}

	/**
	 * Get the List of NamedModels from which to extract data.  Implementations should
	 * return the user-specified values, if present.  Otherwise selects automatically 
	 * from the provided list
	 * @param datamodelOptions the List of available data models to consider
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> selectNamedModels() {
		log.debug("getSelectedNamedModels()=" + getSelectedNamedModels());
		//if named models already selected, return
		if (getSelectedNamedModels() != null && getSelectedNamedModels().size() > 0) {
			log.debug("getSelectedNamedModels().size()=" + getSelectedNamedModels().size());
			return getSelectedNamedModels();
		}
		//...otherwise populate the list of available named models
		Collection<String> choosableNamedModels = selectAvailableNamedModels();
		log.debug("choosableNamedModels=" + choosableNamedModels);
		//int countChoosableNamedModels = choosableNamedModels.size();
		
		//if we do not have enough choosable datamodels, return null
//		if (countChoosableNamedModels < getMinimumNumberOfNamedModels()) {
//			return;
//		}
		
		//if we do have enough choosable datamodels, return the entire list of choosable datamodels
//		if (countChoosableNamedModels > getMaximumNumberOfNamedModels()) {
//			return;
//		}
		
		//we must select a subset of the choosable datamodels
		//randomly remove datamodels until we reach the maximum acceptable number
		Collection<String> selectedNamedModels = (Collection<String>) RandomListChooser.chooseRandomItems(new ArrayList(choosableNamedModels), getMinimumNumberOfNamedModels(), getMaximumNumberOfNamedModels());
		
		//if (selectedNamedModels != null) {
		return selectedNamedModels;
		//}
	}

	/**
	 * Do a SPARQL query, and convert results into a learnable DataTable
	 * @param dataColumnsToUse 
	 * @return
	 */
	protected Model doQuery(Collection<String> namedModelsToUse, Collection<Arc> dataColumnsToUse, String sparql) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(namedModelsToUse);
		queryCriteria.setQuery(sparql);
		
		Model resultModel = Queryer.constructRdf(queryCriteria);
//		log.debug(RdfTableWriter.dataTableToString(resultRdfTable));
		//DataTable resultDataTable = DataTableFactory.createDataTable(resultRdfTable, Arrays.asList(arcList));
//		DataTable resultDataTable = DataTableFactory.createDataTable(resultRdfTable, dataColumnsToUse);
		//add column info to the DataTable
		//List<DataColumn> columnsList = Arrays.asList(arcList);
		//resultDataTable.setColumns(columnsList);
		//resultDataTable.setColumns(dataColumnsToUse);
		
		//setResultDataTable(resultDataTable);
		return resultModel;
	}

	/**
	 * Implementations should override this method.  
	 * 
	 * Care should be taken to ensure that queries permit random or manual access to 
	 * the complete set of data that is in the datamodels.  Otherwise, some data
	 * will forever be invisible to the data extraction process, and will never be
	 * used for learning.
	 * 
	 * @return sparql the SPARQL query string, to generate the learnable table of data
	 */
	protected abstract String generateSparql(Resource subjectClass, Collection<Arc> dataColumns);
	//protected abstract String generateSparql(Collection<String> modelsToUse, Persister persister);

	/**
	 * TODO Make this configurable
	 * @return
	 */
	protected int getMaximumNumberOfNamedModels() {
		return DEFAULT_MAXIMUM_NUMBER_OF_DATAMODELS;
	}

	/**
	 * TODO Make this configurable
	 * @return
	 */
	protected int getMinimumNumberOfNamedModels() {
		return DEFAULT_MINIMUM_NUMBER_OF_DATAMODELS;
	}

//	public String getQuery() {
//		return query;
//	}
//
//	public void setQuery(String query) {
//		this.query = query;
//	}

	public void replicate(AConstructSparqlSampler objectToClone) {
		clone(objectToClone);
		super.replicate(objectToClone);
	}
	
	public void clone(AConstructSparqlSampler templateSampler) {
		setSubjectClass(templateSampler.getSubjectClass());
		setArcs(templateSampler.getArcs());
		setNumberOfAttributes(templateSampler.getNumberOfAttributes());
		super.clone(templateSampler);
//		setQuery(((ASelectSparqlSampler)templateSampler).getQuery());
	}

	public void setNumberOfAttributes(int numberOfAttributes) {
		this.numberOfAttributes = numberOfAttributes;
	}

	public URI getSubjectClass() {
		return subjectClass;
	}

	public void setSubjectClass(URI subjectClass) {
		this.subjectClass = subjectClass;
	}

	public Collection<Arc> getArcs() {
		return arcs;
	}

	public void setArcs(Collection<Arc> arcs) {
		this.arcs = arcs;
	}
	
	/**
	 * Add all field values from the provided template sampler to this sampler, including the ID field
	 */
//	public void replicate(ISampler templateSampler) {
//		clone((ASelectSparqlSampler)templateSampler);
//		setId(((ASelectSparqlSampler)templateSampler).getId());
//	}
}
