package org.inqle.ui.rap.table;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.conversion.IConverter;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.util.QuerySolutionValueExtractor;
import org.inqle.data.rdf.jenabean.BasicJenabean;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * For databinding, can convert a QuerySolution, into a representative String value
 * @author David Donohue
 * Mar 13, 2008
 */
@Deprecated
public class ConverterQuerySolutionToString implements IConverter {

	static Logger log = Logger.getLogger(ConverterQuerySolutionToString.class);
	private String fieldName;
	
	public ConverterQuerySolutionToString(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object convert(Object fromObject) {
		QuerySolution querySolution = (QuerySolution)fromObject;
		return QuerySolutionValueExtractor.getDisplayable(querySolution, fieldName);
	}

	public Object getFromType() {
		try {
			return QuerySolution.class.newInstance();
		} catch (Exception e) {
			log.error("Unable to handle conversions from " + QuerySolution.class, e);
		}
		return null;
	}

	public Object getToType() {
		return new String();
	}

}
