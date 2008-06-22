package org.inqle.data.rdf;

import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

/**
 * This class contains configuration info for an INQLE server.
 * In particular it is used to contain connection info for the Repository Model, which 
 * contains info about each data repository attached to this server
 * @author David Donohue
 * Dec 4, 2007
 */
@Namespace(RDF.INQLE)
public class AppInfo extends UniqueJenabean {
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String APPINFO_INSTANCE_ID = "AppInfoInstance";
	private String serverId;
	private String serverBaseUrl;
	private Dataset metarepositoryDataset;
	private Connection metarepositoryConnection;
	
	@Id
	public String getId() {
		return APPINFO_INSTANCE_ID;
	}
	
	public String getServerId() {
		return serverId;
	}
	
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	
	public String getServerBaseUrl() {
		return this.serverBaseUrl;
	}
	
	public void setServerBaseUrl(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
	}

	public Dataset getMetarepositoryDataset() {
		return metarepositoryDataset;
	}
	
	public void setMetarepositoryDataset(Dataset metarepositoryDataset) {
		this.metarepositoryDataset = metarepositoryDataset;
	}

//	public NamedModel getLogNamedModel() {
//		return logNamedModel;
//	}
//
//	public void setLogNamedModel(NamedModel logNamedModel) {
//		this.logNamedModel = logNamedModel;
//	}
	
	public void clone(AppInfo objectToBeCloned) {
		setServerBaseUrl(objectToBeCloned.getServerBaseUrl());
		setMetarepositoryDataset(objectToBeCloned.getMetarepositoryDataset());
		setMetarepositoryConnection(objectToBeCloned.getMetarepositoryConnection());
		super.clone(objectToBeCloned);
	}
	
	public void replicate(AppInfo objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
		super.replicate(objectToClone);
	}
	
	public AppInfo createClone() {
		AppInfo newAppInfo = new AppInfo();
		newAppInfo.clone(this);
		return newAppInfo;
	}

	public AppInfo createReplica() {
		AppInfo newAppInfo = new AppInfo();
		newAppInfo.replicate(this);
		return newAppInfo;
	}

	public Connection getMetarepositoryConnection() {
		return metarepositoryConnection;
	}

	public void setMetarepositoryConnection(Connection metarepositoryConnection) {
		this.metarepositoryConnection = metarepositoryConnection;
	}

}
