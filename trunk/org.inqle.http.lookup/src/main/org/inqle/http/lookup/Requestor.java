/**
 * 
 */
package org.inqle.http.lookup;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;

/**
 * @author David Donohue
 * Jul 15, 2008
 */
public class Requestor {

	private static Logger log = Logger.getLogger(Requestor.class);
	
	/**
	 * Send any Jenabean object to the central INQLE server to register it.
	 * Output response to the system console.
	 * @param object
	 * @return
	 */
	public static boolean registerObject(Object object) {
		return registerObject(object, new PrintWriter(System.out));
	}
	
	/**
	 * Send any Jenabean object to the central INQLE server to register it
	 * @param object
	 * @param outWriter
	 * @return
	 */
	public static boolean registerObject(Object object, Writer outWriter) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(InqleInfo.PARAM_REGISTER_RDF, JenabeanWriter.toString(object));
		return registerData(params, outWriter);
	}
	
	public static boolean registerData(Map<String, String> params, Writer outWriter) {
		return postData(InqleInfo.URL_CENTRAL_REGISTRATION_SERVICE, params, outWriter);
	}
	/**
	* Reads data from the data reader and posts it to a server via POST request.
	* @param url - The server's address
	* @param params - the Map of key-value pairs to send as request variables
	* @param output - writes the server's response to output
	* @throws Exception
	*/
	public static boolean postData(String urlStr, Map<String, String> params, Writer outWriter) {
		//add siteId to the params
		Persister persister = Persister.getInstance();
		params.put(InqleInfo.PARAM_SITE_ID, persister.getAppInfo().getSite().getId());
		params.put(InqleInfo.PARAM_INQLE_VERSION, InqleInfo.getInqleVersion());
		
		URL url;
		boolean success = true;
		
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			log.error("Malformed URL: '" + urlStr + "'.  Not sending HTTP request", e);
			return false;
		}
		HttpURLConnection urlc = null;
		try {
			urlc = (HttpURLConnection) url.openConnection();
			try {
				urlc.setRequestMethod("POST");
			} catch (ProtocolException e) {
				log.error("Should never happen: HttpURLConnection does not support POST?", e);
				return false;
			}
			
			urlc.setDoOutput(true);
			urlc.setDoInput(true);
			urlc.setUseCaches(false);
			urlc.setAllowUserInteraction(false);
			//urlc.setRequestProperty("Content-type", "text/xml; charset=" + "UTF-8");
			urlc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			
			if (params != null && params.size() > 0) {
				String requestParams = "";
				for (String key: params.keySet()) {
					if (requestParams.length() > 0) requestParams += "&";
					String value = URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8");
					requestParams += key + "=" + value;
				}
				urlc.setRequestProperty("Content-Length", String.valueOf(requestParams.length()));
				log.info("Sending request params of length: " + requestParams.length());
				OutputStreamWriter outStream = new OutputStreamWriter(new BufferedOutputStream(urlc.getOutputStream()));
				outStream.write(requestParams);
				outStream.close();
				log.info("added request params:\n" + requestParams);
			}
			
			InputStream in = urlc.getInputStream();
			try {
				Reader reader = new InputStreamReader(in);
				pipe(reader, outWriter);
				reader.close();
			} catch (IOException e) {
				log.error("IOException while reading response", e);
				success = false;
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (IOException e) {
			log.error("Connection error; Unable to connect to server at " + url, e);
			success = false;
		} finally {
			if (urlc != null) {
				urlc.disconnect();
			}
		}
		return success;
	}

	/**
	* Pipes everything from the reader to the writer via a buffer
	*/
	public static void pipe(Reader reader, Writer writer) throws IOException {
		char[] buf = new char[1024];
		int read = 0;
		while ((read = reader.read(buf)) >= 0) {
			writer.write(buf, 0, read);
		}
		writer.flush();
	}

}
