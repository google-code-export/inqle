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

import java.io.IOException;

import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.inqle.ui.google.jsapi.MotionChart;

public abstract class VisualizationWidgetLCA extends AbstractWidgetLCA {

  protected static final String PROP_DATA = "widgetData";
  protected static final String PROP_OPTIONS = "widgetOptions";

  public void preserveValues( final Widget widget ) {
    ControlLCAUtil.preserveValues( ( Control )widget );
    IWidgetAdapter adapter = WidgetUtil.getAdapter( widget );
    adapter.preserve( PROP_DATA, ( ( MotionChart )widget ).getWidgetData() );
    adapter.preserve( PROP_OPTIONS, ( ( MotionChart )widget ).getWidgetOptions() );
    // only needed for custom variants (theming)
//    WidgetLCAUtil.preserveCustomVariant( widget );
  }

  public void renderChanges( final Widget widget ) throws IOException {
    VisualizationWidget vWidget = ( VisualizationWidget )widget;
    ControlLCAUtil.writeChanges( vWidget );
    JSWriter writer = JSWriter.getWriterFor( vWidget );
    writer.set( PROP_DATA, PROP_DATA, vWidget.getWidgetData() );
    writer.set( PROP_OPTIONS, PROP_OPTIONS, vWidget.getWidgetOptions() );
  }

  public void renderDispose( final Widget widget ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( widget );
    writer.dispose();
  }

  public void createResetHandlerCalls( String typePoolId ) throws IOException {
  }

  public String getTypePoolId( Widget widget ) {
    return null;
  }
  
  /*
   * Override this (which does nothing) to read the parameters transfered from the client.  
   */
  public void readData( final Widget widget ) {
//    MotionChart motionChart = ( MotionChart )widget;
//    String location = WidgetLCAUtil.readPropertyValue( motionChart, PARAM_CENTER );
//    motionChart.setCenterLocation( location );
  }
}
