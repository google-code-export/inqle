/**
 * 
 */
package org.inqle.ui.dao;

import java.util.List;

import org.inqle.ui.model.Option;

import com.antilia.common.query.IQuery;

/**
 * 
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public interface IOptionsDao {

	/**
	 * 
	 * @param option
	 * @return
	 */
	public Option persistOption(Option option);
	
	/**
	 * 
	 * @param option
	 * @return
	 */
	public Option deleteOption(Option option);
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	public List<Option> getOptionsMatching(IQuery<Option> query);
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	public Long countOptionsMatching(IQuery<Option> query);
}
