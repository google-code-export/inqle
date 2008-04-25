package org.inqle.data.rdf.jena;

import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import static org.inqle.data.rdf.jena.AssemblerVocabulary.NS;
/**
 * from http://jena.sourceforge.net/assembler/assembler-howto.html
 * class ja:NamedModel subClassOf ja:Model
 * domainOf ja:modelName cardinality 1
 * @author David Donohue
 * Jan 9, 2008
 */
@Namespace(NS)
public abstract class NamedModel extends JenaAssemblerObject {

	private String modelName;

	@RdfProperty(NS + "modelName")
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
