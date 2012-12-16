package org.inqle.qa;

public interface IQABean {

	/**
	 * The key is a unique identifier across all entities of all kinds
	 * @return
	 */
	public String getKey();
	public void setKey(String key);
	/**
	 * The id is unique within this kind only
	 * @return
	 */
	public String getId();
	public void setId(String id);
}
