package org.inqle.qs.bean;

import static org.inqle.qs.Constants.NS_Q;

import org.openrdf.annotations.Iri;
import org.openrdf.model.URI;
import org.openrdf.repository.object.LangString;

@Iri(NS_Q)
public class Question implements MapsToSemanticInfo {
	private LangString title;
	private LangString question;
	/**
	 * URI if the class of Data, to which answers map
	 */
	private URI mapsSubject;
	private URI mapsPredicate;
	private URI mapsNegationPredicate;
	
	
	public LangString getTitle() {
		return title;
	}
	public void setTitle(LangString title) {
		this.title = title;
	}
	public LangString getQuestion() {
		return question;
	}
	public void setQuestion(LangString question) {
		this.question = question;
	}
	public URI getMapsSubject() {
		return mapsSubject;
	}
	public void setMapsSubject(URI mapsSubject) {
		this.mapsSubject = mapsSubject;
	}
	@Override
	public URI getMapsPredicate() {
		return mapsPredicate;
	}
	@Override
	public void setMapsPredicate(URI mapsPredicate) {
		this.mapsPredicate = mapsPredicate;
	}
	public URI getMapsNegationPredicate() {
		return mapsNegationPredicate;
	}
	public void setMapsNegationPredicate(URI mapsNegationPredicate) {
		this.mapsNegationPredicate = mapsNegationPredicate;
	}
	
}
