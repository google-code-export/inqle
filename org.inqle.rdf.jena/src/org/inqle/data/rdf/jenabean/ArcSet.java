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
//	private LinkedHashMap<Arc, Object> arcs = new LinkedHashMap<Arc, Object>();
	private LinkedHashMap<Arc, Object> arcValues = new LinkedHashMap<Arc, Object>();
	private URI subject;
	
	public void addArcAndValue(Arc arc, Object value) {
		arcValues.put(arc, value);
	}

	public List<Arc> getArcList() {
//		return arcList;
		List<Arc> keys = new ArrayList<Arc>();
		for (Arc arc: arcValues.keySet()) {
			keys.add(arc);
		}
		return keys;
	}

	public Object getValue(Arc arc) {
		return arcValues.get(arc);
	}
	
	public Object getValue(int columnIndex) {
		Arc arc = getArcList().get(columnIndex);
		return arcValues.get(arc);
	}
	
	public String getStringRepresentation() {
		String s = "";
		for (Arc arc: getArcList()) {
			s += arc.toString() + " = " + arcValues.get(arc) + "\n";
			
		}
		return s;
	}

	@Override
	public String toString() {
		return getStringRepresentation();
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
