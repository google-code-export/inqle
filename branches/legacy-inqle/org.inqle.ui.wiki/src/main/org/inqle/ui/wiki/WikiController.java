package org.inqle.ui.wiki;

import javax.servlet.http.HttpServletRequest;

import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.SubjectRetriever;
import org.inqle.http.lookup.util.HttpParameterParser;

import com.hp.hpl.jena.rdf.model.Model;

public class WikiController {

	public WikiController(HttpServletRequest req) {
		String datamodelId = HttpParameterParser.getParam(req, InqleInfo.PARAM_DATAMODEL);
		String rdfClass = HttpParameterParser.getParam(req, InqleInfo.PARAM_RDF_CLASS);
		String rdfSubject = HttpParameterParser.getParam(req, InqleInfo.PARAM_RDF_SUBJECT);
		
		Model subjectModel = SubjectRetriever.getSubject(datamodelId, rdfSubject);
		req.setAttribute(InqleInfo.PARAM_MODEL, subjectModel);
	}
}
