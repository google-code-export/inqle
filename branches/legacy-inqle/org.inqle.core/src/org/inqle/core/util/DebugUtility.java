package org.inqle.core.util;

/*
 * DebugUtility.java
 *
 * Created on October 12, 2005, 11:21 AM
 *
 */

import java.io.*;

/**
 *
 * @author David Donohue
 */
public class DebugUtility {
  
  /** Creates a new instance of DebugUtility */
  public DebugUtility() {
  }
  
  public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
  
}
