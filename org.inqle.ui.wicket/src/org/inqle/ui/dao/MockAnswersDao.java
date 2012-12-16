package org.inqle.ui.dao;

import java.util.ArrayList;
import java.util.List;

import org.inqle.ui.model.IAnswer;

import com.antilia.common.dao.impl.ListQuerableUpdatableDao;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MockAnswersDao extends ListQuerableUpdatableDao<IAnswer> implements IAnswersDao {

	private static final long serialVersionUID = 1L;
	
	private static List<IAnswer> options = new ArrayList<IAnswer>();
	
	private static final MockAnswersDao instance = new MockAnswersDao();
	
	/**
	 * @param list
	 */
	private MockAnswersDao() {
		super(options);
	}

	public static MockAnswersDao getInstance() {
		return instance;
	}

}
