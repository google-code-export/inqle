package org.inqle.agent;


import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class InqleCoreAgentActivator implements BundleActivator {
		
		static Logger log = Logger.getLogger(InqleCoreAgentActivator.class);

		private AgentInvestigator agent;
		
		public void start(BundleContext arg0) throws Exception {
			log.info("Starting InqleCoreAgentActivator...");
			agent = new AgentInvestigator();
			agent.start();
			log.info("Started InqleCoreAgentActivator.");
		}

		public void stop(BundleContext arg0) throws Exception {
			log.info("Stopping InqleCoreAgentActivator...");
			agent.signalStop();
			while(!agent.isStopped()) {
				Thread.sleep(100);
			}
			log.info("Stopped InqleCoreAgentActivator.");
		}
}
