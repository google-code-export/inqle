/**
 * 
 */
package org.inqle.ui.dao;

import java.io.Serializable;
import java.util.List;

import com.antilia.common.query.IQuery;

/**
 * 
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public interface IDao<E extends Serializable> extends Serializable {

	/**
	 * 
	 * @param option
	 * @return
	 */
	public E persist(E bean);
	
	
	/**
	 * 
	 * @param option
	 * @return
	 */
	public E update(E bean);
	
	
	/**
	 * 
	 * @param option
	 * @return
	 */
	public E delete(E option);
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	public List<E> getAllMatching(IQuery<E> query);
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	public Long countAllMatching(IQuery<E> query);
}
