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

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.inqle.ui.google.json.JSONGoogleDataTable;

/**
 * Renders a Google Visualization Motion Chart.
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * dataTable = new JSONGoogleDataTable();
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
    </code>
 * @See http://code.google.com/apis/visualization/documentation/gallery/motionchart.html
 * @author David Donohue
 * 2009/4/8
 */
public class MotionChart extends VisualizationWidget {

  public MotionChart( Composite parent, int style ) {
    super( parent, style );
  }
  
}
