package org.inqle.ui.factory;

import java.io.Serializable;

import org.inqle.ui.model.IAnswerType;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IAnswerTypesService extends Serializable {

	
	Iterable<IAnswerType<?>> getAnswerTypes();
	
	public void registerType(IAnswerType<?> answerType);
	
	public void deregisterType(IAnswerType<?> answerType);
	
}
