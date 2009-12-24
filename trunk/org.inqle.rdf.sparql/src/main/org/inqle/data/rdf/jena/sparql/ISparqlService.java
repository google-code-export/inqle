package org.inqle.data.rdf.jena.sparql;

import org.inqle.ecf.common.IInqleEcfService;

public interface ISparqlService extends IInqleEcfService {

	public String querySelect(String query, String modelId);
}
