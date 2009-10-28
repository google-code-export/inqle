/**
 * 
 */
package org.inqle.ui.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.inqle.ui.dao.IAnswersDao;
import org.inqle.ui.dao.IOptionsDao;
import org.inqle.ui.dao.IQuestionsDao;
import org.inqle.ui.dao.MockAnswersDao;
import org.inqle.ui.dao.MockOptionsDao;
import org.inqle.ui.dao.MockQuestionDao;

import com.antilia.web.AntiliaWebApplication;
import com.google.inject.Binder;
import com.google.inject.Module;

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
		return Index.class;
	}
	
	@Override
	protected void init() {
		super.init();
		getMarkupSettings().setStripWicketTags(true);
		getDebugSettings().setAjaxDebugModeEnabled(false);
		getDebugSettings().setOutputMarkupContainerClassName(false);
		addComponentInstantiationListener(
				new GuiceComponentInjector(this, getModule()));
	}
	
	/**
	 * Configure GUICE.
	 * 
	 * @return
	 */
	protected Module getModule() {
		return new Module() {
			
			@Override
			public void configure(Binder binder) {
				binder.bind(IOptionsDao.class).toInstance(MockOptionsDao.getInstance());
				binder.bind(IAnswersDao.class).toInstance(MockAnswersDao.getInstance());				
				binder.bind(IQuestionsDao.class).toInstance(MockQuestionDao.getInstance());
			}
		};
	}

}
