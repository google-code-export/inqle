/**
 * 
 */
package org.inqle.ui.model;

import com.antilia.common.util.StringUtils;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class GoogleTranslationService implements ITranslationService {
	
	private Language from = Language.ENGLISH;
	
	private Language to = Language.SPANISH;
	
	private static final int NO_PORT = -1;
	
	/**
	 * 
	 */
	public GoogleTranslationService() {
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.model.ITranslationService#translate(org.inqle.ui.model.ITranslatable)
	 */
	@Override
	public String translate(ITranslatable translatable) {
		String key = translatable.getTranslationKey();
		if(!StringUtils.isEmpty(key)) {
			try {
				configureProxy();
				return Translate.execute(key, Language.ENGLISH, Language.SPANISH);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return key;
	}
	
	private void configureProxy() {
		if(getProxyHost() != null) {
			System.setProperty("http.proxyHost", getProxyHost());
		}
		if(getProxyPort() > 0) {
			System.setProperty("http.proxyPort", Integer.toString(getProxyPort()));
		}
	}
	
	protected String getProxyHost() {
		return null;
	}
	
	protected int getProxyPort() {
		return NO_PORT;
	}

	public Language getFrom() {
		return from;
	}

	public void setFrom(Language from) {
		this.from = from;
	}

	public Language getTo() {
		return to;
	}

	public void setTo(Language to) {
		this.to = to;
	}

}
