package org.inqle.data.rdf.jenabean;

import thewebsemantic.Id;

public abstract class GlobalJenabean extends BasicJenabean implements IGlobalJenabean {

	@Id
	public String getId() {
		return super.getId();
	}
}
