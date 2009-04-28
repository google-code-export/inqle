package org.inqle.ui.internal.google.jsapi.tablekit;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;
import org.inqle.ui.google.jsapi.Table;
import org.inqle.ui.google.jsapi.VisualizationWidgetLCA;

public class TableLCA extends VisualizationWidgetLCA {

  private static final Logger log = Logger.getLogger(TableLCA.class);
  
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
  
  public void readData( final Widget widget ) {
    log.info( "TableLCA.readData(): widget=" + widget);
    Table table = ( Table )widget;
    String selectedItem = WidgetLCAUtil.readPropertyValue( table, "selectedItem" );
    table.setSelectedItem( selectedItem );
    log.info( "TableLCA.readData(): selectedItem=" + selectedItem);
    ControlLCAUtil.processSelection( table, null, true );
  }
}
