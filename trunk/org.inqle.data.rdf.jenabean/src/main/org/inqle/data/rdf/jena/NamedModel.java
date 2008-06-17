package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.BasicJenabean;

import thewebsemantic.Namespace;
/**
 * from http://jena.sourceforge.net/assembler/assembler-howto.html
 * class ja:NamedModel subClassOf ja:Model
 * domainOf ja:modelName cardinality 1
 * @author David Donohue
 * Jan 9, 2008
 */
//@Namespace(NS)
//public abstract class NamedModel extends JenaAssemblerObject {

@Namespace(RDF.INQLE)
public abstract class NamedModel extends BasicJenabean {

	//private String modelName;

	//@RdfProperty(NS + "modelName")
//	public String getModelName() {
//		//return modelName;
//		return getId();
//	}

//	public void setModelName(String modelName) {
//		//this.modelName = modelName;
//		this.id = modelName;
//	}
	
//	@Override
//	public String getName() {
//		return getModelName();
//	}
	
//	@Override
//	public void setName(String name) {
//		setModelName(name);
//	}
//	
//	public void clone(Dataset objectToBeCloned) {
//		super.clone(objectToBeCloned);
//		setModelName(objectToBeCloned.getModelName());
//	}
}
