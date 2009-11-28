package org.inqle.data.rdf.jena;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

@Namespace(RDF.INQLE)
public abstract class DatabaseBackedJenamodel extends Jenamodel {
	private String databaseId;
//	private String datamodelName;
	private String modelType;

	private static Logger log = Logger.getLogger(DatabaseBackedJenamodel.class);
	
	@Override
	@Id
	public String getId() {
		return getDatabaseId() + "/" + getModelType() + "/" + getName();
	}
	
	@Override
	public void setId(String id) {
		super.setId(id);
		setDatabaseId(getDatabaseIdFromDatamodelId(id));
		setModelType(getModelTypeFromDatamodelId(id));
		setName(getModelNameFromDatamodelId(id));
	}
	
//	public void clone(DatabaseBackedDatamodel objectToBeCloned) {
//		super.clone(objectToBeCloned);
//		//setConnection(objectToBeCloned.getConnection());
//		setDatabaseId(objectToBeCloned.getDatabaseId());
//	}
//	
//	public void replicate(DatabaseBackedDatamodel objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//	}

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

//	public void setDatamodelName(String datamodelName) {
//		this.datamodelName = datamodelName;
//	}

//	public String getDatamodelName() {
//		if (datamodelName == null) {
//			setDatamodelName(UUID.randomUUID().toString());
//		}
//		return datamodelName;
//	}
	
	@Override
	public String getName() {
		if (name == null) {
			setName(UUID.randomUUID().toString());
		}
		return name;
	}
	
	/**
	 * @return the type of model, e.g. "system" or "data"
	 * @return
	 */
	public String getModelType() {
		return modelType;
	}
	
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public static String getDatabaseIdFromDatamodelId(String datamodelId) {
		if (datamodelId.substring(datamodelId.length()-1).equals("/")) {
			log.error("datamodelId should not end with a slash.  Was '" + datamodelId + "'");
			return null;
		}
		if (datamodelId==null || datamodelId.indexOf("/") < 1) {
			log.error("datamodelId should be in the format 'database_name/model_type/model_name'.  Was '" + datamodelId + "'");
			return null;
		}
		String dbIdPlusType = datamodelId.substring(0, datamodelId.lastIndexOf("/"));
		if (dbIdPlusType==null || dbIdPlusType.indexOf("/") < 1) {
			log.error("datamodelId should be in the format 'database_name/model_type/model_name'.  Was '" + datamodelId + "'");
			return null;
		}
		
		return dbIdPlusType.substring(0, dbIdPlusType.lastIndexOf("/"));
	}

	public static String getModelTypeFromDatamodelId(String datamodelId) {
		if (datamodelId.substring(datamodelId.length()-1).equals("/")) {
			log.error("datamodelId should not end with a slash.  Was '" + datamodelId + "'");
			return null;
		}
		if (datamodelId==null || datamodelId.indexOf("/") < 1) {
			log.error("datamodelId should be in the format 'database_name/model_type/model_name'.  Was '" + datamodelId + "'");
			return null;
		}
		String dbIdPlusType = datamodelId.substring(0, datamodelId.lastIndexOf("/"));
		if (dbIdPlusType==null || dbIdPlusType.indexOf("/") < 1) {
			log.error("datamodelId should be in the format 'database_name/model_type/model_name'.  Was '" + datamodelId + "'");
			return null;
		}
		
		return dbIdPlusType.substring(dbIdPlusType.lastIndexOf("/") + 1);
	}

	public static String getModelNameFromDatamodelId(String datamodelId) {
			if (datamodelId==null || datamodelId.indexOf("/") < 1) {
				log.error("datamodelId should be in the format 'database_name/datamodel_name.  Was '" + datamodelId + "'");
				return null;
			}
			if (datamodelId.substring(datamodelId.length()-1).equals("/")) {
				log.error("datamodelId should not end with a slash.  Was '" + datamodelId + "'");
				return null;
			}
			return datamodelId.substring(datamodelId.lastIndexOf("/") + 1);
		}
	//	/**
	//	 * Given an instance of a Model, retrieve the Jena model
	//	 * @param datamodel
	//	 * @return
	//	 */
	//	public Model getModel(Datamodel datamodel) {
	//		if (datamodel == null) return null;
	//		//Model repositoryModel = getMetarepositoryModel();
	//		if (cachedModels.containsKey(datamodel.getId())) {
	//			return cachedModels.get(datamodel.getId());
	//		}
	//		if (datamodel instanceof DatabaseBackedDatamodel) {
	//			IDBConnector connector = DBConnectorFactory.getDBConnector(((DatabaseBackedDatamodel) datamodel).getDatabaseId());
	//			Model model = connector.getModel(datamodel.getId());
	//			cachedModels.put(datamodel.getId(), model);
	//			return model;
	//		}
	//		
	//		if (datamodel instanceof Datafile){
	//			return getModelFromFile(((Datafile)datamodel).getFileUrl());
	//		}
	//		
	//		//unknown type of Datamodel: return null
	//		return null;
	//	}
}
