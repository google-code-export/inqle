package org.inqle.data.sampling.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.util.BeanTool;
import org.inqle.data.sampling.ISampler;
import org.inqle.ui.rap.IPartType;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class DeleteSamplerAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister;
	private ISampler samplerToDelete = null;
	private CustomizedSamplerPart samplerPart = null;
	
	private static final Logger log = Logger.getLogger(DeleteSamplerAction.class);
	
	//public DeleteSamplerAction(String menuText, CustomizedSamplerPart samplerPart, ISampler samplerToDelete, IWorkbenchWindow window, Persister persister) {
		
	public DeleteSamplerAction(String menuText, CustomizedSamplerPart samplerPart, IWorkbenchWindow window) {
		this.window = window;
		this.menuText = menuText;
		this.samplerPart = samplerPart;
		ISampler baseSampler = samplerPart.getSamplerFactory().getBaseSampler();
//		this.samplerToDelete  = (ISampler)baseSampler.createReplica();
		this.samplerToDelete  = BeanTool.replicate(baseSampler);
		//this.persister = persister;
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void run() {
		boolean confirmDelete = false;
		
		if (this.samplerPart instanceof IPartType) {
			IPartType thisPartType = (IPartType)samplerPart;
			if (thisPartType.hasChildren()) {
				MessageDialog.openWarning(window.getShell(), "Unable to delete", "Please remove all child objects before deleting this sampler.");
				return;
			}
		}
		if (samplerToDelete != null) {
			confirmDelete = MessageDialog.openConfirm(window.getShell(), "Delete this Sampler", "Are you sure you want to delete sampler\n'" + samplerToDelete.getName() + "'?\nTHIS CANNOT BE UNDONE!");
		}
		if (confirmDelete) {
			Persister persister = Persister.getInstance();
			persister.remove(samplerToDelete);
			IPartType parentPart = samplerPart.getParent();
			parentPart.fireUpdate(parentPart);
		}
	}
}
