/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.tree.parts.DatabasePart;
import org.inqle.ui.rap.tree.parts.ModelPart;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class ModelWizardAction extends Action {
	public static final int MODE_NEW = 0;
	public static final int MODE_EDIT = 1;
	public static final int MODE_CLONE = 2;
	private String menuText;
	private IWorkbenchWindow window;
	private Persister persister = null;
	private int mode = MODE_NEW;
	private ModelPart modelPart = null;
	private DatabasePart databasePart;
	
	private static final Logger log = Logger.getLogger(ModelWizardAction.class);
	
	public ModelWizardAction(int mode, String menuText, DatabasePart databasePart, IWorkbenchWindow window, Persister persister) {
		this.mode = mode;
		this.menuText = menuText;
		this.databasePart = databasePart;
		this.window = window;
		this.persister = persister;
	}
	
	public ModelPart getModelPart() {
		return modelPart;
	}

	public void setModelPart(ModelPart modelPart) {
		this.modelPart = modelPart;
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void runWithEvent(Event event) {
		//MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Opening new Wizard", event.toString());
		ModelWizard wizard = new ModelWizard(mode, databasePart, persister);
		
		//for MODE_NEW, do not add a starting base Connection
		if (mode == MODE_EDIT || mode == MODE_CLONE) {
			wizard.setModelPart(modelPart);
		}
		
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();
	}
}
