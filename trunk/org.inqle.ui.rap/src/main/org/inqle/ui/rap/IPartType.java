package org.inqle.ui.rap;

import java.util.List;

import org.inqle.core.extensions.util.IJavaExtension;

public interface IPartType extends IPart, IJavaExtension {

	public static final String ID = "org.inqle.ui.rap.IPartType";

	public void addChild(IPart child);
	
	public void removeChild(IPart child);
	
	public IPart[] getChildren();
	
	public boolean hasChildren();

}
