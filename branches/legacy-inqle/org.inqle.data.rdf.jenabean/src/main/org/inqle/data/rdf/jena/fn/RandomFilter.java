/**
 * 
 */
package org.inqle.data.rdf.jena.fn;

import java.util.Random;

import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase1;

/**
 * This is designed to be used in a SPARQL query,
 * in order to retrieve a random set of records.
 * The argument for this function should be a double,
 * e.g. an argument of 0.01 would retrieve on average 1 in 100 records.
 * 
 * Usage:
 * PREFIX inqle-fn: <java:org.inqle.data.rdf.jena.fn.>
 * SELECT ?whatever
 * { ?whatever ?p ?o .
 * FILTER (inqle-fn:RandomFilter(0.01)) }
 * 
 * @author David Donohue
 * Oct 22, 2008
 */
public class RandomFilter extends FunctionBase1 {
	
	@Override
	public NodeValue exec(NodeValue nodeValue) {
		double fractionToKeep = nodeValue.getDouble();
		double randomNumber = new Random().nextDouble();
		if (randomNumber < fractionToKeep) {
			return NodeValue.TRUE;
		}
		return NodeValue.FALSE;
	}

}
