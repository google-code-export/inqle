/**
 * 
 */
package org.inqle.data.rdf.jena.uri;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.rdf.RDF;

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
	
	private PrefixMapping prefixMapping;
	
	private UriMapper() {
		//Start with Jena's Extended PrefixMapping, which knows about major namespaces like FOAF
		prefixMapping = PrefixMapping.Factory.create();
		prefixMapping.withDefaultMappings(PrefixMapping.Extended);
		
		//add INQLE's prefix mapping
		prefixMapping.setNsPrefix("inqle", RDF.INQLE);
		
		//add the default NamespaceMapping for this server
		Persister persister = Persister.getInstance();
		NamespaceMapping defaultMapping = persister.getAppInfo().getSite().getUriPrefix();
		prefixMapping.setNsPrefix(defaultMapping.getNamespaceAbbrev(), defaultMapping.getNamespaceUri());
		
		//add namespaces stored in the prefixes directory, including UMBEL and some related schemas
		addMappings(persister.getPrefixesModel());
		
		//add any NamespaceMapping objects stored in the corresponding system datamodel
		Collection<?> namespaces = persister.reconstituteAll(NamespaceMapping.class);
		for (Object namespaceObj: namespaces) {
			NamespaceMapping namespaceMapping = (NamespaceMapping) namespaceObj;
			if (prefixMapping.getNsPrefixURI(namespaceMapping.getNamespaceAbbrev()) != null) {
				log.warn("Multiple prefixes have been added of value '" + namespaceMapping.getNamespaceAbbrev() + "'.  Skipping <" + namespaceMapping.getUri() + ">; keeping <" + prefixMapping.getNsPrefixURI(namespaceMapping.getNamespaceAbbrev()) + ">");
			}
			prefixMapping.setNsPrefix(namespaceMapping.getNamespaceAbbrev(), namespaceMapping.getNamespaceUri());
		}
	}
	
	/**
	 * Add any prefix mappings from any Models or other PrefixMapping classes
	 * @param prefixMapping
	 */
	public void addMappings(PrefixMapping modelOrOtherMapping) {
		prefixMapping.withDefaultMappings(modelOrOtherMapping);
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
