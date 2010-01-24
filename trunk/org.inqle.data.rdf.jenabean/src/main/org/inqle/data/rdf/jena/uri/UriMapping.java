/**
 * 
 */
package org.inqle.data.rdf.jena.uri;

import thewebsemantic.Namespace;

import org.inqle.rdf.RDF;

/**
 * This class maps a string (e.g. a column header)
 * to a RDF123 expression.
 * @author David Donohue
 * May 30, 2008
 */
@Namespace(RDF.INQLE)
public class UriMapping {

	private String string;
	private String uri;
	
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
