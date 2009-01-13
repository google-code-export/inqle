package org.inqle.ui.rap.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

/**
 * Renders a table of widgets.  To render a table, either the subclass or the calling
 * code must call renderTable();
 * @author David Donohue
 * Dec 30, 2008
 */
public abstract class AScrolledTable extends AScrolledWidget {

	/**
	 * This class holds the name of a column, and is attached to a link
	 * @author David Donohue
	 * Jan 12, 2009
	 */
	public class ColumnNameData {

		private String columnName;

		public ColumnNameData(String columnName) {
			this.columnName = columnName;
		}

		public String getColumnName() {
			return columnName;
		}
		
		@Override
		public String toString() {
			return columnName;
		}

	}

	protected static final String SELECT_COLUMN_NAME = "Select";
	
	protected List<String> columnNames = new ArrayList<String>();

	protected int selectionMode;

	protected boolean sortable = true;
	protected boolean addCheckBoxes = false;

	protected SelectionListener listener;
	
	private static Logger log = Logger.getLogger(AScrolledTable.class);
	
	protected AScrolledTable(Composite parent, int style) {
		super(parent, style);		
		if ((style & SWT.SINGLE) == SWT.SINGLE) {
			selectionMode = SWT.SINGLE;
		} else {
			selectionMode = SWT.MULTI;
		}
		
		if ((style & SWT.CHECK) == SWT.CHECK) {
			addCheckBoxes  = true;
		}
		
	}
	
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}
	
	protected List<String> getColumnNames() {
		return columnNames;
	}
	
	public void renderTable(SelectionListener listener) {
//		unscrolledTopComposite.setLayout(new GridLayout(1,true));
//		Label tableTitle = new Label(unscrolledTopComposite, SWT.NONE);
//		tableTitle.setText(getTableTitle());
		
		this.listener = listener;
//		log.info("Rendering table.  getColumnNames()=" + getColumnNames());
		int size = getColumnNames().size();
		if (addCheckBoxes) {
			size = size + 1;
		}
		GridLayout gl = new GridLayout(size, false);
		composite.setLayout(gl);
		
		fillColumnNames();
		
		fillTable();
		
		recomputeSize();
	}

	protected abstract void fillTable();

	protected void fillColumnNames() {
		List<String> colNames = getColumnNames();
		if (colNames == null || colNames.size()==0) return;
		Label label;
		if (addCheckBoxes) {
			label = new Label(composite, SWT.BOLD);
			label.setText(SELECT_COLUMN_NAME);
		}
//		label.setLayoutData(new GridData(GridData.FILL_BOTH));
		for (String columnName: colNames) {
			if (columnName==null) columnName = "";
			String displayName = columnName.replaceAll("_", " ");
			Link link = new Link(composite, SWT.NONE);
			link.setToolTipText("Click to sort");
			link.addSelectionListener(listener);
			if (sortable) {
				link.setText("<a>"+displayName+"</a>");
			} else {
				link.setText(displayName);
			}
			link.setData(new ColumnNameData(columnName));
//			link.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

//	public void clearTable() {
//		for (Control control: getChildren()) {
//			log.info("Disposing control: " + control);
//			control.dispose();
//		}
//	}
	
//	@Override
//	public void dispose() {
//		super.dispose();
//	}
}
