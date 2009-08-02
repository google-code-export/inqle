/**
 * 
 */
package org.inqle.data.rdf.jena.uri;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.CloneableJenabean;
import org.inqle.data.rdf.jenabean.GlobalCloneableJenabean;
import org.inqle.data.rdf.jenabean.ICloneableJenabean;

import thewebsemantic.Namespace;

/**
 * Represents a URI namespace
 * @author David Donohue
 * May 31, 2008
 */
@TargetDatamodel(NamespaceMapping.NAMESPACE_MAPPING_DATASET)
@Namespace(RDF.INQLE)
public class NamespaceMapping extends GlobalCloneableJenabean {

	private static final String NAMESPACE_MAPPING_DATASET = "org.inqle.datamodels.ns";
	private String namespaceAbbrev;
	private String namespaceUri;

	public void setNamespaceAbbrev(String namespaceAbbrev) {
		this.namespaceAbbrev = namespaceAbbrev;
	}
	public String getNamespaceUri() {
		return namespaceUri;
	}
	public void setNamespaceUri(String namespaceUri) {
		this.namespaceUri = namespaceUri;
	}
	public String getStringRepresentation() {
		return namespaceAbbrev + "=<" + namespaceUri + ">";
	}
	public void clone(NamespaceMapping objectToBeCloned) {
		setNamespaceAbbrev(objectToBeCloned.getNamespaceAbbrev());
		setNamespaceUri(objectToBeCloned.getUri());
	}
	public ICloneableJenabean createClone() {
		NamespaceMapping ns = new NamespaceMapping();
		ns.clone(this);
		return ns;
	}
	public String getNamespaceAbbrev() {
		return namespaceAbbrev;
	}
}
