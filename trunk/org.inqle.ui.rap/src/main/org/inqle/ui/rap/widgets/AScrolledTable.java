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

	protected static final String SELECT_COLUMN_NAME = "Select";
	
	protected List<String> columnNames = new ArrayList<String>();

	protected int selectionMode;

	private static Logger log = Logger.getLogger(AScrolledTable.class);
	
	protected AScrolledTable(Composite parent, int style) {
		super(parent, style);		
		if ((style & SWT.SINGLE) == SWT.SINGLE) {
			selectionMode = SWT.SINGLE;
		} else {
			selectionMode = SWT.MULTI;
		}
	}
	
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}
	
	protected List<String> getColumnNames() {
		return columnNames;
	}
	
	public void renderTable(SelectionListener listener) {
//		log.info("Rendering table.  getColumnNames()=" + getColumnNames());
		GridLayout gl = new GridLayout(getColumnNames().size() + 1, false);
		composite.setLayout(gl);
		
		fillColumnNames(listener);
		
		fillTable();
		
		recomputeSize();
	}

	protected abstract void fillTable();

	protected void fillColumnNames(SelectionListener listener) {
		List<String> colNames = getColumnNames();
		if (colNames == null || colNames.size()==0) return;
		Label label = new Label(composite, SWT.BOLD);
		label.setText(SELECT_COLUMN_NAME);
//		label.setLayoutData(new GridData(GridData.FILL_BOTH));
		for (String columnName: colNames) {
			if (columnName==null) columnName = "";
			String displayName = columnName.replaceAll("_", " ");
			Link link = new Link(composite, SWT.NONE);
			link.setToolTipText("Click to sort");
			link.addSelectionListener(listener);
			link.setText("<a>"+displayName+"</a>");
			link.setData(columnName);
//			link.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
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