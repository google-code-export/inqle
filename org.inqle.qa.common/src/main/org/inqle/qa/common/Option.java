package org.inqle.qa.common;

import java.io.Serializable;
import java.util.Collection;

import org.inqle.core.data.GlobalModelObject;

/**
 * Represents an option for questions and answers
 * @author David Donohue
 *
 */
public class Option extends GlobalModelObject implements IOption, Serializable {

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

	@Override
	/**
	 * So any 2 option objects with the same English text will have the same ID.
	 * Any difference in characters will cause the IDs to be different
	 */
	public String getDefiningStringRepresentation() {
		return getClass().getName() + "_" + getDefaultOptionTranslation();
	}
}
