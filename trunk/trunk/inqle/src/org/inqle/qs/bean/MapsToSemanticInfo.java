package org.inqle.qs.bean;

import org.openrdf.model.URI;

public interface MapsToSemanticInfo {
	public URI getMapsSubject();
	public void setMapsSubject(URI subject);
	public URI getMapsPredicate();
	public void setMapsPredicate(URI predicate);
	public URI getMapsNegationPredicate();
	public void setMapsNegationPredicate(URI negationPredicate);
}
