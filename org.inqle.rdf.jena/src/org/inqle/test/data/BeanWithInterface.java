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
public class BeanWithInterface {

	private String id;
	
	private IInterfaceBean interfaceBean;

	private IInterfaceBean otherInterfaceBean;

	@Id
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public IInterfaceBean getInterfaceBean() {
		return interfaceBean;
	}

	public void setInterfaceBean(IInterfaceBean interfaceBean) {
		this.interfaceBean = interfaceBean;
	}
	
	public IInterfaceBean getOtherInterfaceBean() {
		return otherInterfaceBean;
	}

	public void setOtherInterfaceBean(IInterfaceBean otherInterfaceBean) {
		this.otherInterfaceBean = otherInterfaceBean;
	}

	
	
	
}
