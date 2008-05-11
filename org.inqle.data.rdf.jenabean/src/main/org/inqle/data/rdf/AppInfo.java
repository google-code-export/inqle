package org.inqle.data.rdf;

import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
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
	public static final String APPINFO_INSTANCE = "AppInfoInstance";
	//private String serverBaseUri;
	private String serverBaseUrl;
	private NamedModel repositoryNamedModel;
	private NamedModel logNamedModel;
	
	@Id
	public String getId() {
		return APPINFO_INSTANCE;
	}
	
	/*
	public String getServerBaseUri() {
		return this.serverBaseUri;
	}
	
	public void setServerBaseUri(String serverBaseUri) {
		this.serverBaseUri = serverBaseUri;
	}
	*/
	
	public String getServerBaseUrl() {
		return this.serverBaseUrl;
	}
	
	public void setServerBaseUrl(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
	}

	public NamedModel getRepositoryNamedModel() {
		return this.repositoryNamedModel;
	}
	
	public void setRepositoryNamedModel(NamedModel repositoryNamedModel) {
		this.repositoryNamedModel = repositoryNamedModel;
	}

	public NamedModel getLogNamedModel() {
		return logNamedModel;
	}

	public void setLogNamedModel(NamedModel logNamedModel) {
		this.logNamedModel = logNamedModel;
	}
	
	public void clone(AppInfo objectToBeCloned) {
		setServerBaseUrl(objectToBeCloned.getServerBaseUrl());
		setRepositoryNamedModel(objectToBeCloned.getRepositoryNamedModel());
		setLogNamedModel(objectToBeCloned.getLogNamedModel());
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

}
