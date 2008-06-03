/**
 * 
 */
package org.inqle.data.rdf.jena.uri;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

import thewebsemantic.Namespace;

/**
 * Represents a URI namespace
 * @author David Donohue
 * May 31, 2008
 */
@Namespace(RDF.INQLE)
public class LocalNamespace extends GlobalJenabean {

	private String prefix;
	private String namespaceUri;
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getNamespaceUri() {
		return namespaceUri;
	}
	public void setNamespaceUri(String namespaceUri) {
		this.namespaceUri = namespaceUri;
	}
	public String getStringRepresentation() {
		return prefix + "=<" + namespaceUri + ">";
	}
	public void clone(LocalNamespace objectToBeCloned) {
		setPrefix(objectToBeCloned.getPrefix());
		setNamespaceUri(objectToBeCloned.getUri());
	}
	public IBasicJenabean createClone() {
		LocalNamespace ns = new LocalNamespace();
		ns.clone(this);
		return ns;
	}
}
