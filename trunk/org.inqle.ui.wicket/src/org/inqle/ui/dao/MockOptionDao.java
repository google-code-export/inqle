/**
 * 
 */
package org.inqle.ui.dao;

import java.util.List;

import org.inqle.ui.model.Option;

import com.antilia.common.query.IQuery;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MockOptionDao implements IOptionsDao {

	private static final long serialVersionUID = 1L;


	/**
	 * 
	 */
	public MockOptionDao() {
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.dao.IOptionsDao#countOptionsMatching(com.antilia.common.query.IQuery)
	 */
	public Long countAllMatching(IQuery<Option> query) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.dao.IOptionsDao#deleteOption(org.inqle.ui.model.Option)
	 */
	public Option delete(Option option) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.dao.IOptionsDao#getOptionsMatching(com.antilia.common.query.IQuery)
	 */
	public List<Option> getAllMatching(IQuery<Option> query) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.dao.IOptionsDao#persistOption(org.inqle.ui.model.Option)
	 */
	public Option persist(Option option) {
		return option;
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.inqle.ui.dao.IOptionsDao#updateOption(org.inqle.ui.model.Option)
	 */
	public Option update(Option option) {
		return option;
	}
}
