package org.inqle.qa.common.services;

public class Hello implements IHello {

	private String serverId;
	
	public Hello() {}
	public Hello(String serverId) {
		this.serverId = serverId;
	}
	
	public String hello(String from) {
		// This is the implementation of the IHello service
		// This method can be executed via remote proxies
		System.out.println("Hello server: " + serverId + " reports: hello from="+from);
		return "TO: " + from + ", FROM: " + serverId + "; HELLO!";
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getServerId() {
		return serverId;
	}

}
