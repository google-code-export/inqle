/**
 * 
 */
package org.inqle.data.rdf.jena.uri;

import java.util.Collection;

import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.util.FileUtils;

/**
 * Maps URIs to short form of URI and vice versa
 * also maps prefixes
 * @author David Donohue
 * May 31, 2008
 */
public class UriMapper {
	private PrefixMapping prefixMapping = PrefixMapping.Extended;
	
	public UriMapper() {
		//Add to Jena's Extended PrefixMapping, all locally known prefix mappings
		Persister persister = Persister.getInstance();
		Collection<?> namespaces = persister.reconstituteAll(LocalNamespace.class);
		for (Object namespaceObj: namespaces) {
			LocalNamespace namespace = (LocalNamespace) namespaceObj;
			prefixMapping.setNsPrefix(namespace.getPrefix(), namespace.getNamespaceUri());
		}
	}
	
	/**
	 * Retrieve the URI of the provided qname
	 * @param qname, e.g. foaf:mbox
	 * @return the URI, e.g. http://xmlns.com/foaf/0.1/mbox
	 */
	public String	getUri(String qname) {
		return prefixMapping.expandPrefix(qname);
	}
	
	/**
	 * Retrieve the (prefixed) QName of the provided URI
	 * @param uri, e.g. http://xmlns.com/foaf/0.1/mbox
	 * @return the qname, e.g. foaf:mbox
	 */
	public String	getQname(String uri) {
		return prefixMapping.shortForm(uri);
	}
	
	public static boolean isUri(String possibleUri) {
		return FileUtils.isURI(possibleUri);
	}
	
}
