package org.inqle.data.rdf.jena.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

public class Loader {

	/** types of RDF file formats to try to import */
  private static final String[] langs = {
  	"RDF/XML", "N-TRIPLE", "N3"
  };
  
	public static Logger log = Logger.getLogger(Loader.class);
	
	private Model model;

	private Exception exception;

	private long countLoaded;

	public Loader(Model model) {
		this.model = model;
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
      model.begin();
      
    	InputStream in = new FileInputStream(file);
      if (in == null) {
        throw new IOException("No InputStream provided.  Unable to load file.");
      }

      //try reading from each of 3 RDF languages, or from Excel 97+
      //boolean successReadingCurrentURL = false;
      
	    for (int i=0; i<langs.length; i++) {
	    	try {
	    		log.info("Trying to read file '" + file.getPath() + "' using " + langs[i] + " format...");
	    		//Read the triples from the file into the model
	    		model = model.read(in, defaultUri, langs[i]);
	    		overallSuccessLoading = true;
	    		break;
	    	} catch (Exception e) {
	    		log.info("Unable to read file '" + file.getPath() + "' using " + langs[i] + " format.");
	    		//refresh inputstream
	    		in = new FileInputStream(file);
	    	}
      }
      if (!overallSuccessLoading) {
      	log.error("FAILED to load file '" + file.getPath() + "'");
      } else {
	      log.info("SUCCESS loading: model now has " + model.size() + " statements.");
	      countLoaded = model.size() - initialSize;
      }
      model.commit();
    } catch (Exception e) {
      log.error("Exception loading into model", e);
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
