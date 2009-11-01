package org.inqle.ui.model;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class SingleChoiceType implements IAnswerType<SingleChoiceAnswer> {


	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.inqle.ui.model.IAnswerType#createNewInstance()
	 */
	@Override
	public SingleChoiceAnswer createNewInstance() {
		return new SingleChoiceAnswer();
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.model.IAnswerType#getTypeDescription()
	 */
	@Override
	public String getTypeDescription() {
		return "Single choice answer";
	}

	@Override
	public String toString() {
		return getTypeDescription();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof SingleChoiceType);
	}
}
