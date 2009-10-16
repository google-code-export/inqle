package org.inqle.ui.model;


/**
 * A textual Answer!
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class TextualAnswer extends IdentifiableTranslatable implements IAnswer {
	
	private static final long serialVersionUID = 1L;
		
	public TextualAnswer(String text) {
		setTranslationKey(text);
	}
		
}
