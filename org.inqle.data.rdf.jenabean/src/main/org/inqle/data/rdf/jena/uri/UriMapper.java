/**
 * 
 */
package org.inqle.data.rdf.jena.uri;

import java.util.Collection;

import org.apache.log4j.Logger;
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
	
	private static Logger log = Logger.getLogger(UriMapper.class);
	
	private PrefixMapping prefixMapping = PrefixMapping.Extended;
	
	private UriMapper() {
		//Add to Jena's Extended PrefixMapping, all locally known prefix mappings
		Persister persister = Persister.getInstance();
		Collection<?> namespaces = persister.reconstituteAll(NamespaceMapping.class);
		for (Object namespaceObj: namespaces) {
			NamespaceMapping namespaceMapping = (NamespaceMapping) namespaceObj;
			if (prefixMapping.getNsPrefixURI(namespaceMapping.getPrefix()) != null) {
				log.warn("Multiple prefixes have been added of value '" + namespaceMapping.getPrefix() + "'.  Skipping <" + namespaceMapping.getUri() + ">; keeping <" + prefixMapping.getNsPrefixURI(namespaceMapping.getPrefix()) + ">");
			}
			prefixMapping.setNsPrefix(namespaceMapping.getPrefix(), namespaceMapping.getNamespaceUri());
		}
	}
	
	private static class UriMapperHolder {
		private final static UriMapper instance = new UriMapper();
	}
	
	public static UriMapper getInstance() {
		return UriMapperHolder.instance;
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
