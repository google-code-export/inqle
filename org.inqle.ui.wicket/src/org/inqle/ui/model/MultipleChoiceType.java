package org.inqle.ui.model;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MultipleChoiceType implements IAnswerType<MultipleChoiceAnswer> {

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.inqle.ui.model.IAnswerType#createNewInstance()
	 */
	@Override
	public MultipleChoiceAnswer createNewInstance() {
		return new MultipleChoiceAnswer();
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.model.IAnswerType#getTypeDescription()
	 */
	@Override
	public String getTypeDescription() {
		return "Multiple choice answer";
	}
	
	@Override
	public String toString() {
		return getTypeDescription();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof MultipleChoiceType);
	}
}
