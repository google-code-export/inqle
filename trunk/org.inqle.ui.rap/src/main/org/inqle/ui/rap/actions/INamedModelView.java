package org.inqle.ui.rap.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.inqle.data.rdf.jena.NamedModel;

public interface INamedModelView extends IWorkbenchPart {

	public void setNamedModel(NamedModel data);
	
	public NamedModel getNamedModel();

	public void setTitleText(String string);

	public void refreshView();
}
