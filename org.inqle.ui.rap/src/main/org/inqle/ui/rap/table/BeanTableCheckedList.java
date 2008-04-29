/**
 * 
 */
package org.inqle.ui.rap.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.inqle.data.rdf.jenabean.JenabeanConverter;

/**
 * contains the List of objects which are checked in a CheckboxTableViewer, which
 * is backed by a List of (Jenabean) rows
 * @author David Donohue
 * Mar 12, 2008
 */
public class BeanTableCheckedList extends WritableList implements IChangeListener, ICheckStateListener {
	private CheckboxTableViewer checkboxTableViewer;
	
	private static final Logger log = Logger.getLogger(BeanTableCheckedList.class);
	
	public BeanTableCheckedList(CheckboxTableViewer checkboxTableViewer) {
		super();
		this.checkboxTableViewer = checkboxTableViewer;
		//checkInitialItems(initialCheckedItems);
		checkboxTableViewer.addCheckStateListener(this);
		
		addChangeListener(this);
	}
	
	private void checkInitialItems(List<?> initialCheckedItems) {
		int index = 0;
		Object rowBean = checkboxTableViewer.getElementAt(index);
		while(rowBean != null) {
			String id = JenabeanConverter.getId(rowBean);
			log.info("rowBean of class " + rowBean.getClass() + " has id= " + id);
			
			//if (Arrays.binarySearch(beanItems, id) >= 0) {
			if (initialCheckedItems.contains(id)) {
				checkboxTableViewer.setChecked(rowBean, true);
			}
			index++;
			rowBean = checkboxTableViewer.getElementAt(index);
		}
		checkboxTableViewer.refresh();
		
	}

	/**
	 * Checks boxes upon initial loading
	 */
	public void handleChange(ChangeEvent event) {
		log.info("Received Change event on model bean: " + event);
		String[] nullStringArray = new String[] {};
		List<String> beanItems = new ArrayList<String>();
		try {
			beanItems = Arrays.asList((String[])this.toArray(nullStringArray));
		} catch (Exception e) {
			log.error("Unable to convert selections to a String[]", e);
		}
		int index = 0;
		log.info("checking these items:" + beanItems + "...");
		Object rowBean = checkboxTableViewer.getElementAt(index);
		while(rowBean != null) {
			log.info("rowBean of class " + rowBean.getClass() + " has id= " + JenabeanConverter.getId(rowBean));
			String id = JenabeanConverter.getId(rowBean);
			//if (Arrays.binarySearch(beanItems, id) >= 0) {
			if (beanItems.contains(id)) {
				checkboxTableViewer.setChecked(rowBean, true);
			}
			index++;
			rowBean = checkboxTableViewer.getElementAt(index);
		}
		checkboxTableViewer.refresh();
	}
	
	public void checkStateChanged(CheckStateChangedEvent event) {
		List<String> checkedIds = JenabeanConverter.getIds(Arrays.asList(checkboxTableViewer.getCheckedElements()));
		this.updateWrappedList(checkedIds);
	}
	
}
