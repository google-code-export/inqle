package org.inqle.qa.common.beans;

import java.io.Serializable;
import java.util.Collection;

import org.inqle.core.data.GlobalModelObject;

/**
 * Represents an option for questions and answers
 * @author David Donohue
 *
 */
public class Option extends GlobalModelObject implements Serializable {

	private Collection<Translation> translations;
	
	public void setOptionTranslations(Collection<Translation> translations) {
		this.translations = translations;
	}

	public Collection<Translation> getOptionTranslations() {
		return translations;
	}
	
	public void addOptionTranslation(Translation translation) {
		translations.add(translation);
	}
	
	public Translation getOptionTranslation(String lang) {
		for (Translation translation: getOptionTranslations()) {
			if (translation.getLang().equals(lang)) return translation;
		}
		return null;
	}
	public Translation getDefaultOptionTranslation() {
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
