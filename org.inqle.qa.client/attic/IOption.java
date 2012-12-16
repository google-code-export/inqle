package org.inqle.qa.common;

import java.util.Collection;

public interface IOption {

	public void setOptionTranslations(Collection<ITranslation> optionTranslations);

	public Collection<ITranslation> getOptionTranslations();
	
	public void addOptionTranslation(ITranslation translation);
	
	public ITranslation getOptionTranslation(String lang);
	
	public ITranslation getDefaultOptionTranslation();
}
