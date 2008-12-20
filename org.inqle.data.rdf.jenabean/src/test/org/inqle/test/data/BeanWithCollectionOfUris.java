package org.inqle.test.data;

import java.net.URI;
import java.util.Collection;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

import org.inqle.data.rdf.RDF;

@Namespace(RDF.INQLE)
public class BeanWithCollectionOfUris {

	private String id;
	private Collection<URI> uriCollection;
	
	@Id
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Collection<URI> getUriCollection() {
		return uriCollection;
	}
	
	public void setUriCollection(Collection<URI> uriCollection) {
		this.uriCollection = uriCollection;
	}
	
}
