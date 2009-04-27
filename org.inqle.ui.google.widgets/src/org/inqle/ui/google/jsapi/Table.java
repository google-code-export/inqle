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
 * Renders a Google Visualization Table widget.
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("theyear", "Date", "string", null);
    dataTable.addColumn("CO2", "CO2", "number", null);
    dataTable.addColumn("Temperature", "Temperature", "number", null);
    dataTable.addRow(new Object[] {"1970", 300, 22});
    dataTable.addRow(new Object[] {"2009", 400, 24});
    String serializedData = dataTable.toString();
    
 * l = new Label(composite, SWT.NONE);
    l.setText("Table");
    Table table = new Table( composite, SWT.NONE );
    table.setWidgetOptions("{width: 300, height: 400}");
    table.setWidgetData(serializedData);
    gridData = new GridData(300, 300);
    table.setLayoutData(gridData);
    
 * @See http://code.google.com/apis/visualization/documentation/gallery/table.html
 * @author David Donohue
 * 2009/4/21
 */
public class Table extends VisualizationWidget {

  private String selectedItem;
//  private List<SelectionListener> listeners = new ArrayList<SelectionListener>();
  
  public Table( final Composite parent, final int style ) {
    super( parent, style );
  }

  public void setSelectedItem( String selectedItem ) {
    this.selectedItem = selectedItem;
//    selectionChanged();
  }

  public String getSelectedItem() {
    return selectedItem;
  }

//  public void addSelectionListener(SelectionListener listener) {
//    listeners.add(listener);
//  }
  
//  public void addSelectionListener (SelectionListener listener) {
//    checkWidget ();
//    TypedListener typedListener = new TypedListener (listener);
//    addListener (SWT.Selection,typedListener);
//    addListener (SWT.DefaultSelection,typedListener);
//  }
  
//  public void removeSelectionListener(SelectionListener listener) {
//    listeners.remove(listener);
//  }
  
//  public void removeSelectionListener (SelectionListener listener) {
//    checkWidget();
//    TypedListener typedListener = new TypedListener(listener);
//    removeListener(SWT.Selection,typedListener);
//    removeListener(SWT.DefaultSelection,typedListener);
//  }
  
//  private void selectionChanged() {
//    Event event = new Event();
//    event.data = selectedItem;
//    event.text = selectedItem;
//    event.widget = this;
//    VisualizationSelectionEvent selectionEvent = new VisualizationSelectionEvent(event);
//    selectionEvent.setWidget(this);
//    selectionEvent.setSource(this);
//    selectionEvent.data = selectedItem;
//    selectionEvent.text = selectedItem;
//    for (SelectionListener listener: listeners) {
//      listener.widgetSelected( selectionEvent );
//    }
//  }
  
  
}
