package org.inqle.qa.common;

import java.util.UUID;

public abstract class QAModelObject {

	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		if (id==null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}
	
}
