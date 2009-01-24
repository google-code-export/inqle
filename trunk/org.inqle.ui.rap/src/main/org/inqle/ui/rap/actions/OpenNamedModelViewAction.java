package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.inqle.data.rdf.jena.NamedModel;

/**
 * When run, this action will open another instance of a view.
 * That view should implement INamedModelView, so that we can pass
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

	private NamedModel namedModel;
	
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
			INamedModelView view = (INamedModelView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);
			if (view==null) {
				view = (INamedModelView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
			}
			view.setNamedModel(namedModel);
			view.setTitleText("Types in Dataset :" + namedModel);
			log.info("Refreshing Dataset View with dataset: " + getNamedModel());
			view.refreshView();
			log.info("Got view for named model: " + namedModel);
			theWindow.getActivePage().showView(viewId);
			theWindow.getActivePage().bringToTop(view);
		} catch (Exception e) {
			log.error("Error running OpenDataViewAction", e);
		}
		
//		super.run();
	}

	public void setNamedModel(NamedModel namedModel) {
		this.namedModel = namedModel;
	}

	public NamedModel getNamedModel() {
		return namedModel;
	}
}
