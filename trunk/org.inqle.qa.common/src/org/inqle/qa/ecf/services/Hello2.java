package org.inqle.qa.ecf.services;

public class Hello2 implements IHello2 {

	private String serverId;
	
	public Hello2() {}
	public Hello2(String serverId) {
		this.serverId = serverId;
	}
	
	public String hello(String from) {
		// This is the implementation of the IHello service
		// This method can be executed via remote proxies
		System.out.println("Hello2 server: " + serverId + " reports: hello2 from="+from);
		return "TO2: " + from + ", FROM2: " + serverId + "; HELLO2!";
	}
	
	public void setServerId(String containerId) {
		this.serverId = containerId;
	}

	public String getServerId() {
		return this.serverId;
	}
	
//	public void setServerId(String serverId) {
//		this.serverId = serverId;
//	}
//
//	public String getServerId() {
//		return serverId;
//	}

}
