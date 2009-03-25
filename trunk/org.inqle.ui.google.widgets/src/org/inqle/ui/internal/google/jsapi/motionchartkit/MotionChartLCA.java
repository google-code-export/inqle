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
package org.inqle.ui.internal.google.jsapi.motionchartkit;

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

public class MotionChartLCA extends AbstractWidgetLCA {

  private static final String PARAM_CENTER = "centerLocation";
//  private static final String JS_PROP_ADDRESS = "address";
//  private static final String PROP_ADDRESS = "address";

  public void preserveValues( final Widget widget ) {
    ControlLCAUtil.preserveValues( ( Control )widget );
    IWidgetAdapter adapter = WidgetUtil.getAdapter( widget );
//    adapter.preserve( PROP_ADDRESS, ( ( MotionChart )widget ).getAddress() );
    
    // only needed for custom variants (theming)
//    WidgetLCAUtil.preserveCustomVariant( widget );
  }

  /*
   * Read the parameters transfered from the client
   */
  public void readData( final Widget widget ) {
    MotionChart motionChart = ( MotionChart )widget;
//    String location = WidgetLCAUtil.readPropertyValue( motionChart, PARAM_CENTER );
//    motionChart.setCenterLocation( location );
  }

  /*
   * Initial creation procedure of the widget
   */
  public void renderInitialization( final Widget widget ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( widget );
    String id = WidgetUtil.getId( widget );
    writer.newWidget( "org.inqle.ui.google.jsapi.MotionChart", new Object[]{
      id
    } );
    writer.set( "appearance", "composite" );
    writer.set( "overflow", "hidden" );
    ControlLCAUtil.writeStyleFlags( ( MotionChart )widget );
  }

  public void renderChanges( final Widget widget ) throws IOException {
    MotionChart motionChart = ( MotionChart )widget;
    ControlLCAUtil.writeChanges( motionChart );
    JSWriter writer = JSWriter.getWriterFor( widget );
//    writer.set( PROP_ADDRESS, JS_PROP_ADDRESS, motionChart.getAddress() );
    
    // only needed for custom variants (theming)
    WidgetLCAUtil.writeCustomVariant( widget );
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
}
