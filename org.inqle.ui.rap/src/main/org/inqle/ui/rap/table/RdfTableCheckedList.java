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
import org.inqle.data.rdf.jena.util.QuerySolutionValueExtractor;
import org.inqle.data.rdf.jenabean.JenabeanConverter;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * contains the List of objects which are checked in a CheckboxTableViewer, which
 * is backed by an RdfTable of (QuerySolution) rows
 * @author David Donohue
 * Mar 12, 2008
 */
public class RdfTableCheckedList extends WritableList implements IChangeListener, ICheckStateListener {
	private CheckboxTableViewer checkboxTableViewer;

	private String idField = "id";
	
	private static final Logger log = Logger.getLogger(RdfTableCheckedList.class);
	
	/**
	 * Constructor, uses default name of the idfield
	 * @param checkboxTableViewer
	 */
	public RdfTableCheckedList(CheckboxTableViewer checkboxTableViewer) {
		super();
		this.checkboxTableViewer = checkboxTableViewer;
		checkboxTableViewer.addCheckStateListener(this);
		addChangeListener(this);
	}
	
	public RdfTableCheckedList(CheckboxTableViewer checkboxTableViewer, String idField) {
		super();
		this.idField = idField;
		this.checkboxTableViewer = checkboxTableViewer;
		checkboxTableViewer.addCheckStateListener(this);
		addChangeListener(this);
	}
	
	/**
	 * Checks boxes upon initial loading
	 */
	public void handleChange(ChangeEvent event) {
		String[] nullStringArray = new String[] {};
		List<String> beanItems = new ArrayList<String>();
		try {
			beanItems = Arrays.asList((String[])this.toArray(nullStringArray));
		} catch (Exception e) {
			log.error("Unable to convert selections to a String[]", e);
		}
		int index = 0;
		Object rowObject = checkboxTableViewer.getElementAt(index);
		while(rowObject != null) {
			QuerySolution querySolution = (QuerySolution)rowObject;
			String rowId = QuerySolutionValueExtractor.getDisplayable(querySolution, idField);
			//if (Arrays.binarySearch(beanItems, id) >= 0) {
			if (beanItems.contains(rowId)) {
				checkboxTableViewer.setChecked(rowObject, true);
			}
			index++;
			rowObject = checkboxTableViewer.getElementAt(index);
		}
		checkboxTableViewer.refresh();
	}
	
	public void checkStateChanged(CheckStateChangedEvent event) {
		QuerySolution[] nullQuerySolutionArray = new QuerySolution[]{};
		List<String> checkedIds = new ArrayList<String>();
		for (Object rowObj: checkboxTableViewer.getCheckedElements()) {
			QuerySolution querySolution = (QuerySolution)rowObj;
			checkedIds.add(QuerySolutionValueExtractor.getDisplayable(querySolution, idField));
		}
		//Arrays.asList(checkboxTableViewer.getCheckedElements(), nullQuerySolutionArray);
		//List<String> checkedIds = QuerySolutionValueExtractor.getValues(checkedRows, idField);
		this.updateWrappedList(checkedIds);
	}
}
