package org.inqle.qa;

import java.util.Collection;

public interface Storer {

	public <T> void store(T object);
	public <T> void store(Collection<T> objects);
}
