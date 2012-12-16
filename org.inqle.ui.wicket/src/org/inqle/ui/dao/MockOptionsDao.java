package org.inqle.ui.dao;

import java.util.ArrayList;
import java.util.List;

import org.inqle.ui.model.Option;

import com.antilia.common.dao.impl.ListQuerableUpdatableDao;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MockOptionsDao extends ListQuerableUpdatableDao<Option> implements IOptionsDao {

	private static final long serialVersionUID = 1L;
	
	private static List<Option> options = new ArrayList<Option>();
	
	private static final MockOptionsDao instance = new MockOptionsDao();
	
	/**
	 * @param list
	 */
	private MockOptionsDao() {
		super(options);
	}

	public static MockOptionsDao getInstance() {
		return instance;
	}

}
