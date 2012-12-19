package org.inqle.test.restlet;
import org.inqle.qs.Constants;
// Document.java
import org.openrdf.annotations.Iri;

@Iri(Constants.NS + "Document")
public class Document {
	@Iri(Constants.NS + "title") 
	String title;
	
	@Iri(Constants.NS + "body") 
	String body;
	
	public Document() {}
	
	public Document(String title, String body) {
		this.title = title;
		this.body = body;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

public boolean equals(Document other) {
	if (this.title == null |! this.title.equals(other.getTitle())) {
		return false;
	}
	if (this.body == null |! this.body.equals(other.getBody())) {
		return false;
	}
	return true;
}
}