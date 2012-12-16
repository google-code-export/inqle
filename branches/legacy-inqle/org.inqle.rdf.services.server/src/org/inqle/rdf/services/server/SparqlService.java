package org.inqle.rdf.services.server;

import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.rdf.services.ISparqlService;

public class SparqlService implements ISparqlService {

	private String serverId;

	public String querySelect(String query, String modelId) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(query);
		queryCriteria.addDatamodel(modelId);
		return Queryer.selectXml(queryCriteria);
	}

	public String echoQuery(String query, String modelId) {
		return "querying sparql: " + query + " on model: " + modelId;
	}
	
	public void setServerId(String containerId) {
		this.serverId = containerId;
	}

	public String getServerId() {
		return this.serverId;
	}
}
