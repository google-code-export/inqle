/**
 * 
 */
package org.inqle.data.rdf.jena.uri;
import org.inqle.core.util.InqleInfo;
import org.inqle.rdf.RDF;
import org.inqle.rdf.annotations.TargetDatabaseId;
import org.inqle.rdf.annotations.TargetModelName;
import org.inqle.rdf.beans.GlobalJenabean;

import thewebsemantic.Namespace;

/**
 * Represents a URI namespace
 * @author David Donohue
 * May 31, 2008
 */
@TargetDatabaseId(InqleInfo.CORE_DATABASE_ID)
@TargetModelName(NamespaceMapping.NAMESPACE_MAPPING_DATASET)
@Namespace(RDF.INQLE)
public class NamespaceMapping extends GlobalJenabean {

	private static final String NAMESPACE_MAPPING_DATASET = "_NameSpaces";
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
