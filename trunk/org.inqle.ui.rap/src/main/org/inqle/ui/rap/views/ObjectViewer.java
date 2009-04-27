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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.ui.google.jsapi.ColumnChart;
import org.inqle.ui.google.jsapi.MotionChart;
import org.inqle.ui.google.jsapi.Table;
import org.inqle.ui.google.json.JSONGoogleDataTable;
import org.inqle.ui.rap.IDisposableViewer;

public class ObjectViewer extends Viewer implements IDisposableViewer, Listener {

	private Composite composite;
	private Text idWidget;
	private Text nameWidget;
	private Text classWidget;
	private Text descriptionWidget;
	private Text detailWidget;
	
	public ObjectViewer(Composite parentComposite, Object bean) {
		this(parentComposite);
		setInput(bean);
	}
	
	public ObjectViewer(Composite parentComposite) {
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
	  
	  //testing custom widgets
	  JSONGoogleDataTable dataTable = null;
	  String widgetData = null;
	  
	  //MOTION CHART:
		dataTable = new JSONGoogleDataTable();
		dataTable.addColumn("Model", "Model", "string", null);
		dataTable.addColumn("thedate", "Date", "date", null);
		dataTable.addColumn("CO2", "CO2", "number", null);
		dataTable.addColumn("Temperature", "Temperature", "number", null);
		dataTable.addRow(new Object[] {"Model1", new Date(1199145600), 377, 22});
		dataTable.addRow(new Object[] {"Model1", new Date(), 400, 24});
		dataTable.addRow(new Object[] {"Model2", new Date(1199145600), 377, 22});
		dataTable.addRow(new Object[] {"Model2", new Date(), 500, 26});
		widgetData = dataTable.toString();
	    
	  l = new Label(composite, SWT.NONE);
		l.setText("Motion Chart");
		MotionChart motionChart = new MotionChart( composite, SWT.NONE );
		motionChart.setWidgetOptions("{width: 500, height: 300}");
		motionChart.setWidgetData(widgetData);
    gridData = new GridData(500, 300);
    motionChart.setLayoutData(gridData);
    
	  //COLUMN CHART:
    dataTable = new JSONGoogleDataTable();
		dataTable.addColumn("theyear", "Date", "string", null);
		dataTable.addColumn("CO2", "CO2", "number", null);
		dataTable.addColumn("Temperature", "Temperature", "number", null);
		dataTable.addRow(new Object[] {"1970", 300, 22});
		dataTable.addRow(new Object[] {"2009", 400, 24});
		widgetData = dataTable.toString();
		
    l = new Label(composite, SWT.NONE);
		l.setText("Column Chart");
		ColumnChart chart = new ColumnChart( composite, SWT.NONE );
		chart.setWidgetOptions("{width: 300, height: 300}");
		chart.setWidgetData(widgetData);
	  gridData = new GridData(300, 300);
	  chart.setLayoutData(gridData);
	  
	  l = new Label(composite, SWT.NONE);
		l.setText("Table");
		Table table = new Table( composite, SWT.NONE );
//		table.setWidgetOptions("{width: 300, height: 400}");
		table.setWidgetData(widgetData);
	  gridData = new GridData(300, 300);
	  table.setLayoutData(gridData);
	  table.addListener(SWT.Selection, this);
	  l = new Label(composite, SWT.NONE);
		l.setText("Detail");
		//l.setFont(boldFont);
	  detailWidget = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
	  gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
	  detailWidget.setLayoutData(gridData);
	  
	  
	}
	
	//private ISelection selection;
	private Object bean;

	private static final Logger log = Logger.getLogger(ObjectViewer.class);
	
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

	@Override
	public Object getInput() {
		return bean;
	}

	@Override
	public void setInput(Object bean) {
		this.bean = bean;
		refresh();
	}

	public void clearData() {
		idWidget.setText("");
		nameWidget.setText("");
		classWidget.setText("");
		descriptionWidget.setText("");
		detailWidget.setText("");
	}

	public void dispose() {
		idWidget.dispose();
		nameWidget.dispose();
		classWidget.dispose();
		descriptionWidget.dispose();
		detailWidget.dispose();
		composite.dispose();
	}

	public void handleEvent(Event arg0) {
		log.info("Event: " + arg0);
		
	}
}
