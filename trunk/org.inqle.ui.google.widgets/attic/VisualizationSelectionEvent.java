/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.inqle.ui.google;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;


public class VisualizationSelectionEvent extends SelectionEvent {

  private Object source;
  public VisualizationSelectionEvent() {
    this(new Event());
  }
  
  public VisualizationSelectionEvent(Event e) {
    super(e);
  }
  public void setWidget(Widget widget) {
    this.widget = widget;
  }
  public void setSource(Object source) {
    this.source = source;
  }

  @Override
  public Object getSource() {
    return source;
  }
}
