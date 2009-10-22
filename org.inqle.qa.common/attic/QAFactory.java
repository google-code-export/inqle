package org.inqle.qa.common;

public class QAFactory {

	public static IQuestion newQuestion() {
		return new Question();
	}
	
	public static IAnswer newAnswer() {
		return new Answer();
	}
	
	public static ITranslation newTranslation() {
		return new Translation();
	}
	
	public static IOption newOption() {
		return new Option();
	}
}
