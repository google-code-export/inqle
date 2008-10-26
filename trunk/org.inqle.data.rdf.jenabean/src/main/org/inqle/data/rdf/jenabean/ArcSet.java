/**
 * 
 */
package org.inqle.data.rdf.jenabean;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class represents a set of Arc objects, 
 * each originating from a common RDF node.
 * Contains the value that is present of each arc.
 * @author David Donohue
 * Feb 22, 2008
 */
//@Namespace(RDF.INQLE)
public class ArcSet {
	private LinkedHashMap<Arc, Object> arcs = new LinkedHashMap<Arc, Object>();
	private URI subject;
	
	public void addArcAndValue(Arc arc, Object value) {
		arcs.put(arc, value);
	}

	public List<Arc> getArcs() {
		List<Arc> keys = new ArrayList<Arc>();
		for (Arc arc: arcs.keySet()) {
			keys.add(arc);
		}
		return keys;
	}

	public Object getValue(Arc arc) {
		return arcs.get(arc);
	}
	
	public Object getValue(int columnIndex) {
		Arc arc = getArcs().get(columnIndex);
		return arcs.get(arc);
	}
	
	public String getStringRepresentation() {
		String s = "";
		for (Arc arc: getArcs()) {
			s += arc.toString() + "\n";
		}
		return s;
	}

//	public void setArcs(Collection<Arc> arcCollection) {
//		this.arcs = new ArrayList<Arc>(arcCollection);
//		Collections.sort(arcs);
//	}
	
//	public void clone(ArcSet objectToBeCloned) {
//		super.clone(objectToBeCloned);
//		setSubject(objectToBeCloned.getSubject());
//		setArcs(objectToBeCloned.getArcs());
//	}
//	
//	public ArcSet createClone() {
//		ArcSet newArcSet = new ArcSet();
//		newArcSet.setArcs(arcs);
//		return newArcSet;
//	}

	public URI getSubject() {
		return subject;
	}

	public void setSubject(URI subject) {
		this.subject = subject;
	}
}
