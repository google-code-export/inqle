package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Namespace;

@Namespace(RDF.INQLE)
@TargetDatamodel(Persister.METAREPOSITORY_DATAMODEL)
//public class SDBDatabase extends JenaAssemblerObject implements IDatabase {
public class SDBDatabase extends UniqueJenabean implements IDatabase {
	
	private String dbClass = null;	
	private String dbPassword = null;
	private String dbType = null;
	private String dbURL = null;	
	private String dbUser = null;	
	
	public void clone(SDBDatabase oldConnection) {
		super.clone(oldConnection);
		setDbClass(oldConnection.getDbClass());
		setDbPassword(oldConnection.getDbPassword());
		setDbType(oldConnection.getDbType());
		setDbURL(oldConnection.getDbURL());
		setDbUser(oldConnection.getDbUser());
	}
	
	public void replicate(SDBDatabase oldConnection) {
		clone(oldConnection);
		setId(oldConnection.getId());
		super.replicate(oldConnection);
	}
	
	public String getDbClass() {
		return dbClass;
	}
	public void setDbClass(String dbClass) {
		this.dbClass = dbClass;
	}
	
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	public String getDbURL() {
		return dbURL;
	}
	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}
	
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	
	public SDBDatabase createClone() {
		SDBDatabase newObj = new SDBDatabase();
		newObj.clone(this);
		return newObj;
	}
	
	public SDBDatabase createReplica() {
		SDBDatabase newObj = new SDBDatabase();
		newObj.replicate(this);
		return newObj;
	}

	public String getDisplayName() {
		return getDbUser() + "@" + getDbURL();
	}
}
