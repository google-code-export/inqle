/**
 * 
 */
package org.inqle.testing;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.agent.AAgent;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * @author David Donohue
 * Apr 25, 2008
 */
public class JUnitTestRunnerAgent extends AAgent {

	private IJUnitTest[] testsToRun;

	private List<Result> testingResults = new ArrayList<Result>();
	
	private static Logger log = Logger.getLogger(JUnitTestRunnerAgent.class);
	
	public void clone(JUnitTestRunnerAgent objectToClone) {
		setTestsToRun(objectToClone.getTestsToRun());
		super.clone(objectToClone);
	}
	
	/**
	 * @see org.inqle.data.rdf.jenabean.BasicJenabean#createClone()
	 */
	@Override
	public IBasicJenabean createClone() {
		JUnitTestRunnerAgent newAgent = new JUnitTestRunnerAgent();
		newAgent.clone(this);
		return newAgent;
	}

	/**
	 * @see org.inqle.data.rdf.jenabean.BasicJenabean#createReplica()
	 */
	@Override
	public IBasicJenabean createReplica() {
		JUnitTestRunnerAgent newAgent = new JUnitTestRunnerAgent();
		newAgent.replicate(this);
		return newAgent;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		mode = RUNNING;
		log.info("Starting to run()");
		
		IJUnitTest[] testsToRun =	selectTestsToRun();

		
		log.info("Running these tests: " + getTestsToRun());
		//run each test
		for (IJUnitTest junitTest: testsToRun) {
			if (mode != RUNNING) {
				break;
			}
			
			Result result = JUnitCore.runClasses(junitTest.getJUnitTestClass());
			
			testingResults.add(result);
			log.info("JUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNITJUNIT\nRan JUnit test: " + junitTest + ".  Results=\n");
			log.info("getRunCount()=" + result.getRunCount());
			log.info("getRunTime()=" + result.getRunTime());
			log.info("getFailureCount()=" + result.getFailureCount());
		}
		
		mode = STOPPED;
	}

	private IJUnitTest[] getTestsToRun() {
		return testsToRun;
	}
	
	private void setTestsToRun(IJUnitTest[] testsToRun) {
		this.testsToRun = testsToRun;
	}

	private IJUnitTest[] selectTestsToRun() {
		if (getTestsToRun() != null) {
			return getTestsToRun();
		}
		List<IJUnitTest> listOfTests = JUnitTestLister.listJUnitTests();
		IJUnitTest[] testArray = {};
		return listOfTests.toArray(testArray);
	}

}
