package org.inqle.data.sampling;

import java.io.Serializable;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * This class contains an RDFNode (the RDF object) 
 * @author David Donohue
 * Jun 7, 2007
 */
public class DataCell implements Serializable {

	private RDFNode node;

	public DataCell(RDFNode node) {
		this.node = node;
	}
	
	public void setNode(RDFNode node) {
		this.node = node;
	}

	public RDFNode getNode() {
		return node;
	}
	
	public String toString() {
		return node.toString();
	}
}
