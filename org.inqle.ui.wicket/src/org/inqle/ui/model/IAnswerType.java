package org.inqle.ui.model;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IAnswerType<T extends IAnswer> {
	
	String getTypeDescription();
	
	T createNewInstance();
	
}
