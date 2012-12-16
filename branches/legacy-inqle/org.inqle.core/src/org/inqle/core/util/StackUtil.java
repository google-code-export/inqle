package org.inqle.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class StackUtil {

  public static String exceptionToString(Throwable aThrowable) {
    final Writer result = new StringWriter();
    final PrintWriter printWriter = new PrintWriter(result);
    aThrowable.printStackTrace(printWriter);
    return result.toString();
  }

}
