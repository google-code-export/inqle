/**
 * 
 */
package org.inqle.data.rdf.jena.fn;

import java.util.Random;

import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase0;

/**
 * This is designed to be used in a SPARQL query,
 * in order to retrieve a random set of records.
 * This returns a random value from 0 to 1.
 * 
 * Usage:
 * PREFIX inqle-fn: <java:org.inqle.data.rdf.jena.fn.>
 * SELECT ?whatever
 * { ?whatever ?p ?o .
 * LET (?rand := inqle-fn:Rand())}
 * ORDER BY DESC(?rand)
 * LIMIT 100
 * 
 * @author David Donohue
 * Oct 22, 2008
 */
public class Rand extends FunctionBase0 {
	
	@Override
	public NodeValue exec() {
		double dbl = new Random().nextDouble();
		NodeValue randomValue = NodeValue.makeDouble(dbl);
		return randomValue;
	}

}
