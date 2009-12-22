package org.inqle.qa.common.services;

/**
 * interface that all remote services should implement.
 * ensures that the client can identify the source
 * server of the instance 
 * @author gd9345
 *
 */
@Deprecated
public interface IServerIdentified {

	public String getServerId();
	
	public void setServerId(String serverId);
}
