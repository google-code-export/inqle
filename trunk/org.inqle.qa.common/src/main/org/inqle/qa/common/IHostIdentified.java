package org.inqle.qa.common;

/**
 * interface that all remote services shoud implement.
 * ensures that the client can identify the source
 * server of the instance 
 * @author gd9345
 *
 */
public interface IHostIdentified {

	public String getHostUri();
	
	public void setHostUri(String hostUri);
}
