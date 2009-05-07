/*******************************************************************************
 * Copyright (c) 2002-2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.inqle.ui.google.jsapi;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.inqle.ui.google.json.JSONGoogleDataTable;

/**
 * Renders a Google Visualization Line Chart.
 * @see http://code.google.com/apis/visualization/documentation/gallery/linechart.html
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("Month", "Month", "string", null);
    dataTable.addColumn("Provider1", "Provider 1", "number", null);
    dataTable.addColumn("Provider2", "Provider 2", "number", null);
    dataTable.addColumn("Provider3", "Provider 3", "number", null);
    dataTable.addRow(new Object[] {"May", 10, 15, 20});
    dataTable.addRow(new Object[] {"June", 12, 23, 33});
    dataTable.addRow(new Object[] {"July", 11, 25, 50});
    widgetData = dataTable.toString();
    
    LineChart lineChart = new LineChart( composite, SWT.NONE );
    lineChart.setWidgetOptions("{width: 300, height: 300}");
    lineChart.setWidgetData(widgetData);
    gridData = new GridData(300, 300);
    lineChart.setLayoutData(gridData);
    lineChart.addListener(SWT.Selection, this);
    </code>
    
    <code>
    public void handleEvent(Event event) {
    log.info("Event: " + event);
    VisualizationWidget widget = (VisualizationWidget)event.widget;
    log.info( "Selected item=" + widget.getSelectedItem() + 
        "; row=" + widget.getSelectedRow() +
        "; column=" + widget.getSelectedColumn() +
        "; value=" + widget.getSelectedValue());
    </code>
 * @author David Donohue
 * 2009/4/29
 */
public class LineChart extends VisualizationWidget {

  public LineChart( Composite parent, int style ) {
    super( parent, style );
  }
  
}
