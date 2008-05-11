package org.inqle.data.rdf.jenabean;

import org.inqle.core.util.JavaHasher;

import thewebsemantic.Id;

public abstract class GlobalJenabean extends BasicJenabean implements IGlobalJenabean {

//	@Id
//	public String getId() {
//		return super.getId();
//	}
	@Id
	@Override
	public String getId() {
		String hash = JavaHasher.hashSha256(toString());
		return hash;
	}
}
