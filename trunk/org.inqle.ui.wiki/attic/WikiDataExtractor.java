package org.inqle.ui.wiki;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.TypeWrapper;
import thewebsemantic.ValuesContext;

public class WikiDataExtractor {

	private String title;
	private String description;
	private Datamodel datamodel;
	private String classUri;
	private String objectUri;
	private Map<String, Object> properties;

	private static final Logger log = Logger.getLogger(WikiDataExtractor.class);
	
	public WikiDataExtractor(Datamodel datamodel, String subjectClassUri, String subjectInstanceUri) {
		if (subjectClassUri==null) {
			//figure out the class URI
			
		}
		//try to reconstitute the object as a Jenabean
		Persister persister = Persister.getInstance();
		Object obj = persister.reconstitute();
		this.datamodel = datamodel;
//		log.info("obj=" + obj);
		properties = new HashMap<String, Object>();
		if (obj instanceof INamedAndDescribed) {
			INamedAndDescribed namedObj = (INamedAndDescribed)obj;
			setTitle(namedObj.getName());
//			log.info("INamedAndDescribed title=" + namedObj.getName());
			setDescription(namedObj.getDescription());
			
//			Model model = persister.getModel(datamodel);
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
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		if (title==null) return "";
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		if (description==null) return "";
		return description;
	}

	public void setObjectUri(String objectUri) {
		this.objectUri = objectUri;
	}

	public String getObjectUri() {
		if (objectUri==null) return "";
		return objectUri;
	}

	public void setClassUri(String classUri) {
		this.classUri = classUri;
	}

	public String getClassUri() {
		return classUri;
	}

	public String getType() {
		return getObjectUri();
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
