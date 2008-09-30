package org.inqle.data.rdf;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

@Namespace(RDF.INQLE)
public class UserAccount {

	private String userName;
	private String password;
	
	@Id
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
