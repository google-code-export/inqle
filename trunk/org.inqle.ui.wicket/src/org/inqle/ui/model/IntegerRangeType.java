package org.inqle.ui.model;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class IntegerRangeType implements IAnswerType<IntegerRangeAnswer> {


	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.inqle.ui.model.IAnswerType#createNewInstance()
	 */
	@Override
	public IntegerRangeAnswer createNewInstance() {
		return new IntegerRangeAnswer();
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.model.IAnswerType#getTypeDescription()
	 */
	@Override
	public String getTypeDescription() {
		return "Integers range";
	}

}
