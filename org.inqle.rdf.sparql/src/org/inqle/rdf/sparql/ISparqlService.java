package org.inqle.rdf.sparql;

import org.inqle.ecf.common.IInqleEcfService;

public interface ISparqlService extends IInqleEcfService {

	public String echoQuery(String query, String modelId);
	public String querySelect(String query, String modelId);
}
