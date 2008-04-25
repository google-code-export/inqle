package org.inqle.ui.rap.tree;

import org.inqle.ui.rap.IPart;

public class NullDeltaListener implements IDeltaListener {
	protected static NullDeltaListener soleInstance = new NullDeltaListener();
	public static NullDeltaListener getSoleInstance() {
		return soleInstance;
	}
	
	/*
	 * @see IDeltaListener#add(DeltaEvent)
	 */
	public void updateTree(IPart changedPart) {}

	public void updatePart(IPart part) {}

}
