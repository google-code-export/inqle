package org.inqle.ui.google.jsapi;

import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public abstract class GoogleVisualizationResource implements IResource {

  public String getCharset() {
    return HTML.CHARSET_NAME_ISO_8859_1;
  }

  public ClassLoader getLoader() {
    return this.getClass().getClassLoader();
  }

  public RegisterOptions getOptions() {
    return RegisterOptions.VERSION;
  }

  public abstract String getLocation();

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isExternal() {
    return false;
  }
}
