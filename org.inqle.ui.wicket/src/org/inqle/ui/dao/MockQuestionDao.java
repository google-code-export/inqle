package org.inqle.ui.dao;

import java.util.ArrayList;
import java.util.List;

import org.inqle.ui.model.Question;

import com.antilia.common.dao.impl.ListQuerableUpdatableDao;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MockQuestionDao extends ListQuerableUpdatableDao<Question> implements IQuestionsDao {

	private static final long serialVersionUID = 1L;
	
	private static List<Question> questions = new ArrayList<Question>();
	
	private static final MockQuestionDao instance = new MockQuestionDao();
	
	/**
	 * @param list
	 */
	private MockQuestionDao() {
		super(questions);
	}

	public static MockQuestionDao getInstance() {
		return instance;
	}

}
