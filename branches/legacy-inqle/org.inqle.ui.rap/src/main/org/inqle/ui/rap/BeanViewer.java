package org.inqle.ui.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.data.rdf.jenabean.util.JenabeanWriter;
import org.inqle.rdf.beans.INamedAndDescribedJenabean;

public class BeanViewer extends Viewer implements ISelectionListener {

	private Composite composite;
	private Text idWidget;
	private Text nameWidget;
	private Text classWidget;
	private Text descriptionWidget;
	private Text detailWidget;
	
	public BeanViewer(Composite parentComposite, Object bean) {
		this(parentComposite);
		setInput(bean);
	}
	
	public BeanViewer(Composite parentComposite) {
		//this.parentComposite = parentComposite;
		
		composite = new Composite(parentComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		//GridData compositeLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayout(gridLayout);
		
		Label l = new Label(composite, SWT.NONE);
		l.setText("ID");
		//l.setFont(boldFont);
		idWidget = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
	  GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	  idWidget.setLayoutData(gridData);
	  
		l = new Label(composite, SWT.NONE);
		l.setText("Name");
		//l.setFont(boldFont);
		nameWidget = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
	  gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	  nameWidget.setLayoutData(gridData);
	  
	  l = new Label(composite, SWT.NONE);
		l.setText("Class");
		//l.setFont(boldFont);
		classWidget = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
	  gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	  classWidget.setLayoutData(gridData);
	  
	  l = new Label(composite, SWT.NONE);
		l.setText("Description");
		//l.setFont(boldFont);
	  descriptionWidget = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
	  gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP);
	  descriptionWidget.setLayoutData(gridData);
	  
	  l = new Label(composite, SWT.NONE);
		l.setText("Detail");
		//l.setFont(boldFont);
	  detailWidget = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
	  //gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP);
	  //gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, true);
	  gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
	  detailWidget.setLayoutData(gridData);
	  
	  composite.setVisible(false);
	}
	
	//private ISelection selection;
	private Object bean;

	private static final Logger log = Logger.getLogger(BeanViewer.class);
	
	@Override
	public Control getControl() {
		return composite;
	}

	@Override
	public ISelection getSelection() {
		//return selection;
		return null;
	}

	@Override
	public void refresh() {
		composite.setVisible(true);
		log.trace("Refreshing...");
		
//		nameWidget.setText("");
//		classWidget.setText("");
//		descriptionWidget.setText("");
//		detailWidget.setText("");
		
		String clazz = "";
		if (bean != null) {
			clazz = bean.getClass().getName();
		}
		String id = "";
		String name = "";
		String description = "";
		String detail = "";
		

		if (bean instanceof INamedAndDescribed) {
			log.trace("...is INamedAndDescribed...");
			INamedAndDescribed namedAndDescribed = (INamedAndDescribed)bean;
			name = namedAndDescribed.getName();
			description = namedAndDescribed.getDescription();
			//MessageDialog.openInformation(composite.getShell(), "Selected object '" + namedAndDescribed.getName() + "'", "Description:\n" + namedAndDescribed.getDescription());
		}
		if (bean instanceof INamedAndDescribedJenabean) {
			log.trace("...is IBasicJenabean...");
			try {
				id = ((INamedAndDescribedJenabean)bean).getId();
				detail = JenabeanWriter.toString(bean);
			} catch (Exception e) {
				log.error("Unable to write IBasicJenabean to RDF:\nname=" + name + "\ndescription=" + description + "\nclass=" + clazz, e);
			}
		  //MessageDialog.openInformation(composite.getShell(), "Detail=", JenabeanWriter.toString(bean));
		}
		if (id == null) {
			id = "";
		}
		if (name == null) {
			name = "";
		}
		if (clazz == null) {
			clazz = "";
		}
		if (description == null) {
			description = "";
		}
		if (detail == null) {
			detail = "";
		}
		//log.info("\n\nName=" + name + "\nClass=" + clazz + "\nDescrption=" + description + "\nDetail=" + detail);
		idWidget.setText(id);
		nameWidget.setText(name);
		classWidget.setText(clazz);
		descriptionWidget.setText(description);
		detailWidget.setText(detail);
	  
//		nameWidget.pack();
//		classWidget.pack();
//		descriptionWidget.pack();
//		detailWidget.pack();
		
//		composite.setVisible(false);
//		composite.setVisible(true);
	}

	@Override
	public void setSelection(ISelection selection, boolean arg1) {
		log.trace("setSelection(" + selection + ", " + arg1 + ")");
		//this.selection = selection;
	}
	
	@Override
	public void inputChanged(Object input, Object oldInput) {
		log.trace("inputChanged(" + input + ", " + oldInput + ")");
	}
	
//	public void selectionChanged(IWorkbenchPart part, ISelection iSelection) {
//		//MessageDialog.openInformation(parent.getShell(), "Selection Made in Tree", iSelection.toString());
//	  log.info("Selection Made in Tree" + iSelection.toString());
//		if(iSelection instanceof IStructuredSelection) {
//	     IStructuredSelection selection = (IStructuredSelection)iSelection;
//	     
//	     Object firstSelectedObject = selection.getFirstElement();
//	     if (firstSelectedObject == null) {
//	    	 log.info("firstSelectedObject is null");
//	    	 return;
//	     }
//	     
//	     if (firstSelectedObject instanceof IPart) {
//	     	 Object representedObject = ((IPart) firstSelectedObject).getObject();
//	     	 log.info("is an IPart");
//	     	 setBean(representedObject);
//	     } else {
//	    	 log.info("not an IPart");
//	     }
////	     String msg = "Selected:\n";
////	     for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
////	       Object selectedObj = iterator.next();
////	       msg += selectedObj + ",\n";
////	     }
////	     MessageDialog.openInformation(shell.getShell(), "Selection Made in Tree", msg);
//	     
//	 		 //page.openEditor(editorInput, "org.eclipse.ui.DefaultTextEdtior");
//	  }
//		
//	}

	@Override
	public Object getInput() {
		return bean;
	}

	@Override
	public void setInput(Object bean) {
		this.bean = bean;
		refresh();
	}

	public void selectionChanged(IWorkbenchPart arg0, ISelection arg1) {
		//do nothing
	}
}
