/**
 * 
 */
package org.inqle.test.data;

import java.util.Collection;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

/**
 * Test class to prove Jena saving & loading 
 * Jenabeans with List fields
 */
@Namespace("http://my-uri.com/ns/")
public class BeanWithCollections {

	private Collection<String> strings1;
	private Collection<String> strings2;
	private String id;

	public Collection<String> getStrings1() {
		return strings1;
	}

	public void setStrings1(Collection<String> stringList) {
		this.strings1 = stringList;
	}

	@Id
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Collection<String> getStrings2() {
		return strings2;
	}

	public void setStrings2(Collection<String> strings2) {
		this.strings2 = strings2;
	}
	
	
}
