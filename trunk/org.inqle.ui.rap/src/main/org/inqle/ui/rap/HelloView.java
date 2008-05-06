package org.inqle.ui.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;

/**
 * This view shows a &quot;mail message&quot;. This class is contributed through
 * the plugin.xml.
 */
public class HelloView extends ViewPart implements ISelectionListener {

	private static final Logger log = Logger.getLogger(HelloView.class);
	
	public static final String ID = "org.inqle.ui.rap.helloView";
	private Composite parent;

	private BeanViewer beanViewer;
	
	public void createPartControl(Composite parent) {
		this.parent = parent;
		getSite().getPage().addSelectionListener(this);
		beanViewer = new BeanViewer(parent);
	}
	
//	public void resetView(Object objectToDetail) {
//		String clazz = objectToDetail.getClass().getName();
//		String name = "[none]";
//		String description = "";
//		String detail = "";
//		
//		Composite composite = new Composite(parent, SWT.NONE);
//		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, true));
//		
//		if (objectToDetail instanceof INamedAndDescribed) {
//			log.info("...is INamedAndDescribed");
//			INamedAndDescribed namedAndDescribed = (INamedAndDescribed)objectToDetail;
//			name = namedAndDescribed.getName();
//			description = namedAndDescribed.getDescription();
//			//MessageDialog.openInformation(composite.getShell(), "Selected object '" + namedAndDescribed.getName() + "'", "Description:\n" + namedAndDescribed.getDescription());
//		}
//		if (objectToDetail instanceof IBasicJenabean) {
//			log.info("...is IBasicJenabean");
//			detail = JenabeanWriter.toString(objectToDetail);
//		  //MessageDialog.openInformation(composite.getShell(), "Detail=", JenabeanWriter.toString(objectToDetail));
//		}
//		
//		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, true));
////		GridLayout layout = new GridLayout();
////		layout.marginHeight = 0;
////		layout.marginWidth = 0;
////		top.setLayout(layout);
////		// top banner
////		Composite banner = new Composite(top, SWT.NONE);
////		banner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, true));
////		layout = new GridLayout();
////		layout.marginHeight = 5;
////		layout.marginWidth = 10;
////		layout.numColumns = 2;
////		banner.setLayout(layout);
////		
////		// setup bold font
//		Font boldFont = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);    
//		
//		Label l = new Label(composite, SWT.NONE);
//		l.setText("Name");
//		l.setFont(boldFont);
//		Text nameWidget = new Text(composite, SWT.BORDER);
//    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP);
//    nameWidget.setLayoutData(gridData);
//    nameWidget.setText(name);
//    
//    l = new Label(composite, SWT.NONE);
//		l.setText("Class");
//		l.setFont(boldFont);
//		Text classWidget = new Text(composite, SWT.BORDER);
//    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP);
//    classWidget.setLayoutData(gridData);
//    classWidget.setText(clazz);
//    
//    l = new Label(composite, SWT.NONE);
//		l.setText("Description");
//		l.setFont(boldFont);
//    Text descriptionWidget = new Text(composite, SWT.BORDER);
//    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP);
//    descriptionWidget.setLayoutData(gridData);
//    descriptionWidget.setText(description);
//    
//    l = new Label(composite, SWT.NONE);
//		l.setText("Detail");
//		l.setFont(boldFont);
//    Text detailWidget = new Text(composite, SWT.BORDER);
//    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP);
//    detailWidget.setLayoutData(gridData);
//    detailWidget.setText(detail);
//    
//    log.info("\n\nName=" + name + "\nClass=" + clazz + "\nDescrption=" + description + "\nDetail=" + detail);
//		
//    //refresh
////    Shell siteShell = getSite().getShell();
////    getSite().getPage().hideView(this);
////    siteShell.update();
//    //composite.redraw();
//		//l.setText(appInfoObj.getClass() + "; " + appInfoObj.toString());
//	}

	public void setFocus() {
	}

	public void selectionChanged(IWorkbenchPart part, ISelection iSelection) {
		//MessageDialog.openInformation(parent.getShell(), "Selection Made in Tree", iSelection.toString());
	  log.info("Selection Made in Tree" + iSelection.toString());
		if(iSelection instanceof IStructuredSelection) {
	     IStructuredSelection selection = (IStructuredSelection)iSelection;
	     
	     Object firstSelectedObject = selection.getFirstElement();
	     if (firstSelectedObject == null) {
	    	 log.info("firstSelectedObject is null");
	    	 return;
	     }
	     
	     if (firstSelectedObject instanceof IPart) {
	     	 Object representedObject = ((IPart) firstSelectedObject).getObject();
	     	 beanViewer.setInput(representedObject);
	     	 log.info("is an IPart");
	     	 //resetView(representedObject);
	     } else {
	    	 log.info("not an IPart");
	    	 beanViewer.setInput(firstSelectedObject);
	     }
	     
	     
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
