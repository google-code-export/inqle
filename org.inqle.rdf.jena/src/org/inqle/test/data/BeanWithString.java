/**
 * 
 */
package org.inqle.test.data;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

/**
 * Test class to prove Jena saving & loading 
 * Jenabeans with List fields
 */
@Namespace("http://my-uri.com/ns/")
public class BeanWithString {

	private String string;
	private String id;
	
	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
