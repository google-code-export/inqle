package org.inqle.ui.rap.wiki.parts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.data.rdf.jena.Datamodel;

import thewebsemantic.TypeWrapper;
import thewebsemantic.ValuesContext;

public class WikiDataExtractor {

	private String title;
	private String description;
	private Datamodel datamodel;
	private String classUri;
	private String objectUri;
	private List<String> statements;

	private static final Logger log = Logger.getLogger(WikiDataExtractor.class);
	
	public WikiDataExtractor(Datamodel datamodel, Object obj) {
		this.datamodel = datamodel;
		log.info("obj=" + obj);
		statements = new ArrayList<String>();
		if (obj instanceof INamedAndDescribed) {
			INamedAndDescribed namedObj = (INamedAndDescribed)obj;
			setTitle(namedObj.getName());
			log.info("INamedAndDescribed title=" + namedObj.getName());
			setDescription(namedObj.getDescription());
			
//			Model model = persister.getModel(datamodel);
			TypeWrapper wrapper = TypeWrapper.type(obj);
			setClassUri(wrapper.typeUri());
			setObjectUri(wrapper.uri(obj));
			ValuesContext[] contexts = TypeWrapper.valueContexts(obj);
			for (ValuesContext context: contexts) {
				String propertyUri = context.uri();
				Object value = context.invokeGetter();
				statements.add("<" + propertyUri + "> " + value);
			}
		}
		log.info("Statements =" + statements);
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

	public List<String> getStatements() {
		return statements;
	}

	public void setStatements(List<String> statements) {
		this.statements = statements;
	}

}
