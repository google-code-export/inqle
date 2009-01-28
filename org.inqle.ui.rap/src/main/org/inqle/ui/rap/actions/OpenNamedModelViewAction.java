package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.inqle.data.rdf.jena.Datamodel;

/**
 * When run, this action will open another instance of a view.
 * That view should implement IDatamodelView, so that we can pass
 * the data into it.
 */
public class OpenNamedModelViewAction extends OpenViewAction {
	
	
	public OpenNamedModelViewAction(
			IWorkbenchWindow window, 
			String label,
			String viewId, 
			String pluginId, 
			String iconPath) {
		super(window, label, viewId, pluginId, iconPath);
	}

	private Datamodel datamodel;
	
	private static final Logger log = Logger.getLogger(OpenNamedModelViewAction.class);
	
//	public OpenDataViewAction(IWorkbenchWindow window, String label, String viewId, String pluginId, String iconPath) {
//		log.trace("Create OpenViewAction: viewId=" + viewId + "; pluginId=" + pluginId + "; iconPath=" + iconPath);
//		this.window = window;
//		this.viewId = viewId;
//        setText(label);
//    // The id is used to refer to the action in a menu or toolbar
//		setId(ICommandIds.CMD_OPEN);
//		
//    // Associate the action with a pre-defined command, to allow key bindings.
//		setActionDefinitionId(ICommandIds.CMD_OPEN);
//		//setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.inqle.ui.rap", "/icons/sample2.gif"));
//		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "/" + iconPath));
//	}
	
	public void run() {
		log.info("OpenNamedModelViewAction.run()...");
		try {
			IDatamodelView view = (IDatamodelView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);
			if (view==null) {
				view = (IDatamodelView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
			}
			view.setDatamodel(datamodel);
			view.setTitleText("Types in Datamodel :" + datamodel);
			log.info("Refreshing Datamodel View with dataset: " + getDatamodel());
			view.refreshView();
			log.info("Got view for named model: " + datamodel);
			theWindow.getActivePage().showView(viewId);
			theWindow.getActivePage().bringToTop(view);
		} catch (Exception e) {
			log.error("Error running OpenDataViewAction", e);
		}
		
//		super.run();
	}

	public void setDatamodel(Datamodel datamodel) {
		this.datamodel = datamodel;
	}

	public Datamodel getDatamodel() {
		return datamodel;
	}
}
