package org.inqle.ui.rap.views;

import java.util.Date;

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
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.core.util.DateFormatter;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IDisposableViewer;

import com.hp.hpl.jena.rdf.model.Model;

public class DatasetViewer extends Viewer implements IDisposableViewer {

	private Composite composite;
	private Text idWidget;
	private Text nameWidget;
	private Text classWidget;
	private Text descriptionWidget;
	private Text sizeWidget;
	private Text createdWidget;
//	private ResultSetTable resultSetTable;
	
	public DatasetViewer(Composite parentComposite, Object bean) {
		this(parentComposite);
		setInput(bean);
	}
	
	public Dataset getDataset() {
		if (bean == null) return null;
		return (Dataset)bean;
	}
	
	public Model getModel() {
		Persister persister = Persister.getInstance();
		Model model = persister.getModel(getDataset());
		return model;
	}
	
	public DatasetViewer(Composite parentComposite) {
		//this.parentComposite = parentComposite;
		
		composite = new Composite(parentComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		
		Composite formComposite = new Composite(composite, SWT.NONE);
		formComposite.setLayout(new GridLayout(4, false));
		
		Label l = new Label(formComposite, SWT.NONE);
		l.setText("ID");
		//l.setFont(boldFont);
		idWidget = new Text(formComposite, SWT.BORDER | SWT.WRAP);
		idWidget.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
//	  GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//	  idWidget.setLayoutData(gridData);
	  
		l = new Label(formComposite, SWT.NONE);
		l.setText("Name");
		nameWidget = new Text(formComposite, SWT.BORDER | SWT.WRAP);
	  nameWidget.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP));
	  
	  l = new Label(formComposite, SWT.NONE);
		l.setText("Class");
		classWidget = new Text(formComposite, SWT.BORDER | SWT.WRAP);
		classWidget.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP));
	  
	  l = new Label(formComposite, SWT.NONE);
		l.setText("Description");
		//l.setFont(boldFont);
	  descriptionWidget = new Text(formComposite, SWT.BORDER | SWT.WRAP | SWT.MULTI);
	  descriptionWidget.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP));
	  
	  l = new Label(formComposite, SWT.NONE);
		l.setText("Created");
	  createdWidget = new Text(formComposite, SWT.BORDER);
	  createdWidget.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP));
	  
	  l = new Label(formComposite, SWT.NONE);
		l.setText("Statements");
	  sizeWidget = new Text(formComposite, SWT.BORDER);
	  sizeWidget.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | SWT.WRAP));
		
//	  resultSetTable = new ResultSetTable(composite, SWT.SINGLE);
	  composite.setVisible(true);
	}
	
	//private ISelection selection;
	private Object bean;

	private static final Logger log = Logger.getLogger(DatasetViewer.class);
	
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
		if (bean instanceof IBasicJenabean) {
			log.trace("...is IBasicJenabean...");
			try {
				id = ((IBasicJenabean)bean).getId();
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
		Model model = getModel();
		if (model != null) {
			sizeWidget.setText(String.valueOf(model.size()));
		}
		if (getDataset() != null) {
			Date creationDate = getDataset().getCreationDate();
			if (creationDate != null) {
				createdWidget.setText(DateFormatter.getDateString(creationDate));
			}
//			//query for subjects
//			ResultSetRewindable subjectsRS = SubjectClassLister.queryGetUncommonSubjectsRS(getDataset().getId());
//			resultSetTable.setResultSet(subjectsRS);
//			resultSetTable.setSortable(false);
//			resultSetTable.setLinkColumn(SubjectClassLister.CLASS_URI_VAR);
//			resultSetTable.renderTable(this);
		}
		
//		Label l = new Label(composite, SWT.BOLD);
//		l.setText("Available Actions");
//		
//		ActionsMenu actionsMenu = new ActionsMenu(composite, SWT.BORDER, selectedPart.getActions(getSite().getWorkbenchWindow()));
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

	@Override
	public Object getInput() {
		return bean;
	}

	@Override
	public void setInput(Object bean) {
		this.bean = bean;
		refresh();
	}

//	public void selectionChanged(IWorkbenchPart arg0, ISelection arg1) {
//		
//	}

	public void clearData() {
		idWidget.setText("");
		nameWidget.setText("");
		classWidget.setText("");
		descriptionWidget.setText("");
//		resultSetTable.dispose();
	}

	public void dispose() {
		idWidget.dispose();
		nameWidget.dispose();
		classWidget.dispose();
		descriptionWidget.dispose();
//		resultSetTable.dispose();
		composite.dispose();
	}

//	public void widgetDefaultSelected(SelectionEvent arg0) {
//	}

//	public void widgetSelected(SelectionEvent event) {
//		Object source = event.getSource();
//		if (source instanceof Link) {
//			Link link = (Link)source;
//			Object data = link.getData();
//			if (data==null) return;
////			log.info(data + " clicked.");
////			ClassView classView = new ClassView();
//			
//			ClassView classView = (ClassView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ClassView.ID);
//			if (classView==null) {
//				try {
//					classView = (ClassView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ClassView.ID);
//				} catch (PartInitException e) {
//					log.error("Error showing view: " + ClassView.ID, e);
//				}
//			}
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop(classView);
//			classView.setNamedModel(getDataset());
//			classView.setClassUri(data.toString());
//			classView.setTitleText("Things of type: <" + data.toString() + ">");
//			log.info("Refreshing Class View with dataset: " + getDataset() + " and class URI: " + data.toString());
//			classView.refreshView();
////			classView.setFocus();
//		}
//	}
	
}
