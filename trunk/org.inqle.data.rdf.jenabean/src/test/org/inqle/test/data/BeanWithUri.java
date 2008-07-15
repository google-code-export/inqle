package org.inqle.test.data;

import java.net.URI;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

import org.inqle.data.rdf.RDF;

@Namespace(RDF.INQLE)
public class BeanWithUri {

	private String id;
	private URI uriField;
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public URI getUriField() {
		return uriField;
	}
	public void setUriField(URI uriField) {
		this.uriField = uriField;
	}
}
