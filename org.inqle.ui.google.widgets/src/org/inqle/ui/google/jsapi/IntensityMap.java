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
 * Renders a Google Visualization Bar Chart.
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addBar("theyear", "Date", "string", null);
    dataTable.addBar("CO2", "CO2", "number", null);
    dataTable.addBar("Temperature", "Temperature", "number", null);
    dataTable.addRow(new Object[] {"1970", 300, 22});
    dataTable.addRow(new Object[] {"2009", 400, 24});
    String serializedData = dataTable.toString();
    
    l = new Label(composite, SWT.NONE);
    l.setText("Bar Chart");
    IntensityMap chart = new IntensityMap( composite, SWT.NONE );
    chart.setWidgetOptions("{width: 300, height: 300}");
    chart.setWidgetData(serializedData);
    gridData = new GridData(300, 300);
    chart.setLayoutData(gridData);
    
 * @See http://code.google.com/apis/visualization/documentation/gallery/columnchart.html
 * @author David Donohue
 * 2009/4/8
 */
public class IntensityMap extends VisualizationWidget {

  public IntensityMap( final Composite parent, final int style ) {
    super( parent, style );
  }

}
