/**
 * 
 */
package org.inqle.test.data;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

/**
 * Test class to prove Jena saving & loading 
 * Jenabeans with array fields
 */
@Namespace("http://my-uri.com/ns/")
public class BeanWithArrays {

	private BeanWithString[] beanArray;
	private String id;

	@Id
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public BeanWithString[] getBeanArray() {
		return beanArray;
	}

	public void setBeanArray(BeanWithString[] beanArray) {
		this.beanArray = beanArray;
	}
}
