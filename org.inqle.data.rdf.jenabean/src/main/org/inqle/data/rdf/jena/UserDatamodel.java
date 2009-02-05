/**
 * 
 */
package org.inqle.data.rdf.jena;

import java.util.ArrayList;
import java.util.Collection;

import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.RDF;
import thewebsemantic.Namespace;

/**
 * A Datamodel, which contains external (minable) data
 * @author David Donohue
 * Jul 2, 2008
 */
@TargetDatamodel(Persister.METAREPOSITORY_DATAMODEL)
@Namespace(RDF.INQLE)
public class UserDatamodel extends DatabaseBackedDatamodel {

	private Collection<String> datamodelFunctions = new ArrayList<String>();
	
	public UserDatamodel createClone() {
		UserDatamodel newObj = new UserDatamodel();
		newObj.clone(this);
		return newObj;
	}

	public UserDatamodel createReplica() {
		UserDatamodel newObj = new UserDatamodel();
		newObj.replicate(this);
		return newObj;
	}

	public Collection<String> getDatamodelFunctions() {
		return datamodelFunctions;
	}

	public void setDatamodelFunctions(Collection<String> datamodelFunctions) {
		this.datamodelFunctions = datamodelFunctions;
	}
	
	public void addDatasetFunction(String datamodelFunction) {
		datamodelFunctions.add(datamodelFunction);
	}
	
	public void clone(UserDatamodel objectToBeCloned) {
		super.clone(objectToBeCloned);
		//setConnection(objectToBeCloned.getConnection());
		setDatamodelFunctions(objectToBeCloned.getDatamodelFunctions());
	}
	
	public void replicate(UserDatamodel objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
	}
	
}
