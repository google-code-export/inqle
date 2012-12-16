/**
 * 
 */
package org.inqle.ui.rap.table;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

/**
 * Sets the values in a Table, using a Javabean as the element (model).
 * To use this, set the column names using the <code>setColumnFields</code> 
 * method.  The field names in this list should correspond to Javabean methods
 * 
 * @author David Donohue
 * Mar 11, 2008
 */
public class BeanTableLabelProvider extends CellLabelProvider {

	protected List<String> columnFields = new ArrayList<String>();
	
	private static final Logger log = Logger.getLogger(BeanTableLabelProvider.class);
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		
		Object bean = cell.getElement();
		String fieldName = columnFields.get(cell.getColumnIndex());
		String cellValue = getStringValue(bean, fieldName);
		log.debug("Update cell " + bean + ":" + fieldName + "=" + cellValue);
		cell.setText(cellValue);
	}

	public void setColumnFields(List<String> columnFields) {
		this.columnFields = columnFields;
	}

	/**
	 * Uses Java reflection to retrieve a simple value from a Javabean.
	 * Assumes the Javabean method naming convention
	 * @param bean
	 * @param fieldName
	 * @return
	 */
	private static String getStringValue(Object bean, String fieldName) {
		String prop = Character.toUpperCase(fieldName.charAt(0)) +
		fieldName.substring(1);
		String mname = "get" + prop;
		Class<?>[] types = new Class[] {};
		Method method;
		try {
			method = bean.getClass().getMethod(mname, types);
			Object result = method.invoke(bean, new Object[0]);
			return result.toString();
		} catch (Exception e) {
			log.error("Unable to invoke method " + mname + " on bean " + bean);
		}
		return "";
	}
}
