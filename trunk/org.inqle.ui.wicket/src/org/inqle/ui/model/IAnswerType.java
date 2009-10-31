package org.inqle.ui.model;

import java.io.Serializable;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IAnswerType<T extends IAnswer> extends Serializable {
	
	String getTypeDescription();
	
	T createNewInstance();
	
}
