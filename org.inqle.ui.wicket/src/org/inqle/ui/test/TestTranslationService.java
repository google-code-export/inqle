/**
 * 
 */
package org.inqle.ui.test;

import org.inqle.ui.model.GoogleTranslationService;
import org.inqle.ui.model.Question;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class TestTranslationService {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		GoogleTranslationService translationService = new GoogleTranslationService() {
			 
			@Override
			protected String getProxyHost() {
				return "bcnisa1";
			}
			
			@Override
			protected int getProxyPort() {
				return 8080;
			}
		};
		*/
		GoogleTranslationService translationService = new GoogleTranslationService();
		Question question = new Question();
		question.setTranslationKey("Hello World!");
		System.out.println(translationService.translate(question));
	}

}
