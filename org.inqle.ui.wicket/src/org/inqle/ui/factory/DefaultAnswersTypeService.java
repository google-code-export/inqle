/**
 * 
 */
package org.inqle.ui.factory;

import java.util.ArrayList;
import java.util.List;

import org.inqle.ui.model.IAnswerType;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class DefaultAnswersTypeService implements IAnswerTypesService {

	private static final long serialVersionUID = 1L;

	private List<IAnswerType<?>> types = new ArrayList<IAnswerType<?>>();
	
	private static final DefaultAnswersTypeService instance = new DefaultAnswersTypeService();
	
	public static DefaultAnswersTypeService getInstance() {
		return instance;
	}


	/**
	 * 
	 */
	private DefaultAnswersTypeService() {
	}
	
	
	@Override
	public void deregisterType(IAnswerType<?> answerType) {
		types.remove(answerType);
	}
	
	@Override
	public void registerType(IAnswerType<?> answerType) {
		types.add(answerType);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.factory.IAnswerTypesService#getAnswerTypes()
	 */
	@Override
	public Iterable<IAnswerType<?>> getAnswerTypes() {
		return types;
	}

}
