/**
 * 
 */
package org.inqle.data.rdf.jenabean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.inqle.data.rdf.RDF;

import thewebsemantic.Namespace;

/**
 * This class represents a set of Arc objects, 
 * each originating from a common RDF node.
 * Internally it is backed by an (ordered) List.
 * Externally, it represents this as an (unordered) Collection, for the 
 * purposes of RDF persistence, as Jenabean/RDF best handles unordered collections.
 * @author David Donohue
 * Feb 22, 2008
 */
@Namespace(RDF.INQLE)
public class ArcSet extends GlobalJenabean {
	private List<Arc> arcs = new ArrayList<Arc>();
	
	public void addArc(Arc arc) {
		arcs.add(arc);
	}

	public Collection<Arc> getArcs() {
		return arcs;
	}

	public String getStringRepresentation() {
		String s = "";
		for (Arc arc: arcs) {
			s += arc.toString() + "\n";
		}
		return s;
	}

	public void setArcs(Collection<Arc> arcCollection) {
		this.arcs = new ArrayList<Arc>(arcCollection);
		Collections.sort(arcs);
	}
	
	public ArcSet createClone() {
		ArcSet newArcSet = new ArcSet();
		newArcSet.setArcs(arcs);
		return newArcSet;
	}
}
