/**
 * 
 */
package org.inqle.data.rdf.jena.uri;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

import thewebsemantic.Namespace;

/**
 * Represents a URI namespace
 * @author David Donohue
 * May 31, 2008
 */
@TargetDataset(NamespaceMapping.NAMESPACE_MAPPING_DATASET)
@Namespace(RDF.INQLE)
public class NamespaceMapping extends GlobalJenabean {

	private static final String NAMESPACE_MAPPING_DATASET = "org.inqle.datasets.ns";
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
	public void clone(NamespaceMapping objectToBeCloned) {
		setPrefix(objectToBeCloned.getPrefix());
		setNamespaceUri(objectToBeCloned.getUri());
	}
	public IBasicJenabean createClone() {
		NamespaceMapping ns = new NamespaceMapping();
		ns.clone(this);
		return ns;
	}
}
