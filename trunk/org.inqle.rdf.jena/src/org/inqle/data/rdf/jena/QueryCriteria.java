package org.inqle.data.rdf.jena;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.util.OntModelUtil;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.DataSource;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.larq.IndexLARQ;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.Store;

/**
 * Contains all info pertinent to a given SPARQL query.  
 * To execute a SPARQL query, simply create a QueryCriteria then pass
 * this to Queryer, as in
 * 
 *  QueryCriteria queryCriteria = new QueryCriteria(appInfo);
		queryCriteria.addDatamodel(datamodel);
		queryCriteria.setQuery(mySparql);
		RdfTable resultTable = Queryer.querySelect(queryCriteria);
 * 
 * 
 * @author David Donohue
 * Aug 7, 2007
 * 
 * TODO consider dynamic generation of SPARQL, through adding "RDF step objects"
 * TODO consider add a QueryCriteriaFactory, which creates appropriate QueryCriteria implementation
 * 
 * @version 0.5
 */
public class QueryCriteria {

	private Store store;
	
//	private List<Model> models = new ArrayList<Model>();
//	private List<Datamodel> datamodels = new ArrayList<Datamodel>();
	private List<String> datamodelIds = new ArrayList<String>();
	private DataSource dataSource = null;
	private String query = "";
	private IndexLARQ textIndex = null;

	@Deprecated
	private Model singleModel;

	private String inferenceRules;
	
	//private Persister persister = null;
	
	private static Logger log = Logger.getLogger(QueryCriteria.class);
	
	//public QueryCriteria(Persister persister) {
	public QueryCriteria() {
		//this.persister = persister;
		this.dataSource = DatasetFactory.create();
	}
	
	/**
	 * Add a Datamodel to the list of models to query, 
	 * given the ID of the Datamodel
	 * @param datamodelUri
	 */
	public void addDatamodel(String datamodelId) {
		Persister persister = Persister.getInstance();
//		Datamodel namedModel = (Datamodel)persister.getDatamodel(namedModelId);
//		addDatamodel(namedModel);
		datamodelIds.add(datamodelId);
		Model model = persister.getModel(datamodelId);
		log.info("In QueryCriteria, for datamodel: " + datamodelId + ", adding model of size " + model.size());
		addModel(datamodelId, model);
	}
	
//	/**
//	 * Add a Datamodel to the list of models to query
//	 * @param aModel
//	 */
//	public void addDatamodel(Datamodel datamodel) {
//		log.trace("QueryCriteria.addModel(" + datamodel + ")");
//		Persister persister = Persister.getInstance();
//		datamodels.add(datamodel);
//		Model model = persister.getModel(datamodel);
//		log.info("In QueryCriteria, for datamodel: " + datamodel.getId() + ", adding model of size " + model.size());
//		addModel(datamodel.getId(), model);
//	}
	
	public void addModel(String id, Model model) {
		if (inferenceRules  != null) {
			//add the inference-capable OntModel instead
			
			OntModel ontModel = OntModelUtil.asOntModel(model, inferenceRules);
//			models.add(ontModel);
			dataSource.addNamedModel(id, ontModel);
			log.info("Added inference rules to model of ID:" + id + "\n" + inferenceRules);
		} else {
//			models.add(model);
			dataSource.addNamedModel(id, model);
		}
	}
	
	/**
	 * This method sets the model as the default model.  Beware, this can foul up
	 * SPARQL queries which assume named graphs
	 * @param model
	 */
	public void setDefaultModel(Model model) {
		dataSource.setDefaultModel(model);
	}
	
//	@Deprecated
//	public void setSingleModel(Model model) {
//		this.singleModel = model;
//	}

	/**
	 * Add a List of Datamodel to be queried
	 * @param namedModelIds
	 */
	public void addDatamodelIds(Collection<String> datamodelIds) {
		if (datamodelIds == null) return;
		Persister persister = Persister.getInstance();
		for (String datamodelId: datamodelIds) {
			addDatamodel(datamodelId);
		}
	}
	
//	/**
//	 * Add a List of AModels to query
//	 * @param addDatamodels
//	 */
//	public void addDatamodels(List<Datamodel> addDatamodels) {
//		for (Datamodel aDatamodel: addDatamodels) {
//			addDatamodel(aDatamodel);
//		}
//	}
	
	/**
	 * Get the list of AModels which have been added to this
	 * @return
	 */
	public List<String> getDatamodelIds() {
		return datamodelIds;
	}
	
	/**
	 * retrieves the DataSource containing all models added
	 * @return
	 */
	public Dataset getDataset() {
		return dataSource;
	}
	
	/**
	 * Retrieve the SPARQL query which has been added to this
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Set the SPARQL query to use
	 * @param q
	 */
	public void setQuery(String q) {
		query = q;
	}

	/**
	 * Close any open objects, if any
	 */
	public void close() {
		//call close method for each ARepository object
//		Iterator<Model> modelsI = models.iterator();
//		while (modelsI.hasNext()) {
//			Model model = (Model)modelsI.next();
//			if (!model.isClosed()) model.close();
//		}
		dataSource.close();
		if (store != null) store.close();
	}

	/**
	 * If a single model has been added to this object, return it.  Otherwise
	 * throw an exception
	 * @return
	 */
//	public Model getModel() {
//		if (models.size() != 1) {
//			throw new RuntimeException("QueryCriteria has had " + models.size() + " models added to it.  Should have 1 model added to it if getModel() is to be called.");
//		}
//		return (Model) models.get(0);
//	}

	public IndexLARQ getTextIndex() {
		return textIndex;
	}

	public void setTextIndex(IndexLARQ textIndex) {
		this.textIndex = textIndex;
	}

	@Deprecated
	public Model getSingleModel() {
		return singleModel;
	}

	public String getInferenceRules() {
		return inferenceRules;
	}

	public void setInferenceRules(String inferenceRules) {
		this.inferenceRules = inferenceRules;
	}
	
	/*
	 * StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash,
                                       DatabaseType.Derby) ;
   JDBC.loadDriverDerby() ;
   String jdbcURL = "jdbc:derby:DB/SDB2"; 
   SDBConnection conn = new SDBConnection(jdbcURL, null, null) ; 
   Store store = SDBFactory.connectStore(conn, storeDesc) ;
   */
	 
}
