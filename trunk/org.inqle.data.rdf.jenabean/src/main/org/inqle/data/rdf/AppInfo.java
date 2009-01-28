package org.inqle.data.rdf;

import java.util.ArrayList;
import java.util.Collection;

import org.inqle.data.rdf.jena.SDBDatabase;
import org.inqle.data.rdf.jena.SystemDatamodel;
import org.inqle.data.rdf.jenabean.Site;
import org.inqle.data.rdf.jenabean.UniqueJenabean;
import org.inqle.data.rdf.jenabean.UserAccount;

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
	//private String serverId;
//	private String serverBaseUrl;
//	private SystemDatamodel metarepositoryDataset;
//	private SDBDatabase internalConnection;
	private Site site;
	private Collection<UserAccount> adminAccounts = new ArrayList<UserAccount>();
	
	@Id
	public String getId() {
		return APPINFO_INSTANCE_ID;
	}
	
//	public String getServerBaseUrl() {
//		return this.serverBaseUrl;
//	}
//	
//	public void setServerBaseUrl(String serverBaseUrl) {
//		this.serverBaseUrl = serverBaseUrl;
//	}

//	public SystemDatamodel getMetarepositoryDataset() {
//		return metarepositoryDataset;
//	}
//	
//	public void setMetarepositoryDataset(SystemDatamodel metarepositoryDataset) {
//		this.metarepositoryDataset = metarepositoryDataset;
//	}

//	public NamedModel getLogNamedModel() {
//		return logNamedModel;
//	}
//
//	public void setLogNamedModel(NamedModel logNamedModel) {
//		this.logNamedModel = logNamedModel;
//	}
	
	public void clone(AppInfo objectToBeCloned) {
//		setServerBaseUrl(objectToBeCloned.getServerBaseUrl());
//		setMetarepositoryDataset(objectToBeCloned.getMetarepositoryDataset());
//		setInternalConnection(objectToBeCloned.getInternalConnection());
		setAdminAccounts(objectToBeCloned.getAdminAccounts());
		setSite(objectToBeCloned.getSite());
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

//	public SDBDatabase getInternalConnection() {
//		return internalConnection;
//	}
//
//	public void setInternalConnection(SDBDatabase internalConnection) {
//		this.internalConnection = internalConnection;
//	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void addAdminAccount(UserAccount adminAccount) {
		adminAccounts.add(adminAccount);
	}
	
	public Collection<UserAccount> getAdminAccounts() {
		return adminAccounts;
	}

	public void setAdminAccounts(Collection<UserAccount> adminAccounts) {
		this.adminAccounts = adminAccounts;
	}

}
