package org.inqle.ui.rap.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.inqle.data.rdf.jena.Datamodel;

public interface IDatamodelView extends IWorkbenchPart {

	public void setDatamodelId(String data);
	
	public String getDatamodelId();

	public void setTitleText(String string);

	public void refreshView();
}
