/**
 * 
 */
package org.inqle.ui.wicket;

import org.apache.wicket.Page;

import com.antilia.web.AntiliaWebApplication;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class InqleApplication extends AntiliaWebApplication {

	/**
	 * 
	 */
	public InqleApplication() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		// TODO Auto-generated method stub
		return Index.class;
	}

}
