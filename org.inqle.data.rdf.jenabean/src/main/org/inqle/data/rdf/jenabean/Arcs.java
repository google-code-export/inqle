/**
 * 
 */
package org.inqle.data.rdf.jenabean;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a set of Arc objects, 
 * each originating from a common RDF node
 * @author David Donohue
 * Feb 22, 2008
 */
@Deprecated
public class Arcs {
	private List<Arc> arcs = new ArrayList<Arc>();
	
	public void addArc(Arc arc) {
		arcs.add(arc);
	}

	public List<Arc> getArcs() {
		return arcs;
	}
	
}
