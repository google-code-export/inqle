package org.inqle.ui.google.jsapi;
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
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public class ColumnChartAPIResource implements IResource {

  private String location;

  public String getCharset() {
    return HTML.CHARSET_NAME_ISO_8859_1;
  }

  public ClassLoader getLoader() {
    return this.getClass().getClassLoader();
  }

  public RegisterOptions getOptions() {
    return RegisterOptions.VERSION;
  }

  public String getLocation() {
//    location = "http://www.google.com/jsapi";
//    location = "http://localhost:7500/web_files/loadVisualizationAPI.js";
    location = "org/inqle/ui/google/jsapi/ColumnChartAPI.js";
    return location;
  }

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isExternal() {
    return false;
  }
}
