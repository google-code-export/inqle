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
import org.eclipse.swt.widgets.Layout;

/**
 * Abstract class for rendering any of the Google Visualization API UI elements.
 * 
 * @See http://code.google.com/apis/visualization/
 * @author David Donohue
 * 2009/4/18
 */
public abstract class VisualizationWidget extends Composite {
  
  private String widgetData = "";
  private String widgetOptions = "";
  private String selectedItem = "";

  public VisualizationWidget( final Composite parent, final int style ) {
    super( parent, style );
  }

  public String getWidgetData() {
    return widgetData;
  }

  /*
   * Intentionally commented out as a Motion Chart cannot have a layout
   */
  public void setLayout( final Layout layout ) {
  }

  
  public void setWidgetData( String widgetData ) {
    if (widgetData==null) {
      this.widgetData = "";
    } else {
      this.widgetData = widgetData;
    }
  }

  public void setWidgetOptions( String widgetOptions ) {
    if (widgetOptions==null) {
      widgetOptions = "";
    } else {
      this.widgetOptions = widgetOptions;
    }
  }

  public String getWidgetOptions() {
    return widgetOptions;
  }
  
  public void setSelectedItem( String selectedItem ) {
    this.selectedItem  = selectedItem;
  }

  public String getSelectedItem() {
    return selectedItem;
  }
  
}
