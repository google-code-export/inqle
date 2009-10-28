/**
 * 
 */
package org.inqle.data.rdf.jena.uri;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.TargetDatabaseId;
import org.inqle.data.rdf.jenabean.TargetDatamodelName;

import thewebsemantic.Namespace;

/**
 * Represents a URI namespace
 * @author David Donohue
 * May 31, 2008
 */
@TargetDatabaseId(Persister.CORE_DATABASE_ID)
@TargetDatamodelName(NamespaceMapping.NAMESPACE_MAPPING_DATASET)
@Namespace(RDF.INQLE)
public class NamespaceMapping extends GlobalJenabean {

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
//	public void clone(NamespaceMapping objectToBeCloned) {
//		setNamespaceAbbrev(objectToBeCloned.getNamespaceAbbrev());
//		setNamespaceUri(objectToBeCloned.getUri());
//	}
//	public INamedAndDescribedJenabean createClone() {
//		NamespaceMapping ns = new NamespaceMapping();
//		ns.clone(this);
//		return ns;
//	}
	public String getNamespaceAbbrev() {
		return namespaceAbbrev;
	}
}
