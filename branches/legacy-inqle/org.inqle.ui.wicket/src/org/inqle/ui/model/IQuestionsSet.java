package org.inqle.ui.model;

import java.io.Serializable;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IQuestionsSet extends Serializable {

	public boolean  hasNext();
	
	public Question  next();
	
	public boolean  hasPrevious();
	
	public Question  previous();
}
