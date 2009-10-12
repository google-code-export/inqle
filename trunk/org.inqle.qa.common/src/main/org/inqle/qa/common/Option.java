package org.inqle.qa.common;

import java.io.Serializable;
import java.util.Collection;

/**
 * Represents an option for questions and answers
 * @author David Donohue
 *
 */
public class Option extends QAModelObject implements IOption, Serializable {

	private Collection<ITranslation> translations;

	/**
	 * Private constructor.  Use QAFactory.newOption();
	 */
	Option(){};
	
	public void setOptionTranslations(Collection<ITranslation> translations) {
		this.translations = translations;
	}

	public Collection<ITranslation> getOptionTranslations() {
		return translations;
	}
	
	public void addOptionTranslation(ITranslation translation) {
		translations.add(translation);
	}
	
	public ITranslation getOptionTranslation(String lang) {
		for (ITranslation translation: getOptionTranslations()) {
			if (translation.getLang().equals(lang)) return translation;
		}
		return null;
	}
	public ITranslation getDefaultOptionTranslation() {
		return getOptionTranslation(QAInfo.DEFAULT_LANG);
	}
}
