package org.inqle.qa.server.services;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.StackUtil;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.qa.beans.Question;
import org.inqle.qa.services.IRdfObjectService;

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
	 * @param <T>
	 * @param objectClass
	 * @param databaseId
	 * @param modelName
	 * @param objectId
	 * @return
	 */
	public <T> T getObject(Class<T> objectClass, String databaseId, String modelName, String objectId) {
		String datamodelId = Persister.getDatamodelId(databaseId, IDBConnector.SUBDATABASE_DATA, modelName);
		Persister persister = Persister.getInstance();
		return persister.reconstitute(objectClass, objectId, datamodelId, true);
	}
	
	/**
	 * Get a persistable object, from the default datamodel for that class
	 * (as specified by the TargetDatabaseId and TargetModelName attributes)
	 * @param <T>
	 * @param objectClass
	 * @param databaseId
	 * @param modelName
	 * @param objectId
	 * @return
	 */
	public <T> T getObject(Class<T> objectClass, String objectId) {
		Persister persister = Persister.getInstance();
		return persister.reconstitute(objectClass, objectId, true);
	}

	/**
	 * Get a list of all objects of the specified class, from the specified database and model
	 * @param <T>
	 * @param objectClass
	 * @param databaseId
	 * @param modelName
	 * @return
	 */
	public <T> Collection<T> listObjectsOfClass(Class<T> objectClass, String databaseId, String modelName) {
		String datamodelId = Persister.getDatamodelId(databaseId, IDBConnector.SUBDATABASE_DATA, modelName);
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
	public <T> String storeObject(String databaseId, String modelName, T objectToStore) {
		String datamodelId = Persister.getDatamodelId(databaseId, IDBConnector.SUBDATABASE_DATA, modelName);
		Persister persister = Persister.getInstance();
		persister.persist(objectToStore, datamodelId);
		return null;
	}
	
	/**
	 * persist Jenabean object to specified database
	 * The object class must have @TargetDatabaseId and @TargetModelName 
	 * attributes specified
	 * Return error message, or null if no error
	 */
	public <T> String storeObject(T objectToStore) {
		String errorMessage = null;
		try {
			Persister persister = Persister.getInstance();
			persister.persist(objectToStore);
		} catch (Exception e) {
			errorMessage = StackUtil.exceptionToString(e);
		}
		return errorMessage;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String containerId) {
		this.serverId = containerId;
	}
}
