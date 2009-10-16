package org.inqle.ui.model;


/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class RangeAnswer<T extends Number> extends IdentifiableTranslatable implements IAnswer {
	
	private static final long serialVersionUID = 1L;
	
	private T minimumResponse;
	
	private T maximumResponse;
	
	private T selectedVaue;
	
	/**
	 * 
	 */
	public RangeAnswer() {
	}

	public T getMinimumResponse() {
		return minimumResponse;
	}

	public void setMinimumResponse(T minimumResponse) {
		this.minimumResponse = minimumResponse;
	}

	public T getMaximumResponse() {
		return maximumResponse;
	}

	public void setMaximumResponse(T maximumResponse) {
		this.maximumResponse = maximumResponse;
	}

	public T getSelectedVaue() {
		return selectedVaue;
	}

	public void setSelectedVaue(T selectedVaue) {
		this.selectedVaue = selectedVaue;
	}
}
