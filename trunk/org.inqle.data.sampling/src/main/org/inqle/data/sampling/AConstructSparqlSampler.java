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
 * (1) Select a set of datasets to mine.  This class supports mining of 
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
		
		Arc labelArc = selectLabelArc(modelsToUse, subjectClass);
		log.info("Label arc=" + labelArc);
		Collection<Arc> avoidArcs = new ArrayList<Arc>();
		avoidArcs.add(labelArc);
		Collection<Arc> learnableArcs = getLearnableArcs(modelsToUse, subjectClass, avoidArcs);
		//log.info("Learnable arcs=" + learnableArcs);
		if (learnableArcs==null || learnableArcs.size()==0) {
			log.warn("Retrieved no learnable attributes from Jena model.  returning null.");
			return null;
		}
		if (learnableArcs.size() < getNumberOfAttributes()) {
			log.warn("Retrieved only " + learnableArcs.size() + " learnable attributes.  Supposet to get " + getNumberOfAttributes() + ".  Proceeding.");
		}
		List<Arc> allArcs = new ArrayList<Arc>();
		allArcs.add(labelArc);
		allArcs.addAll(learnableArcs);
		//log.info("All arcs=" + allArcs);
		
		String sparql = generateSparql(subjectClass, allArcs);
		//log.info("Generated sparql for sampling:" + sparql);
		Model resultModel = doQuery(modelsToUse, allArcs, sparql);
		//log.info("Retrieved this sample:" + JenabeanWriter.modelToString(resultModel));
		if (resultModel==null) return null;
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
			log.warn("TTTTTTTTTTTTTT Table lacks the label arc:" + labelArc);
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
		if (arcs != null) return arcs;
		return decideLearnableArcs(modelsToUse, subjectClass, getNumberOfAttributes(), arcsToExclude);
	}

	/**
	 * Select a List of Arcs to use as data columns in the table
	 * @param modelsToUse the IDs of the NamedModels to query
	 * @param numberToSelect the number of Arcs to select
	 * @return
	 */
	protected abstract Collection<Arc> decideLearnableArcs(Collection<String> modelsToUse, Resource subjectClass, int numberToSelect, Collection<Arc> arcsToExclude);

	/**
	 * How many learnable attributes to use 
	 * (exclusing ID or label attributes)?
	 * @return
	 */
	public int getNumberOfAttributes() {
		if (numberOfAttributes < 1) {
			return selectNumberOfAttributes();
		}
		return numberOfAttributes;
	}

	public abstract int selectNumberOfAttributes();

	/**
	 * Get the Collection of NamedModels from which to extract data.  Implementations should
	 * return the user-specified values, if present.  Otherwise selects automatically 
	 * from the provided list
	 * @param datamodelOptions the Collection of available data models to consider
	 * @return
	 */
	public Collection<String> selectAvailableNamedModels() {
		Persister persister = Persister.getInstance();
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
		
		//we must select a subset of the choosable datamodels
		//randomly remove datamodels until we reach the maximum acceptable number
		int numberDatasetsToSelect = RandomListChooser.getRandomNumber(getMinimumNumberOfNamedModels(), getMaximumNumberOfNamedModels());
		Collection<String> selectedNamedModels = (Collection<String>) RandomListChooser.chooseRandomItemsSubtractively(new ArrayList(choosableNamedModels), numberDatasetsToSelect);
		
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
