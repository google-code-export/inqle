package org.inqle.core.data;

import java.util.UUID;

public abstract class ModelObject {

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
