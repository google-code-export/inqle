package org.inqle.test.data;

import thewebsemantic.Id;

public class InterfaceBean implements IInterfaceBean {

	private String message;
	private String id;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Id
	public String getId() {
		return id;
	}
}
