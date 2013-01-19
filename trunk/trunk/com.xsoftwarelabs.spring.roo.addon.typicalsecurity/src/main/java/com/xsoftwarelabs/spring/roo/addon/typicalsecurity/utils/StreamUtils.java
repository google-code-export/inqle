/**
 * 
 */
package com.xsoftwarelabs.spring.roo.addon.typicalsecurity.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

/**
 * @author rohit 
 *
 */
public class StreamUtils extends IOUtils {
	public static int replaceAndCopy(InputStream in, OutputStream out, Properties replacement) throws IOException {
		Validate.notNull(in, "No InputStream specified");
		Validate.notNull(out, "No OutputStream specified");
        StringBuffer sb = new StringBuffer();
        try {
                int byteCount = 0;
                byte[] buffer = new byte[1000];
                int bytesRead = -1;
                while ((bytesRead = in.read(buffer)) != -1) {
                        sb.append(new String(buffer, 0, bytesRead));
                        byteCount += bytesRead;
                }
                String txt = sb.toString();
                for (Entry<Object, Object> entry : replacement.entrySet()) {
                        txt = txt.replaceAll(entry.getKey().toString(), entry.getValue().toString());
                }
                out.write(txt.getBytes());
                out.flush();
                return byteCount;
        } finally {
                try {
                        in.close();
                } catch (IOException ex) {
                }
                try {
                        out.close();
                } catch (IOException ex) {
                }
        }
}
	
	public static String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}
}
