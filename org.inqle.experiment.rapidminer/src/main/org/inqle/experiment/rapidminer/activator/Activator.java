package org.inqle.experiment.rapidminer.activator;
import java.io.File;
import java.io.InputStream;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.inqle.core.util.InqleInfo;
import org.inqle.ui.rap.pages.SubjectPropertyMappingsPage;
import org.osgi.framework.BundleContext;

import com.rapidminer.RapidMiner;
import com.rapidminer.gui.renderer.RendererService;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.OperatorService;
import com.rapidminer.tools.ParameterService;
import com.rapidminer.tools.Tools;
import com.rapidminer.tools.WekaOperatorFactory;
import com.rapidminer.tools.WekaTools;
import com.rapidminer.tools.XMLSerialization;
import com.rapidminer.tools.cipher.CipherTools;
import com.rapidminer.tools.cipher.KeyGenerationException;
import com.rapidminer.tools.cipher.KeyGeneratorTool;
import com.rapidminer.tools.jdbc.DatabaseService;

/**
 * 
 */

/**
 * @author David Donohue
 * Apr 18, 2008
 */
public class Activator extends Plugin {

//	private static final String SYSTEM_PROPERTY_RAPIDMINER_HOME = "rapidminer.home";

	private static Logger log = Logger.getLogger(Activator.class);
	
	/**
	 * 
	 */
	public Activator() {
		super();
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		super.start(context);
		
		//initialize RapidMiner
		String rapidMinerHome = InqleInfo.getPluginsDirectory() + context.getBundle().getSymbolicName();
		System.setProperty(RapidMiner.PROPERTY_RAPIDMINER_HOME, rapidMinerHome);
//		System.setProperty(RapidMiner.PROPERTY_RAPIDMINER_WEKA_JAR, rapidMinerHome + "/lib/weka.jar");
		System.setProperty(RapidMiner.PROPERTY_RAPIDMINER_WEKA_JAR, rapidMinerHome + "_1.0.0.jar");
//		log.info("Weka jar=" + System.getProperty(RapidMiner.PROPERTY_RAPIDMINER_WEKA_JAR));
//		boolean addWekaOperators = true;
//		boolean searchJDBCInLibDir = false;
//		boolean searchJDBCInClasspath = false;
//		boolean addPlugins = true;
//		RapidMiner.init(
//				addWekaOperators, 
//				searchJDBCInLibDir, 
//				searchJDBCInClasspath, 
//				addPlugins);
		initRapidMiner();
		
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		RapidMiner.cleanUp();
		super.stop(context);
	}
	
	public void initRapidMiner() {		
		// set locale fix to US
		RapidMiner.splashMessage("Using US Local");
		Locale.setDefault(Locale.US);
		
		// ensure rapidminer.home is set
		RapidMiner.splashMessage("Ensure RapidMiner Home is set");
		ParameterService.ensureRapidMinerHomeSet();
		
//		if (addPlugins) {
//		RapidMiner.splashMessage("Register Plugins");
//		Plugin.registerAllPlugins(pluginDir, true);
//		}
//		Plugin.initPluginSplashTexts();
//		RapidMiner.showSplashInfos();
		
		RapidMiner.splashMessage("Initializing Operators");
		boolean addWekaOperators = true;
		ParameterService.init(null, null, addWekaOperators);
		
		WekaOperatorFactory woFactory = new WekaOperatorFactory();
		woFactory.registerOperators(getClass().getClassLoader());
		
		RapidMiner.splashMessage("Loading JDBC Drivers");
		DatabaseService.init(null, false, false);
		
		RapidMiner.splashMessage("Initialize XML serialization");
		XMLSerialization.init(this.getClass().getClassLoader());
		
//		RapidMiner.splashMessage("Define XML Serialization Alias Pairs");
		OperatorService.defineXMLAliasPairs();
		
		// generate encryption key if necessary
		if (!CipherTools.isKeyAvailable()) {
		RapidMiner.splashMessage("Generate Encryption Key");
		try {
		KeyGeneratorTool.createAndStoreKey();
		} catch (KeyGenerationException e) {
		LogService.getGlobal().logError("Cannot generate encryption key: " + e.getMessage());
		}
		}
		
		// initialize renderers
		RapidMiner.splashMessage("Initialize renderers");
		RendererService.init();
		
//		try {
//			OperatorDescription m5pDescription = new OperatorDescription(getClass().getClassLoader(), "W-M5P", "weka.classifiers.trees.M5P", "Weka M5 Prime Decision Tree Learner", "", "", "", "");
//			OperatorService.registerOperator(m5pDescription);
//		} catch (Exception e) {
//			log.error("Unable to register M5 prime operator", e);
//		}
		
		
//		WekaTools.registerWekaOperators(getClass().getClassLoader(), WekaTools.getWekaClasses(weka.classifiers.Classifier.class, ENSEMBLE_CLASSIFIERS , null), "com.rapidminer.operator.learner.weka.GenericWekaEnsembleLearner", "The weka ensemble learner", "Learner.Supervised.Weka.", null);

	}
}
