package org.inqle.ui.rap.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Generate links for a list of RDF properties
 * @author David Donohue
 * May 23, 2009
 */
public class PropertiesTable extends Composite {

	private Map<String, Object> properties;
	private int _style;
	private Composite composite;
	private List<Label> labels;
	private List<Text> values;

	public PropertiesTable(Composite parent, int style) {
		super(parent, style);
		composite = this;
		composite.setLayout(new GridLayout(2, false));
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		composite.setLayoutData(gridData);
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
		refresh();
	}

	public void refresh() {
		labels = new ArrayList<Label>();
		values = new ArrayList<Text>();
		for (String propertyUri: properties.keySet()) {
			Label label = new Label(composite, getStyle());
			label.setText(propertyUri);
			labels.add(label);
			Text text = new Text(composite, getStyle());
			text.setText(String.valueOf(properties.get(propertyUri)));
			values.add(text);
		}
		
	}

	@Override
	public void dispose() {
		for (Label label: labels) {
			label.dispose();
		}
		for (Text text: values) {
			text.dispose();
		}
		labels = null;
		values = null;
		super.dispose();
	}
	
	public void clearData() {
		for (Label label: labels) {
			label.setText("");
		}
		for (Text text: values) {
			text.setText("");
		}
		labels = null;
		values = null;
	}

}
