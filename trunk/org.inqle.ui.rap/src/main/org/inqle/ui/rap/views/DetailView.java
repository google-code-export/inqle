package org.inqle.ui.rap.views;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.inqle.ui.rap.IDisposableViewer;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.widgets.ActionsMenu;

/**
 * This view shows a &quot;mail message&quot;. This class is contributed through
 * the plugin.xml.
 */
public class DetailView extends ViewPart implements ISelectionListener {

	private static final Logger log = Logger.getLogger(DetailView.class);
	
	public static final String ID = "org.inqle.ui.rap.views.DetailView";

	private IDisposableViewer viewer;

	private Composite composite;

	private ScrolledComposite scrolledComposite;

	private ActionsMenu actionsMenu;

//	private Label l;
	
	@Override
	public void createPartControl(Composite parent) {
		getSite().getPage().addSelectionListener(this);
		scrolledComposite = new ScrolledComposite(parent,  SWT.V_SCROLL | SWT.H_SCROLL);
//		scrolledComposite.setLayout(new GridLayout(1, true));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
//		GridData gridData = new GridData(GridData.FILL_BOTH);
//		scrolledComposite.setLayoutData(gridData);
		
//		this.composite = parent;
		 
		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(this);
	}

	public void setFocus() {
	}

	public void selectionChanged(IWorkbenchPart part, ISelection iSelection) {
		//MessageDialog.openInformation(parent.getShell(), "Selection Made in Tree", iSelection.toString());
	  log.info("Selection Made in Tree" + iSelection.toString());
	  if (composite != null) {
	  	composite.dispose();
	  }
	  composite = new Composite(scrolledComposite, SWT.NONE);
	  scrolledComposite.setContent(composite);
		composite.setLayout(new GridLayout(1, true));
		if(iSelection instanceof IStructuredSelection) {
	     IStructuredSelection selection = (IStructuredSelection)iSelection;
	     
	     Object firstSelectedObject = selection.getFirstElement();
	     if (firstSelectedObject == null) {
	    	 log.info("firstSelectedObject is null");
	    	 return;
	     }
	     
	     if (firstSelectedObject instanceof IPart) {
	    	 IPart selectedPart = (IPart)firstSelectedObject;
//	     	 Object representedObject = ((IPart) firstSelectedObject).getObject();
	    	 if (viewer != null) {
	    		 viewer.dispose();
	    	 }
	     	 viewer = selectedPart.getViewer(composite);
	     	 
//	     	 if (l != null) {
//	     		 l.dispose();
//	     	 }
//	     	 l = new Label(composite, SWT.BOLD);
//	     	 l.setText("Available Actions");
	  		 if (actionsMenu != null) {
	  			 actionsMenu.dispose();
	  		 }
	  		 List<IAction> actions = selectedPart.getActions(getSite().getWorkbenchWindow());
	  		 if (actions != null && actions.size()>0) {
	  			 actionsMenu = new ActionsMenu(composite, SWT.BORDER, actions);
	  		 }
	  		 
	     	 composite.pack();
	     	 composite.redraw();
	     	 composite.setVisible(true);
	     	 scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	     	 log.trace("is an IPart");
	     } else {
	    	 log.trace("not an IPart");
	    	 viewer.setInput(firstSelectedObject);
	    	 composite.pack();
	     	 composite.redraw();
	     	 composite.setVisible(true);
	     	 scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	     }
	     
	     PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop(this);
	  }
	}
}
