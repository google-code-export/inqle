package org.inqle.data.rdf.jenabean;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;

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
@TargetDataset(Site.SITE_DATASET_ROLE_ID)
@Namespace(RDF.INQLE)
public class Site extends UniqueJenabean {

	public static final String SITE_DATASET_ROLE_ID = "org.inqle.datasets.site";
	
	private String url;
	private String ip;
	private String ownerEmail;
//	public String getStringRepresentation() {
//		String s = getClass().toString() + " {\n";
//		s += "[id=" + siteId + "]\n";
//		s += "}";
//		return s;
//	}

	private String uriPrefix;

	public void clone(Site objectToBeCloned) {

		super.clone(objectToBeCloned);
	}
	
	public Site createClone() {
		Site site = new Site();
		site.clone(this);
		return site;
	}

	public Site createReplica() {
		Site site = new Site();
		site.replicate(this);
		return site;
	}

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

	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}

	public String getUriPrefix() {
		return uriPrefix;
	}	

}
