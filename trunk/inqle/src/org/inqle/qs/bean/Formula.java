package org.inqle.qs.bean;

import static org.inqle.qs.Constants.NS_A;

import java.util.Collection;

import org.openrdf.annotations.Iri;

/**
 * Formulas are used to derive current data about the user.
 * @author David Donohue
 *
 */
@Iri(NS_A)
public class Formula {
	
	/**
	 * The variables that appear in the right-hand side of the formula
	 */
	private Collection<String> inputInfos;
	
	/**
	 * The variable that appears in the left-hand side of the formula
	 */
	private String outputInfo;
	
	private String formula;
}
