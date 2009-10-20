package org.inqle.ui.model;


/**
 * Option is considered as a kind of answer!
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class Option extends IdentifiableTranslatable implements IUIRenderable {
	
	private static final long serialVersionUID = 1L;
		
	public Option() {		
	}
	
	public Option(String text) {
		setTranslationKey(text);
	}
		
}
