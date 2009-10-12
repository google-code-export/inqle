package org.inqle.core.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * This class represents a globally-recognizable object.  The ID of this object
 * will always be the same, provided that the object is recreated identiclally 
 * each time.  Example: a question option with translation "true" should always have the same id.
 * @author David Donohue
 * 2009/10/12
 *
 */
public abstract class GlobalModelObject extends ModelObject {

	@Override
	public String getId() {
//		String hash = JavaHasher.hashSha256(getDefiningStringRepresentation());
//		return hash;
		try {
			return URLEncoder.encode(getDefiningStringRepresentation(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * Represent this object as a string, such that it can be recognized
	 * as itself when recreated elsewhere 
	 * Be sure to exclude the ID from this string.
	 * @return
	 */
	public abstract String getDefiningStringRepresentation();
	
	
}
