package org.inqle.data.rdf.jena.load;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.RDFReaderFImpl;

public class Loader {

	public static final String LANG_RDF_XML = "RDF/XML";
	public static final String LANG_N_TRIPLE = "N-TRIPLE";
	public static final String LANG_N3 = "N3";

	/** types of RDF file formats to try to import */
  private static final String[] langs = {
  	LANG_N3, LANG_RDF_XML, LANG_N_TRIPLE
  };
  
	public static Logger log = Logger.getLogger(Loader.class);
	
	private Model model;

	private Exception exception;

	private long countLoaded;

	public Loader(Model model) {
		this.model = model;
	}
	
	public boolean loadString(String string, String defaultUri, String lang) {
		byte bytes[];
		try {
			bytes = string.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			log.warn("Unable to read bytes as UTF8");
			bytes = string.getBytes();
		}
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		return loadStream(byteArrayInputStream, defaultUri, lang);
	}
	
	public void loadFile(String filePath, String defaultUri) {
		log.info("loading file from: " + filePath);
		File file = new File(filePath);
		//String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
		load(file, defaultUri);
	}
	
	public boolean load(File file, String defaultUri) {
		boolean overallSuccessLoading = false;
		long initialSize = model.size();
		try {

      // Importing models inside a transaction helps performance.
      // Without this, the model each statement is auto-committed as it is
      // added.
			log.info("Before loading: model has " + initialSize + " statements.");
//      model.begin();
      
    	InputStream in = new FileInputStream(file);
      if (in == null) {
        throw new IOException("No InputStream provided.  Unable to load file.");
      }

      //try reading from each of 3 RDF languages, or from Excel 97+
      //boolean successReadingCurrentURL = false;
      
	    for (int i=0; i<langs.length; i++) {
	    	
	    	overallSuccessLoading = loadStream(in, defaultUri, langs[i]);
	    	if (overallSuccessLoading) {
	    		break;
	    	} else {
	    		//refresh the input stream
	    		in = new FileInputStream(file);
	    	}
	    	
//	    	try {
//	    		log.info("Trying to read file '" + file.getPath() + "' using " + langs[i] + " format...");
//	    		//Read the triples from the file into the model
//	    		model = model.read(in, defaultUri, langs[i]);
//	    		overallSuccessLoading = true;
//	    		break;
//	    	} catch (Exception e) {
//	    		log.info("Unable to read file '" + file.getPath() + "' using " + langs[i] + " format.");
//	    		//refresh inputstream
//	    		in = new FileInputStream(file);
//	    	}
      }
	    
      if (!overallSuccessLoading) {
      	log.error("FAILED to load file '" + file.getPath() + "'");
      } else {
	      log.info("SUCCESS loading: model now has " + model.size() + " statements.");
	      countLoaded = model.size() - initialSize;
      }
//      model.commit();
    } catch (Exception e) {
      log.error("Exception loading into model", e);
//      model.abort();
      setError(e);
    } finally {

//      try {
//        // Close the database connection
//    	  model.close();
//      } catch (Exception e) {
//      	log.error("Error closing database connection.", e);
//      }
    }
    return overallSuccessLoading;
	}
	
	public boolean loadStream(InputStream in, String defaultUri, String lang) {
		RDFReaderFImpl.setBaseReaderClassName
		("N3",
		com.hp.hpl.jena.n3.turtle.TurtleReader.class.getName() ) ;
		boolean success = false;
		int available = -1;
		try {
			available = in.available();
  		log.info("Trying to read input stream using " + lang + " format...");
  		//Read the triples from the input stream into the model
//  		model = model.read(in, defaultUri, lang);
  		model.read(in, defaultUri, lang);
  		success = true;
  	} catch (Exception e) {
  		log.error("Unable to read stream (w/ in.available()=" + available + ") using " + lang + " format.", e);
//  		log.info("Unable to read stream using " + lang + " format.");
  	}
  	return success;
	}

	private void setError(Exception exception) {
		this.exception = exception;
	}
	
	public Exception getError() {
		return exception;
	}

	public long getCountLoaded() {
		return countLoaded;
	}
}
