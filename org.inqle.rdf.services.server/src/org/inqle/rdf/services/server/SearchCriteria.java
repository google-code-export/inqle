package org.inqle.rdf.services.server;

import java.io.Serializable;

import org.inqle.rdf.RDF;

public class SearchCriteria<T> implements Serializable {
	private static final long serialVersionUID = 3446929423774683055L;
	private static final int DEFAULT_COUNT = 10;
	private Class<T> classToSearchFor;
	private String searchTerm;
	private String sortPhrase = "DESC(?score)";
	private int offset = 0;
	private int count = DEFAULT_COUNT;
	
	public SearchCriteria(Class<T> classToSearchFor) {
		this.classToSearchFor = classToSearchFor;
	}
	
	public Class<T> getClassToSearchFor() {
		return classToSearchFor;
	}

	public void setClassToSearchFor(Class<T> classToSearchFor) {
		this.classToSearchFor = classToSearchFor;
	}

	public void setSortPhrase(String sortPhrase) {
		this.sortPhrase = sortPhrase;
	}

	public String getSortPhrase() {
		return sortPhrase;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public String getSearchTerm() {
		return searchTerm;
	}
	
	/**
	 * Get SPARQL which will lookup the URI of objects of the target class.
	 * Requires that upon indexing the objects of the target class in the first place,
	 * any identifying text should be associated by the object (e.g. using a custom IndexBuilder).
	 * @param limit
	 * @param offset
	 * @return the SPARQL query
	 * 
	 * TODO make custom IndexBuilder
	 */
	public String getSparqlToFindObjects() {
		String s = "SELECT ?uri { GRAPH ?g {\n" +
				"?uri a <" + RDF.classUri(getClassToSearchFor()) + "> . \n";
		s += "(?uri ?score) <" + RDF.TEXT_MATCH + "> \"" + searchTerm.replaceAll("\"", "") + "\" \n";
		s += "}} \n";
		s += "ORDER BY " + getSortPhrase() + " \n";
		s += "LIMIT " + getCount() + " \n";
		s += "OFFSET " + getOffset() + " \n";
		return s;
	}
	
	
}
