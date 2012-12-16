package org.inqle.ui.model;


/**
 * Answers do not have references to questions because 
 * the same answer can be applied to different 
 * questions.
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public abstract class BaseTranslatable implements ITranslatable {
	
	private static final long serialVersionUID = 1L;

	private String translationKey;
	
	public BaseTranslatable() {
	}


	public String getTranslationKey() {
		return translationKey;
	}

	public void setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
	}

}
