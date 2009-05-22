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
import org.inqle.ui.google.jsapi.AnnotatedTimeLine;
import org.inqle.ui.google.jsapi.AreaChart;
import org.inqle.ui.google.jsapi.BarChart;
import org.inqle.ui.google.jsapi.ColumnChart;
import org.inqle.ui.google.jsapi.Gauge;
import org.inqle.ui.google.jsapi.Geomap;
import org.inqle.ui.google.jsapi.IntensityMap;
import org.inqle.ui.google.jsapi.LineChart;
import org.inqle.ui.google.jsapi.MotionChart;
import org.inqle.ui.google.jsapi.PieChart;
import org.inqle.ui.google.jsapi.ScatterChart;
import org.inqle.ui.google.jsapi.Table;
import org.inqle.ui.google.jsapi.VisualizationWidget;
import org.inqle.ui.google.json.JSONGoogleDataTable;
import org.inqle.ui.rap.IDisposableViewer;

public class ObjectViewer extends Viewer implements IDisposableViewer, Listener {

	private Composite composite;
	private Text idWidget;
	private Text nameWidget;
	private Text classWidget;
	private Text descriptionWidget;
	private Text detailWidget;
	
	//private ISelection selection;
	private Object bean;

	private static final Logger log = Logger.getLogger(ObjectViewer.class);
	
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
		motionChart.setWidgetOptions("{width: 400, height: 500}");
		motionChart.setWidgetData(widgetData);
    gridData = new GridData(400, 500);
    motionChart.setLayoutData(gridData);
    
    //ANNOTATED TIMELINE
    dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("Date", "Date", "date", null);
    dataTable.addColumn("AverageGPA", "Average GPA", "number", null);
    dataTable.addColumn("Description", "Description", "string", null);
    dataTable.addColumn("Description2", "Description2", "string", null);
    
    dataTable.addRow(new Object[] {new Date(1210000000), 3, "First course", "traditional teaching method used"});
    dataTable.addRow(new Object[] {new Date(1230809560), 3.5, "new course", "newer teaching method used"});
    dataTable.addRow(new Object[] {new Date(), 2.85});
//    dataTable.addRow(new Object[] {new Date(1210000000), 3});
//    dataTable.addRow(new Object[] {new Date(1230809560), 3.5});
//    dataTable.addRow(new Object[] {new Date(), 2.85});
    widgetData = dataTable.toString();
    
    l = new Label(composite, SWT.NONE);
    l.setText("Annotated Time Line");
    AnnotatedTimeLine timeLine = new AnnotatedTimeLine( composite, SWT.NONE );
    timeLine.setWidgetOptions("{width: 500, height: 300, displayAnnotations: true}");
    timeLine.setWidgetData(widgetData);
    gridData = new GridData(500, 300);
    timeLine.setLayoutData(gridData);
    timeLine.addListener(SWT.Selection, this);
    
    //GEOMAP
    dataTable = new JSONGoogleDataTable();
		dataTable.addColumn("Country", "Country", "string", null);
		dataTable.addColumn("Happiness", "Happiness", "number", null);
		dataTable.addRow(new Object[] {"Tanzania", 25});
		dataTable.addRow(new Object[] {"US", 40});
		widgetData = dataTable.toString();
		
    l = new Label(composite, SWT.NONE);
		l.setText("Geomap");
		Geomap geomap = new Geomap( composite, SWT.NONE );
		geomap.setWidgetOptions("{width: 500, height: 500}");
		geomap.setWidgetData(widgetData);
	  gridData = new GridData(500, 500);
	  geomap.setLayoutData(gridData);
	  geomap.addListener(SWT.Selection, this);
	  
	  //INTENSITYMAP - renders in upper left of panel
//    dataTable = new JSONGoogleDataTable();
//		dataTable.addColumn("Country", "Country", "string", null);
//		dataTable.addColumn("Happiness", "Happiness", "number", null);
//		dataTable.addColumn("Income", "Income", "number", null);
//		dataTable.addRow(new Object[] {"TZ", 25, 3});
//		dataTable.addRow(new Object[] {"US", 40, 40});
//		dataTable.addRow(new Object[] {"UK", 38, 35});
//		widgetData = dataTable.toString();
//		
//    l = new Label(composite, SWT.NONE);
//		l.setText("Intensity Map");
//		IntensityMap intensityMap = new IntensityMap( composite, SWT.NONE );
//		intensityMap.setWidgetOptions("{width: 440, height: 220}");
//		intensityMap.setWidgetData(widgetData);
//	  gridData = new GridData(440, 220);
//	  intensityMap.setLayoutData(gridData);
//	  intensityMap.addListener(SWT.Selection, this);
    
	  //COLUMN CHART:
    dataTable = new JSONGoogleDataTable();
		dataTable.addColumn("theyear", "Date", "string", null);
		dataTable.addColumn("CO2", "CO2", "number", null);
		dataTable.addColumn("Temperature", "Temperature", "number", null);
		dataTable.addRow(new Object[] {"1970", 325, 14.1});
		dataTable.addRow(new Object[] {"2009", 389, 14.7});
		widgetData = dataTable.toString();
		
    l = new Label(composite, SWT.NONE);
		l.setText("Column Chart");
		ColumnChart columnChart = new ColumnChart( composite, SWT.NONE );
		columnChart.setWidgetOptions("{width: 300, height: 300}");
		columnChart.setWidgetData(widgetData);
	  gridData = new GridData(300, 300);
	  columnChart.setLayoutData(gridData);
	  columnChart.addListener(SWT.Selection, this);
	  
	  //BAR CHART
	  l = new Label(composite, SWT.NONE);
		l.setText("Bar Chart");
		BarChart barChart = new BarChart( composite, SWT.NONE );
		barChart.setWidgetOptions("{width: 300, height: 300}");
		barChart.setWidgetData(widgetData);
	  gridData = new GridData(300, 300);
	  barChart.setLayoutData(gridData);
	  barChart.addListener(SWT.Selection, this);
	  
	  //AREA CHART
	  l = new Label(composite, SWT.NONE);
		l.setText("Area Chart");
		AreaChart areaChart = new AreaChart( composite, SWT.NONE );
		areaChart.setWidgetOptions("{width: 300, height: 300}");
		areaChart.setWidgetData(widgetData);
	  gridData = new GridData(300, 300);
	  areaChart.setLayoutData(gridData);
	  areaChart.addListener(SWT.Selection, this);
	  
	  //GAGUE
	  dataTable = new JSONGoogleDataTable();
		dataTable.addColumn("CO2", "CO2", "number", null);
		dataTable.addColumn("CH4", "CH4", "number", null);
		dataTable.addColumn("Temperature", "Temperature", "number", null);
		dataTable.addRow(new Object[] {389, 1800, 14});
		widgetData = dataTable.toString();
	  
	//BAR CHART
	  l = new Label(composite, SWT.NONE);
		l.setText("Gauge");
		Gauge gauge = new Gauge( composite, SWT.NONE );
		gauge.setWidgetOptions("{width: 300, height: 300}");
		gauge.setWidgetData(widgetData);
	  gridData = new GridData(300, 300);
	  gauge.setLayoutData(gridData);
	  
	  //SCATTER CHART
	  dataTable = new JSONGoogleDataTable();
		dataTable.addColumn("CO2", "CO2", "number", null);
		dataTable.addColumn("CH4", "CH4", "number", null);
		dataTable.addColumn("Temperature", "Temperature", "number", null);
		dataTable.addRow(new Object[] {350, 10, 22});
		dataTable.addRow(new Object[] {375, 12, 23});
		dataTable.addRow(new Object[] {400, 16, 25});
		widgetData = dataTable.toString();
		
		l = new Label(composite, SWT.NONE);
		l.setText("Scatter Chart");
		ScatterChart scatterChart = new ScatterChart( composite, SWT.NONE );
		scatterChart.setWidgetOptions("{width: 300, height: 300}");
		scatterChart.setWidgetData(widgetData);
	  gridData = new GridData(300, 300);
	  scatterChart.setLayoutData(gridData);
	  scatterChart.addListener(SWT.Selection, this);
	  
	  dataTable = new JSONGoogleDataTable();
		dataTable.addColumn("Activity", "Activity", "string", null);
		dataTable.addColumn("Hours", "Hours per Week", "number", null);
		dataTable.addRow(new Object[] {"software architect", 40});
		dataTable.addRow(new Object[] {"primary care medicine", 9});
		dataTable.addRow(new Object[] {"open source development", 10});
		widgetData = dataTable.toString();
		
		l = new Label(composite, SWT.NONE);
		l.setText("Pie Chart");
		PieChart pieChart = new PieChart( composite, SWT.NONE );
		pieChart.setWidgetOptions("{width: 300, height: 300}");
		pieChart.setWidgetData(widgetData);
	  gridData = new GridData(300, 300);
	  pieChart.setLayoutData(gridData);
	  pieChart.addListener(SWT.Selection, this);
	  
	  dataTable = new JSONGoogleDataTable();
	  dataTable.addColumn("Month", "Month", "string", null);
		dataTable.addColumn("Provider1", "Provider 1", "number", null);
		dataTable.addColumn("Provider2", "Provider 2", "number", null);
		dataTable.addColumn("Provider3", "Provider 3", "number", null);
		dataTable.addRow(new Object[] {"May", 10, 15, 20});
		dataTable.addRow(new Object[] {"June", 12, 23, 33});
		dataTable.addRow(new Object[] {"July", 11, 25, 50});
		widgetData = dataTable.toString();
		
		l = new Label(composite, SWT.NONE);
		l.setText("Line Chart");
		LineChart lineChart = new LineChart( composite, SWT.NONE );
		lineChart.setWidgetOptions("{width: 300, height: 300}");
		lineChart.setWidgetData(widgetData);
	  gridData = new GridData(300, 300);
	  lineChart.setLayoutData(gridData);
	  lineChart.addListener(SWT.Selection, this);
	  
	  l = new Label(composite, SWT.NONE);
		l.setText("Table");
		Table table = new Table( composite, SWT.NONE );
		table.setWidgetOptions("{width: 300, height: 200}");
		table.setWidgetData(widgetData);
	  gridData = new GridData(300, 200);
	  table.setLayoutData(gridData);
	  table.addListener(SWT.Selection, this);
	  
	  l = new Label(composite, SWT.NONE);
		l.setText("Detail");
		//l.setFont(boldFont);
	  detailWidget = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
	  gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
	  detailWidget.setLayoutData(gridData);
	
	}
	
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

	public void handleEvent(Event event) {
		log.info("Event: " + event);
		VisualizationWidget widget = (VisualizationWidget)event.widget;
		log.info( "Selected item=" + widget.getSelectedItem() + 
				"; row=" + widget.getSelectedRow() +
				"; column=" + widget.getSelectedColumn() +
				"; value=" + widget.getSelectedValue());
	}
}