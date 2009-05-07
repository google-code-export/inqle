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

import org.eclipse.swt.widgets.Composite;

/**
 * Renders a Google Visualization Motion Chart.  This originally appeared at gapminder.org
 * as Gapminder's Trendalyzer.
 * @see http://code.google.com/apis/visualization/documentation/gallery/scatterchart.html
 * @see http://www.ted.com/index.php/talks/hans_rosling_shows_the_best_stats_you_ve_ever_seen.html
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("Model", "Model", "string", null);
    dataTable.addColumn("thedate", "Date", "date", null);
    dataTable.addColumn("CO2", "CO2", "number", null);
    dataTable.addColumn("Temperature", "Temperature", "number", null);
    dataTable.addRow(new Object[] {"Model1", new Date(), 389, 14.8});
    dataTable.addRow(new Object[] {"Model1", new Date(4070908800), 450, 19});
    dataTable.addRow(new Object[] {"Model2", new Date(), 389, 14.8});
    dataTable.addRow(new Object[] {"Model2", new Date(4070908800), 700, 23});
    String serializedData = dataTable.toString();
     
    MotionChart motionChart = new MotionChart( composite, SWT.NONE );
    motionChart.setWidgetOptions("{width: 500, height: 300}");
    motionChart.setWidgetData(serializedData);
    GridData gridData = new GridData(500, 300);
    motionChart.setLayoutData(gridData);
    </code>
 * @author David Donohue
 * 2009/4/8
 */
public class MotionChart extends VisualizationWidget {

  public MotionChart( Composite parent, int style ) {
    super( parent, style );
  }
  
}
