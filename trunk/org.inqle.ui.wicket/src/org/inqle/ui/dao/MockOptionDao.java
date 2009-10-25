/**
 * 
 */
package org.inqle.ui.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.inqle.ui.model.Option;

import com.antilia.common.query.IQuery;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MockOptionDao implements IOptionsDao {

	private static final long serialVersionUID = 1L;

	@Override
	public Option add(Option element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAll(Collection<Option> element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Option remove(Option element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeAll(Collection<Option> element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Option element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAll(Collection<Option> element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long count(IQuery<Option> query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Option> findAll(IQuery<Option> query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Option> findAll(Class<Option> beanClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Option> findByExample(Option bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Option findById(Class<Option> beanClass, Serializable key) {
		// TODO Auto-generated method stub
		return null;
	}

}
