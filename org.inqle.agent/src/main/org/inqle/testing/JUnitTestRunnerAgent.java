/**
 * 
 */
package org.inqle.testing;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.agent.AAgent;
import org.inqle.agent.AgentInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.INamedAndDescribedJenabean;
import org.inqle.data.rdf.jenabean.TargetDatabaseId;
import org.inqle.data.rdf.jenabean.TargetDatamodelName;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import thewebsemantic.Namespace;

/**
 * @author David Donohue
 * Apr 25, 2008
 */
@TargetDatabaseId(AgentInfo.AGENT_DB)
@TargetDatamodelName(AgentInfo.AGENT_DATASET)
@Namespace(RDF.INQLE)
public class JUnitTestRunnerAgent extends AAgent {

	private IJUnitTest[] testsToRun;

	private List<Result> testingResults = new ArrayList<Result>();
	
	private static Logger log = Logger.getLogger(JUnitTestRunnerAgent.class);
	
//	public void clone(JUnitTestRunnerAgent objectToClone) {
//		setTestsToRun(objectToClone.getTestsToRun());
//		super.clone(objectToClone);
//	}
//	
//	/**
//	 * @see org.inqle.data.rdf.jenabean.NamedAndDescribedJenabean#createClone()
//	 */
//	public JUnitTestRunnerAgent createClone() {
//		JUnitTestRunnerAgent newAgent = new JUnitTestRunnerAgent();
//		newAgent.clone(this);
//		return newAgent;
//	}

//	/**
//	 * @see org.inqle.data.rdf.jenabean.NamedAndDescribedJenabean#createReplica()
//	 */
//	public JUnitTestRunnerAgent createReplica() {
//		JUnitTestRunnerAgent newAgent = new JUnitTestRunnerAgent();
//		newAgent.replicate(this);
//		return newAgent;
//	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		setRunning();
		
		log.info("Starting to run()");
		
		IJUnitTest[] testsToRun =	selectTestsToRun();

		//run each test
		for (IJUnitTest junitTest: testsToRun) {
			if (getMode() != RUNNING) {
				break;
			}
			log.info("JUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNIT\nRunning JUnit test of class: " + junitTest.getJUnitTestClass() + " and name: " + junitTest.getName() + "...");
			Result result = JUnitCore.runClasses(junitTest.getJUnitTestClass());
			
			testingResults.add(result);
			
			log.info("...finished running JUnit test " + junitTest.getJUnitTestClass() + "\nResults=");
			log.info("getRunCount()=" + result.getRunCount());
			log.info("getRunTime()=" + result.getRunTime());
			log.info("getFailureCount()=" + result.getFailureCount());
			int cnt = 0;
			for (Failure failure: result.getFailures()) {
				cnt++;
				log.info("\n\nFailure #" + cnt + ":\n" + failure + "\nStack Trace:\n" + failure.getTrace());
			}
		}
		
		setStopped();
	}

	private IJUnitTest[] getTestsToRun() {
		return testsToRun;
	}
	
	private void setTestsToRun(IJUnitTest[] testsToRun) {
		this.testsToRun = testsToRun;
	}

	private IJUnitTest[] selectTestsToRun() {
		if (getTestsToRun() == null) {
			List<IJUnitTest> listOfTests = JUnitTestLister.listJUnitTests();
			IJUnitTest[] testArray = {};
			setTestsToRun(listOfTests.toArray(testArray));
		}
		return getTestsToRun();
	}

}
