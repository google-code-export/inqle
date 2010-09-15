package org.inqle.qa;

public interface GenericLocalizedObjectFactory {

	public <T> T create(Class<T> objClass, Object keyObj, String lang) throws InstantiationException, IllegalAccessException;
}
