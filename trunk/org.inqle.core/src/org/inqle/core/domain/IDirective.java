/**
 * 
 */
package org.inqle.core.domain;

/**
 * Directives contain information from the application & the user
 * which might influence a decision.  The goal of a directive is the 
 * fully-qualified identifier of the instance to be returned.
 * 
 * Some implementations should query a database of annotations, 
 * and include pertinent annotations related to the goal.
 * @author David Donohue
 * Nov 28, 2007
 */
public interface IDirective {

	public String getGoal();
	
	public void setGoal(String goal);
}
