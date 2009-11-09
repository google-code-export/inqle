/**
 * 
 */
package org.inqle.data.rdf.jena;

import java.util.ArrayList;
import java.util.Collection;

import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.TargetModelName;
import org.inqle.data.rdf.RDF;
import thewebsemantic.Namespace;

/**
 * A Datamodel, which contains external (minable) data.  Use this class when data of a particular
 * purpose or function might be distributed among multiple datamodels.
 * @author David Donohue
 * Jul 2, 2008
 */
@TargetModelName(Persister.METAREPOSITORY_DATAMODEL)
@Namespace(RDF.INQLE)
public class PurposefulDatamodel extends DatabaseBackedJenamodel {

	private Collection<String> datamodelPurposes = new ArrayList<String>();
	
//	public UserDatamodel createClone() {
//		UserDatamodel newObj = new UserDatamodel();
//		newObj.clone(this);
//		return newObj;
//	}
//
//	public UserDatamodel createReplica() {
//		UserDatamodel newObj = new UserDatamodel();
//		newObj.replicate(this);
//		return newObj;
//	}

	public Collection<String> getDatamodelPurposes() {
		return datamodelPurposes;
	}

	public void setDatamodelPurposes(Collection<String> datamodelPurposes) {
		this.datamodelPurposes = datamodelPurposes;
	}
	
	public void addDatamodelPurpose(String datamodelPurpose) {
		datamodelPurposes.add(datamodelPurpose);
	}
	
//	public void clone(UserDatamodel objectToBeCloned) {
//		super.clone(objectToBeCloned);
//		//setConnection(objectToBeCloned.getConnection());
//		setDatamodelFunctions(objectToBeCloned.getDatamodelFunctions());
//	}
//	
//	public void replicate(UserDatamodel objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//	}
	
	@Override
	public String getModelType() {
		return IDBConnector.SUBDATABASE_DATA;
	}
	
}
