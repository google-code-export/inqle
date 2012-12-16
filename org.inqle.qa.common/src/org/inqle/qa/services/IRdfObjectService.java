package org.inqle.qa.services;

import java.util.Collection;
import java.util.List;

import org.inqle.ecf.common.IInqleEcfService;


public interface IRdfObjectService extends IInqleEcfService {
	
	public <T> T getObject(Class<T> objectClass, String objectId, String databaseId, String modelName);
	public <T> T getObject(Class<T> objectClass, String objectId);
	public <T> Collection<T> listObjectsOfClass(Class<T> objectClass, String datamodelId);
	public <T> Collection<T> listObjectsOfClass(Class<T> objectClass);
	public <T> String storeObject(T objectToStore, String datamodelId);
	public <T> String storeObject(T objectToStore);
	
	public <T> List<T> searchForObjects(Class<T> objectClass, String queryTerm, String databaseId, String modelName);
	
}
