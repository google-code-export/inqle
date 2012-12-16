package org.inqle.rdf.services.server;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.inqle.core.util.StackUtil;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.rdf.RDF;
import org.inqle.rdf.beans.IJenabean;
import org.inqle.rdf.services.IRdfObjectService;

import thewebsemantic.TypeWrapper;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * This service can be used if the client has access to bundle org.inqle.data.rdf.jenabean.
 * In that case, the client can send & receive persistable Jenabean objects directly from this service
 * @author David Donohue
 *
 */
public class RdfObjectService implements IRdfObjectService {

	private String serverId;

	private static Logger log = Logger.getLogger(RdfObjectService.class);
	
	/**
	 * Get a persistable object from the specified database and model
	 */
	public <T> T getObject(Class<T> objectClass, String objectId, String datamodelId) {
		Persister persister = Persister.getInstance();
		return persister.reconstitute(objectClass, objectId, datamodelId, true);
	}
	
	/**
	 * Get a persistable object, from the default datamodel for that class
	 * (as specified by the TargetDatabaseId and TargetModelName attributes)
	 */
	public <T> T getObject(Class<T> objectClass, String objectId) {
		Persister persister = Persister.getInstance();
		return persister.reconstitute(objectClass, objectId, true);
	}

	/**
	 * Get a list of all objects of the specified class, from the specified database and model
	 */
	public <T> Collection<T> listObjectsOfClass(Class<T> objectClass, String datamodelId) {
		Persister persister = Persister.getInstance();
		Model model = persister.getModel(datamodelId);
		return persister.reconstituteAll(objectClass, model);
	}
	
	/**
	 * Get a list of all persistable objects from the object's 
	 * default datamodel
	 * (as specified by the TargetDatabaseId and TargetModelName attributes)
	 * @param <T>
	 * @param objectClass
	 * @param databaseId
	 * @param modelName
	 * @return
	 */
	public <T> Collection<T> listObjectsOfClass(Class<T> objectClass) {
		Persister persister = Persister.getInstance();
		return persister.reconstituteAll(objectClass);
	}

	/**
	 * persist Jenabean object to specified database
	 * Return error message, or null if no error
	 */
	public <T> String storeObject(T objectToStore, String datamodelId) {
		String objectUri = TypeWrapper.instanceURI(objectToStore);
		if (objectUri==null) return "Fail to get URI for object";
		if (datamodelId==null) return "datamodelId is null";
		Persister persister = Persister.getInstance();
		Model model = persister.getModel(datamodelId);
		IndexWriter writer = persister.getIndexWriter(datamodelId);
		JenabeanIndexBuilder jib = new JenabeanIndexBuilder(writer, objectUri);
		model.register(jib);
		persister.persist(objectToStore, model);
		model.unregister(jib);
		return null;
	}
	
	/**
	 * persist Jenabean object to specified database
	 * The object class must have @TargetDatabaseId and @TargetModelName 
	 * attributes specified
	 * Return error message, or null if no error
	 */
	public <T> String storeObject(T objectToStore) {
		String datamodelId = RDF.getTargetDatamodelId(objectToStore.getClass());
		return storeObject(objectToStore, datamodelId);
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String containerId) {
		this.serverId = containerId;
	}

//	public <T> List<T> searchForObjects(SearchCriteria<T> searchCriteria, String modelId) {
//		String sparql = searchCriteria.getSparqlToFindObjects();
//		QueryCriteria qc = new QueryCriteria();
//		qc.setQuery(sparql);
//		List<String> uris = Queryer.selectUriList(qc);
//		
//	}

	public <T> List<T> searchForObjects(Class<T> objectClass, String queryTerm,
			String databaseId, String modelName) {
		// TODO Auto-generated method stub
		return null;
	}
}
