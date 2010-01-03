package org.inqle.qa.services;

import java.util.Collection;

import org.inqle.ecf.common.IInqleEcfService;
import org.inqle.qa.beans.Question;

public interface IRdfObjectService extends IInqleEcfService {
	
	public <T> T getObject(Class<T> objectClass, String databaseId, String modelName, String objectId);
	public <T> T getObject(Class<T> objectClass, String objectId);
	public <T> Collection<T> listObjectsOfClass(Class<T> objectClass, String databaseId, String modelName);
	public <T> Collection<T> listObjectsOfClass(Class<T> objectClass);
	public <T> String storeObject(String databaseId, String modelName, T objectToStore);
	public <T> String storeObject(T objectToStore);
	
}
