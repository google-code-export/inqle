package org.inqle.ui.rap;

import java.util.ArrayList;

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.inqle.core.extensions.util.IExtensionSpec;

/**
 * Intended to be extended by part types
 * @author David Donohue
 * Feb 5, 2008
 */
public abstract class PartType extends Part implements IPartType {

	private static final String ICON_PATH = "org/inqle/ui/rap/images/folder.gif";
	protected ArrayList<IPart> children = new ArrayList<IPart>();
	protected IExtensionSpec spec = null;

	public void addChild(IPart child) {
		children.add(child);
		child.setParent(this);
	}
	public void removeChild(IPart child) {
		children.remove(child);
		child.setParent(null);
	}
	public IPart[] getChildren() {
		return (IPart[]) children.toArray(new IPart[children.size()]);
	}
	public boolean hasChildren() {
		return getChildren().length>0;
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}

	public void setSpec(IExtensionSpec spec) {
		this.spec  = spec;
	}
}
