package org.inqle.data.sampling;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.core.util.RandomListChooser;
import org.inqle.core.util.RandomUtil;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.PurposefulDatamodel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.data.rdf.jena.util.DatamodelLister;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
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
 * (1) Select a set of datamodels to mine.  This class supports mining of 
 * datamodels which are registered in the metarepository
 * (2) Generate a SPARQL CONSTRUCT query, which will create a minable Model of data
 * (3) Execute the SPARQL against the set of datamodels.
 * (4) Convert the Model to a IDataTable (e.g., an ArcTable)
 * (5) Specify which column in the IDataTable is the label
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
	private static final int DEFAULT_MAXIMUM_LEARNABLE_PREDICATES = 3;
	private static final int DEFAULT_MINIMUM_LEARNABLE_PREDICATES = 2;
	private int maxLearnablePredicates = DEFAULT_MAXIMUM_LEARNABLE_PREDICATES;
	private int minLearnablePredicates = DEFAULT_MINIMUM_LEARNABLE_PREDICATES;
	
	//protected Persister persister;

	//protected String query;
	
	static Logger log = Logger.getLogger(AConstructSparqlSampler.class);

	private URI subjectClass;

	private Collection<Arc> arcs;

//	private int numberOfAttributes = -1;
	
//	public void setPersister(Persister persister) {
//		this.persister = persister;
//	}

	/**
	 * Perform all steps to get to final resultant DataTable.
	 * Honor any existing fields, and otherwise make random decisions
	 */
	public ArcTable execute() {
		Collection<String> modelsToUse = selectDatamodels();
		log.info("modelsToUse=" + modelsToUse);
		
		Resource subjectClass = selectSubjectClass(modelsToUse);
		if (subjectClass == null) return null;
		log.info("Subject class=" + subjectClass);
		
		Arc labelArc = selectLabelArc(modelsToUse, subjectClass);
		log.info("Label arc=" + labelArc);
		Collection<Arc> avoidArcs = new ArrayList<Arc>();
		avoidArcs.add(labelArc);
		log.info("Getting learnable arcs...");
		Collection<Arc> learnableArcs = getLearnableArcs(modelsToUse, subjectClass, avoidArcs);
		log.info("Learnable arcs=" + learnableArcs);
		if (learnableArcs==null || learnableArcs.size()==0) {
			log.warn("Retrieved no learnable attributes from Jena model.  returning null.");
			return null;
		}
//		int selectedNumberOfAttributes = selectNumberOfAttributes();
//		if (learnableArcs.size() < selectedNumberOfAttributes) {
//			log.warn("Retrieved only " + learnableArcs.size() + " learnable attributes.  Supposet to get " + selectedNumberOfAttributes + ".  Proceeding.");
//		}
		List<Arc> allArcs = new ArrayList<Arc>();
		allArcs.add(labelArc);
		allArcs.addAll(learnableArcs);
		log.info("All arcs for learning=" + allArcs);
		
		String sparql = generateSparql(subjectClass, allArcs);
		//log.info("Generated sparql for sampling:" + sparql);
		Model resultModel = doQuery(modelsToUse, allArcs, sparql);
		
		if (resultModel==null) return null;
		
		log.trace("Retrieved this sample:" + JenabeanWriter.modelToString(resultModel));
		
		OntModel ontModel = ModelFactory.createOntologyModel();
		ontModel.add(resultModel);
		ArcTableFactory factory = new ArcTableFactory(ontModel);
		//log.info("CACACACACACACACACACA Creating ArcTable...");
		ArcTable resultDataTable = factory.createArcTable(subjectClass);
//		if (resultDataTable.getColumns().contains(labelArc)) {
		//log.info("TATATATATATATATATATATA Testing arcs: " + resultDataTable.getColumns() + " for label arc: " + labelArc);
		if (resultDataTable.getHeaderIndex(labelArc) >= 0) {
			resultDataTable.setLabelColumnIndex(resultDataTable.getHeaderIndex(labelArc));
		} else {
			log.warn("Table lacks the label arc:" + labelArc);
			return null;
		}
		return resultDataTable;
	}

	protected Resource selectSubjectClass(Collection<String> modelsToUse) {
		if (subjectClass == null) {
			subjectClass = decideSubjectClass(modelsToUse);
		}
		if (subjectClass == null) {
			log.error("Unable to select a subject class.  Perhaps the input Model had no recognizable OWL classes.");
			return null;
		}
		return ResourceFactory.createResource(subjectClass.toString());
	}

	protected abstract URI decideSubjectClass(Collection<String> modelsToUse);
	
	public Arc selectLabelArc(Collection<String> modelsToUse, Resource subjectClass) {
		if (labelArc != null) return labelArc;
		return decideLabelArc(modelsToUse, subjectClass);
	}
	
	protected abstract Arc decideLabelArc(Collection<String> modelsToUse, Resource subjectClass);

	public Collection<Arc> getLearnableArcs(Collection<String> modelsToUse, Resource subjectClass, Collection<Arc> arcsToExclude) {
		if (arcs != null && arcs.size() > 0) return arcs;
		return decideLearnableArcs(modelsToUse, subjectClass, selectNumberOfAttributes(), arcsToExclude);
	}

	/**
	 * Select a List of Arcs to use as data columns in the table
	 * @param modelsToUse the IDs of the Datamodels to query
	 * @param numberToSelect the number of Arcs to select
	 * @return
	 */
	protected abstract Collection<Arc> decideLearnableArcs(Collection<String> modelsToUse, Resource subjectClass, int numberToSelect, Collection<Arc> arcsToExclude);

//	/**
//	 * How many learnable attributes to use 
//	 * (exclusing ID or label attributes)?
//	 * @return
//	 */
//	public int getNumberOfAttributes() {
//		if (numberOfAttributes < 1) {
//			return selectNumberOfAttributes();
//		}
//		return numberOfAttributes;
//	}

	public int selectNumberOfAttributes() {
		int randomInt = new RandomUtil().getRandomInt(minLearnablePredicates, maxLearnablePredicates);
		log.info("RRRRRRRRRRRRRRRRRRRRRRR Random number from " + minLearnablePredicates + " to " + maxLearnablePredicates + " = " + randomInt);
		return randomInt;
	}

	public int getMaxLearnablePredicates() {
		return maxLearnablePredicates;
	}

	public void setMaxLearnablePredicates(int maxLearnablePredicates) {
		this.maxLearnablePredicates = maxLearnablePredicates;
	}

	public int getMinLearnablePredicates() {
		return minLearnablePredicates;
	}

	public void setMinLearnablePredicates(int minLearnablePredicates) {
		this.minLearnablePredicates = minLearnablePredicates;
	}


	/**
	 * Get the Collection of Datamodels from which to extract data.  Implementations should
	 * return the user-specified values, if present.  Otherwise selects automatically 
	 * from the provided list
	 * @param datamodelOptions the Collection of available data models to consider
	 * @return
	 */
	public Collection<PurposefulDatamodel> selectAvailableDatamodels() {
		return DatamodelLister.listAllDatamodelsOfPurpose(Persister.EXTENSION_DATAMODEL_PURPOSES_MINABLE_DATA);
//		IDBConnector connector = DBConnectorFactory.getDBConnector(InqleInfo.USER_DATABASE_ID);
//		return connector.listModels();
	}

	/**
	 * Get the List of Datamodels from which to extract data.  Implementations should
	 * return the user-specified values, if present.  Otherwise selects automatically 
	 * from the provided list
	 * @param datamodelOptions the List of available data models to consider
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> selectDatamodels() {
		log.debug("getSelectedDatamodels()=" + getSelectedDatamodels());
		//if named models already selected, return
		if (getSelectedDatamodels() != null && getSelectedDatamodels().size() > 0) {
			log.debug("getSelectedDatamodels().size()=" + getSelectedDatamodels().size());
			return getSelectedDatamodels();
		}
		//...otherwise populate the list of available named models
		Collection<PurposefulDatamodel> choosableDatamodels = selectAvailableDatamodels();
		log.debug("choosableDatamodels=" + choosableDatamodels);
		
		//we must select a subset of the choosable datamodels
		//randomly remove datamodels until we reach the maximum acceptable number
		int numberDatasetsToSelect = RandomListChooser.getRandomNumber(getMinimumNumberOfDatamodels(), getMaximumNumberOfDatamodels());
		Collection<String> selectedDatamodels = (Collection<String>) RandomListChooser.chooseRandomItemsSubtractively(new ArrayList(choosableDatamodels), numberDatasetsToSelect);
		
		//if (selectedDatamodels != null) {
		return selectedDatamodels;
		//}
	}

	/**
	 * Do a SPARQL query, and convert results into a learnable DataTable
	 * @param dataColumnsToUse 
	 * @return
	 */
	protected Model doQuery(Collection<String> namedModelsToUse, Collection<Arc> dataColumnsToUse, String sparql) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodelIds(namedModelsToUse);
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
	protected int getMaximumNumberOfDatamodels() {
		return DEFAULT_MAXIMUM_NUMBER_OF_DATAMODELS;
	}

	/**
	 * TODO Make this configurable
	 * @return
	 */
	protected int getMinimumNumberOfDatamodels() {
		return DEFAULT_MINIMUM_NUMBER_OF_DATAMODELS;
	}

//	public void replicate(AConstructSparqlSampler objectToClone) {
//		clone(objectToClone);
//		super.replicate(objectToClone);
//	}
//	
//	public void clone(AConstructSparqlSampler templateSampler) {
//		setSubjectClass(templateSampler.getSubjectClass());
//		setArcs(templateSampler.getArcs());
//		setMinLearnablePredicates(templateSampler.getMinLearnablePredicates());
//		setMaxLearnablePredicates(templateSampler.getMaxLearnablePredicates());
//		super.clone(templateSampler);
//	}

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
