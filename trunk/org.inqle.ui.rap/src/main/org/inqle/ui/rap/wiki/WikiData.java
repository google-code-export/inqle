package org.inqle.ui.rap.wiki;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Datamodel;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class WikiData {

	private String title;
	private String description;
	private Datamodel datamodel;
	private String classUri;
	private Map<String, Object> properties;
	private Model model;
	private String subjectUri;
	private Resource subject;
	private List<String> tags = new ArrayList<String>();
	private String parentClassUri;

	private static final Logger log = Logger.getLogger(WikiData.class);
	private static final String URI_WIKI_PAGE_TITLE = RDF.INQLE + "wikiPageTitle";
	private static final String URI_WIKI_PAGE_TEXT = RDF.INQLE + "wikiPageText";
	private static final Property PROPERTY_WIKI_PAGE_TITLE = ResourceFactory.createProperty(URI_WIKI_PAGE_TITLE);
	private static final Property PROPERTY_WIKI_PAGE_TEXT = ResourceFactory.createProperty(URI_WIKI_PAGE_TEXT);
	
	/**
	 * Contains the Jena model, which in turn contains relevant info to be
	 * rendered in the wiki page.  2 modes: 
	 * (1) subject instance URI is known.  This object yields title, description,
	 * links relevant to that subject.
	 * (2) class URI is known.  This object yields list of objects within the 
	 * class.
	 * @param model
	 * @param subjectUri
	 */
	public WikiData(Model model, String subjectUri) {
		if (model==null) {
			return;
		}
		this.model = model;
		if (subjectUri==null) {
			this.subjectUri = null;
			subject = null;
			parentClassUri = RDF.RDFS + "Class";
		} else {
			this.subjectUri = subjectUri;
			subject = model.getResource(subjectUri);
			parentClassUri = null;
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		if (title==null) {
			if (subject.hasProperty(PROPERTY_WIKI_PAGE_TITLE)) {
				title = subject.getProperty(PROPERTY_WIKI_PAGE_TITLE).getString();
				return title;
			}
			
			if (subject.hasProperty(RDF.LABEL_PROPERTY)) {
				title = subject.getProperty(RDF.LABEL_PROPERTY).getString();
				return title;
			}
			
			title = subject.getURI();
		}
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		if (description==null) {
			if (subject.hasProperty(PROPERTY_WIKI_PAGE_TEXT)) {
				description = subject.getProperty(PROPERTY_WIKI_PAGE_TEXT).getString();
				return description;
			}
			
			if (subject.hasProperty(RDF.LABEL_PROPERTY)) {
				description = subject.getProperty(RDF.COMMENT_PROPERTY).getString();
				return description;
			}
		}
		return description;
	}
	
	public List<String> getTags() {
		if (tags==null) {
			
		}
		return tags;
	}

	public void setClassUri(String classUri) {
		this.classUri = classUri;
	}

	public String getClassUri() {
		return classUri;
	}

}

/*
//try to reconstitute the object as a Jenabean
Persister persister = Persister.getInstance();
Object obj = persister.reconstitute();
this.datamodel = datamodel;
//log.info("obj=" + obj);
properties = new HashMap<String, Object>();
if (obj instanceof INamedAndDescribed) {
	INamedAndDescribed namedObj = (INamedAndDescribed)obj;
	setTitle(namedObj.getName());
//	log.info("INamedAndDescribed title=" + namedObj.getName());
	setDescription(namedObj.getDescription());
	
//	Model model = persister.getModel(datamodel);
	TypeWrapper wrapper = TypeWrapper.type(obj);
	setClassUri(wrapper.typeUri());
	setObjectUri(wrapper.uri(obj));
	ValuesContext[] contexts = TypeWrapper.valueContexts(obj);
	for (ValuesContext context: contexts) {
		String propertyUri = context.uri();
		Object value = context.invokeGetter();
		properties.put(propertyUri, value);
	}
}
log.trace("Properties = " + properties);
*/