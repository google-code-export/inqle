package org.inqle.qa.common.services;

public class ServerIdentified implements IServerIdentified {

	protected String serverId;

	@Override
	public String getServerId() {
		return serverId;
	}

	@Override
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

}
