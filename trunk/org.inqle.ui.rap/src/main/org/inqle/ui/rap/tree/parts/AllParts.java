/**
 * 
 */
package org.inqle.ui.rap.tree.parts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.PartType;

/**
 * @author David Donohue
 * Feb 5, 2008
 */
public class AllParts extends PartType {
	
	private Logger log = Logger.getLogger(AllParts.class);
	
	public AllParts() {
		persister = Persister.createPersister();
	}
	/* 
	 * 
	 * The children = the list of all parttype plugins
	 * add to each child a reference to the Persister object,
	 * such that the entire tree may use common database connections
	 * @see org.inqle.ui.rap.IPartType#getChildren()
	 */
	public IPart[] getChildren() {
		List<Object> topLevelPartObjects =  ExtensionFactory.getExtensions(IPartType.ID);
		IPart[] nullIPartArr = new IPart[] {};
		if (topLevelPartObjects == null) {
			return nullIPartArr;
		}
		List<IPart> topLevelParts = new ArrayList<IPart>();
		for (Object partObject: topLevelPartObjects) {
			if (partObject == null) continue;
			IPart part = (IPart)partObject;
			part.setParent(this);
			part.setPersister(persister);
			part.addListener(this.listener);
			//log.info("AllParts adding part: " + part.getName());
			topLevelParts.add(part);
		}
		IPart[] ipartArr = topLevelParts.toArray(nullIPartArr);
		
		return ipartArr;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	public String getName() {
		return "";
	}

	public Image getIcon() {
		//root has no icon
		return null;
	}

}
