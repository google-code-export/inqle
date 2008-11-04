package org.inqle.ui.rap.pages;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.inqle.ui.rap.IList2Provider;
import org.inqle.ui.rap.IListProvider;
import org.inqle.ui.rap.IValueUpdater;

/**
 * This generates a wizard page which has a list selector, permitting selection
 * of a single item in the list.
 * It requires the containing wizard to implement the interface 
 * IListProvider.
 * If the containing wizard implements IValueUpdater, this notifies the wizard
 * upon change of the selection, so that the wizard may update the 
 * appropriate bean/value.
 * 
 * @author David Donohue
 * Oct. 28, 2008
 */
public class SimpleListSelectorPage extends DynaWizardPage implements ISelectionChangedListener {

	protected ListViewer listViewer;
	private int singleOrMulti;
	private static final Logger log = Logger.getLogger(SimpleListSelectorPage.class);
	
	/**
	 * 
	 * @param title
	 * @param description
	 * @param labelText
	 * @param singleOrMulti use SWT.SINGLE to support selection of a single item, and SWT.MULTI
	 * to support more than one.
	 */
	public SimpleListSelectorPage(String title, String description, String labelText, int singleOrMulti) {
		super(title, null);
		setMessage(description);
		this.labelText = labelText;
		this.singleOrMulti = singleOrMulti;
	}

	/**
	 * Add form elements.
	 */
	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(1, false);
		selfComposite.setLayout(gl);
		
		createList();
	}

	protected void createList() {
		new Label (selfComposite, SWT.NONE).setText(labelText);	
		listViewer = new ListViewer(selfComposite, SWT.V_SCROLL | SWT.BORDER | singleOrMulti);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		listViewer.getList().setLayoutData(gridData);
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.addSelectionChangedListener(this);
		//listViewer.setContentProvider(new ObservableListContentProvider());
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		updateList();
	}

	/**
	 * Update the items in the list
	 * If the containing wizard implements IListProvider, then use this to populate the list
	 * If the containing wizard implements IList2Provider, then use this to pre-check items in the list
	 */
	private void updateList() {
		listViewer.getList().removeAll();
		if (getWizard() instanceof IListProvider) {
			IListProvider provider = (IListProvider)getWizard();
			List<Object> items = provider.getList(this);
			if (items==null || items.size()==0) {
				return;
			}
			Object[] nullArray = {};
			Object[] itemArray = items.toArray(nullArray);
			listViewer.add(itemArray);
			if (getWizard() instanceof IList2Provider) {
				IList2Provider provider2 = (IList2Provider)getWizard();
				List<Object> checkedItems = provider2.getList2(this);
				if (checkedItems==null) return;
				String[] nullStringArray = {};
				log.info("CCCCCCCCCCCCCCCCC Checking these items:" + checkedItems);
				String[] checkedArray = checkedItems.toArray(nullStringArray);
				listViewer.getList().setSelection(checkedArray);
			}
		}
	}

	public String getSelectedString() {
		return listViewer.getList().getItem(getSelectedIndex());
	}
	
	public String[] getSelectedStrings() {
		return listViewer.getList().getSelection();
	}
	
	public int getSelectedIndex() {
		return listViewer.getList().getSelectionIndex();
	}
	
	public int[] getSelectedIndexes() {
		return listViewer.getList().getSelectionIndices();
	}

	public void selectionChanged(SelectionChangedEvent arg0) {
		if (getWizard() instanceof IValueUpdater) {
			((IValueUpdater)getWizard()).updateValue(this);
		}
	}
}
