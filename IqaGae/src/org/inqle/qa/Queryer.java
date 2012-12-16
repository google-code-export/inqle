package org.inqle.qa;

import java.util.Collection;
import java.util.List;

public interface Queryer {

	public <T> void store(T object);
	public <T> void store(Collection<T> objects);
	public <T> List<T> getList(Class<T> classToQuery, String filterClause);
	public <T> T getObject(Class<T> classToQuery, String id);

}
