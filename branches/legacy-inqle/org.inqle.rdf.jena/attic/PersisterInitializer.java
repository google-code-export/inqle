package org.inqle.data.rdf.jena.util;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Persister;

public class PersisterInitializer {

        private static boolean initialized = false;
        public static Logger log = Logger.getLogger(PersisterInitializer.class);
        
        public static boolean initialize() {
                
                if (initialized) return true;
                
                try {
                        //do any initialization of Persister
                        Persister persister = Persister.getInstance();
                        
                        //initialize internal datasets
                        persister.getInternalDatasets();
                        
                        //initialize LARQ indexes
                        persister.getIndexBuilders();
                        
                        initialized = true;
                } catch (RuntimeException e) {
                        log.error("Unable to initialize the Persister.", e);
                }
                
                return initialized;
        }
}
