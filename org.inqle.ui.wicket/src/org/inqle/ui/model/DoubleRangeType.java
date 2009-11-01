package org.inqle.ui.model;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class DoubleRangeType implements IAnswerType<DoubleRangeAnswer> {


	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.inqle.ui.model.IAnswerType#createNewInstance()
	 */
	@Override
	public DoubleRangeAnswer createNewInstance() {
		return new DoubleRangeAnswer();
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.model.IAnswerType#getTypeDescription()
	 */
	@Override
	public String getTypeDescription() {
		return "Double range";
	}
	
	@Override
	public String toString() {
		return getTypeDescription();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DoubleRangeType);
	}
}
