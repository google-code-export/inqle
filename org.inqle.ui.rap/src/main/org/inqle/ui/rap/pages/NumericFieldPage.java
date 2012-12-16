/**
 * 
 */
package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Generates a page with a single numeric field.
 * @author David Donohue
 * May 16, 2008
 */
public class NumericFieldPage extends DynaWizardPage {
	
	private Text numericField;

	private String numberStr;

	private static Logger log = Logger.getLogger(NumericFieldPage.class);
	
	public NumericFieldPage(String title, String labelText, ImageDescriptor titleImage) {
		super(title, titleImage);
		this.labelText = labelText;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.pages.DynaWizardPage#addElements(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(2, false);
		selfComposite.setLayout(gl);
		
		Composite composite = selfComposite;
		new Label (composite, SWT.NONE).setText(labelText);	
		numericField = new Text(composite, SWT.BORDER);
		if (numberStr != null) {
			numericField.setText(numberStr);
		}
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		numericField.setLayoutData(gridData);
	}
	
	public Double getDoubleValue() {
		double dblVal = 0;
		try {
			dblVal = Double.parseDouble(numericField.getText());
		} catch (NumberFormatException e) {
			log.info("Unable to convert text to a numeric double value.  Returning null");
			return null;
		}
		return new Double(dblVal);
	}
	
	public Integer getIntegerValue() {
		int intVal = 0;
		try {
			intVal = Integer.parseInt(numericField.getText());
		} catch (NumberFormatException e) {
			log.info("Unable to convert text to a numeric integer value.  Returning null");
			return null;
		}
		return new Integer(intVal);
	}
	
	public void setDoubleValue(double val) {
		numberStr = String.valueOf(val);
		if (numericField != null) {
			numericField.setText(numberStr);
		}
	}
	
	public void setIntegerValue(int val) {
		numberStr = String.valueOf(val);
		if (numericField != null) {
			numericField.setText(numberStr);
		}
	}
}
