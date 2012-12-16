package org.inqle.ui.rap.tree;

import org.inqle.ui.rap.IPart;

public interface IDeltaListener {
	public void updateTree(IPart changedPart);

	public void updatePart(IPart part);
}
