package org.inqle.data.rdf.jena;

import static org.inqle.data.rdf.jena.AssemblerVocabulary.NS;

import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

@Namespace(NS)
public class Connection extends JenaAssemblerObject {
	
	private String dbClass = null;	
	private String dbPassword = null;
	private String dbType = null;
	private String dbURL = null;	
	private String dbUser = null;	
	
	public void clone(Connection oldConnection) {
		super.clone(oldConnection);
		setDbClass(oldConnection.getDbClass());
		setDbPassword(oldConnection.getDbPassword());
		setDbType(oldConnection.getDbType());
		setDbURL(oldConnection.getDbURL());
		setDbUser(oldConnection.getDbUser());
		
	}
	
	@RdfProperty(NS + "dbClass")
	public String getDbClass() {
		return dbClass;
	}
	public void setDbClass(String dbClass) {
		this.dbClass = dbClass;
	}
	
	@RdfProperty(NS + "dbPassword")
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	@RdfProperty(NS + "dbType")
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	@RdfProperty(NS + "dbURL")
	public String getDbURL() {
		return dbURL;
	}
	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}
	

	@RdfProperty(NS + "dbUser")
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	
	public Connection createClone() {
		Connection newObj = new Connection();
		newObj.clone(this);
		return newObj;
	}
	
	public Connection createReplica() {
		Connection newObj = new Connection();
		newObj.replicate(this);
		return newObj;
	}
}
