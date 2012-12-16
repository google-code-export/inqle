package org.inqle.data.rdf.jenabean;

import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.uri.NamespaceMapping;
import org.inqle.rdf.RDF;
import org.inqle.rdf.annotations.TargetDatabaseId;
import org.inqle.rdf.annotations.TargetModelName;
import org.inqle.rdf.beans.UniqueJenabean;

import thewebsemantic.Namespace;

/**
 * This class represents INQLE servers.
 * 
 * @author David Donohue
 * Jul 14, 2008
 * 
 * TODO replace UniqueJenabean w/ a base class which does not have replicate or clone methods or name
 * or description
 */
@TargetDatabaseId(InqleInfo.CORE_DATABASE_ID)
@TargetModelName(Site.SITE_DATASET_ROLE_ID)
@Namespace(RDF.INQLE)
public class Site extends UniqueJenabean {

	public static final String SITE_DATASET_ROLE_ID = "_Sites";
	
	private String url;
	private String ip;
	private String ownerEmail;
	private NamespaceMapping uriPrefix;

//	public void clone(Site objectToBeCloned) {
//		setUrl(objectToBeCloned.getUrl());
//		setIp(objectToBeCloned.getIp());
//		setOwnerEmail(objectToBeCloned.getOwnerEmail());
//		setUriPrefix(objectToBeCloned.getUriPrefix());
//		super.clone(objectToBeCloned);
//	}
	
//	public Site createClone() {
//		Site site = new Site();
//		site.clone(this);
//		return site;
//	}
//
//	public Site createReplica() {
//		Site site = new Site();
//		site.replicate(this);
//		return site;
//	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public NamespaceMapping getUriPrefix() {
		return uriPrefix;
	}

	public void setUriPrefix(NamespaceMapping uriPrefix) {
		this.uriPrefix = uriPrefix;
	}

}
