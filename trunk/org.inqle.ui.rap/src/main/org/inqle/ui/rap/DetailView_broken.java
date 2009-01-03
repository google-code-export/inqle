package org.inqle.ui.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

/**
 * This view shows a &quot;mail message&quot;. This class is contributed through
 * the plugin.xml.
 */
public class DetailView_broken extends ViewPart implements ISelectionListener {

	private static final Logger log = Logger.getLogger(DetailView_broken.class);
	
	public static final String ID = "org.inqle.ui.rap.detailView";

	private Viewer viewer;

	private Composite composite;
	
//	private BeanViewer beanViewer;
	
	@Override
	public void createPartControl(Composite parent) {
		this.composite = parent;
		getSite().getPage().addSelectionListener(this);
		viewer = new BeanViewer(parent);
	}
	
	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(this);
	}

	public void setFocus() {
	}

	public void selectionChanged(IWorkbenchPart part, ISelection iSelection) {
		if (viewer != null) {
			viewer.setInput(null);
			viewer.refresh();
		}
		
		//MessageDialog.openInformation(parent.getShell(), "Selection Made in Tree", iSelection.toString());
	  log.info("Selection Made in Tree" + iSelection.toString());
		if(iSelection instanceof IStructuredSelection) {
	     IStructuredSelection selection = (IStructuredSelection)iSelection;
	     
	     Object firstSelectedObject = selection.getFirstElement();
	     if (firstSelectedObject == null) {
	    	 log.info("firstSelectedObject is null");
	    	 return;
	     }
	     
	     
//	     if (firstSelectedObject instanceof ModelPart) {
//	    	 ModelPart selectedModelPart = (ModelPart)firstSelectedObject;
//	    	 ExternalDataset dataset = selectedModelPart.getDataset();
//	    	 log.info("dataset selected:" + dataset);
//	     } else 
	    	if (firstSelectedObject instanceof IPart) {
//	     	 Object representedObject = ((IPart) firstSelectedObject).getObject();
//	     	 beanViewer.setInput(representedObject);
	     	 viewer = ((IPart) firstSelectedObject).getViewer(composite);
	     	 log.info("An IPart was clicked");
	     	 //resetView(representedObject);
	     } else {
	    	 log.info("A NON- IPart was clicked");
	    	 viewer = new BeanViewer(composite, firstSelectedObject);
//	    	 beanViewer.setInput(firstSelectedObject);
	     }
	     
	    	viewer.refresh();
//	     String msg = "Selected:\n";
//	     for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
//	       Object selectedObj = iterator.next();
//	       msg += selectedObj + ",\n";
//	     }
//	     MessageDialog.openInformation(shell.getShell(), "Selection Made in Tree", msg);
	     
	 		 //page.openEditor(editorInput, "org.eclipse.ui.DefaultTextEdtior");
	  }
		
	}
}
