/**
 * 
 */
package org.inqle.data.rdf.jena.fn;

import java.util.Random;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase0;
import com.hp.hpl.jena.sparql.function.FunctionBase1;

/**
 * This is designed to be used in a SPARQL query,
 * in order to retrieve a random set of records.
 * This returns a random value from 0 to 1.
 * 
 * Usage:
 * PREFIX inqle-fn: <java:org.inqle.data.rdf.jena.fn.>
 * SELECT ?whatever
 * { ?whatever ?p ?o .
 * LET (?rand := inqle-fn:RandomPerValue())}
 * ORDER BY DESC(?rand)
 * LIMIT 100
 * 
 * @author David Donohue
 * Oct 22, 2008
 */
public class RandomPerValue extends FunctionBase1 {
	
	private static Logger log = Logger.getLogger(RandomPerValue.class);
	
	@Override
	public NodeValue exec(NodeValue nodeValue) {
		double dbl = new Random(nodeValue.hashCode() + System.currentTimeMillis()).nextDouble();
		log.info("RandomPerValue(" + nodeValue + ")=" + dbl);
		NodeValue randomValue = NodeValue.makeDouble(dbl);
		return randomValue;
	}

}
