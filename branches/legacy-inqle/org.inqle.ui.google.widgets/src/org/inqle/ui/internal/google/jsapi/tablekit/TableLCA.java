package org.inqle.ui.internal.google.jsapi.tablekit;

import java.io.IOException;

import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Widget;
import org.inqle.ui.google.jsapi.Table;
import org.inqle.ui.google.jsapi.VisualizationWidgetLCA;

public class TableLCA extends VisualizationWidgetLCA {
  
  /*
   * Initial creation procedure of the widget
   */
  public void renderInitialization( final Widget widget ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( widget );
    String id = WidgetUtil.getId( widget );
    writer.newWidget( "org.inqle.ui.google.jsapi.Table", new Object[]{
      id
    } );
    writer.set( "appearance", "composite" );
    writer.set( "overflow", "hidden" );
    ControlLCAUtil.writeStyleFlags( ( Table )widget );
  }
}
