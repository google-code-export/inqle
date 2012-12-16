package org.inqle.data.sampling.rap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.ui.rap.pages.DynaWizardPage;

public class MinMaxPage extends DynaWizardPage {

	private Text minText;
	private Text maxText;
	private String minLabel = "Minimum Value";
	private String maxLabel = "Maximum Value";
	private int startingMinVal;
	private int startingMaxVal;

	public MinMaxPage(String pageName, String pageDescription, int startingMinVal, int startingMaxVal) {
		super(pageName, null);
		setMessage(pageDescription);
		this.startingMinVal = startingMinVal;
		this.startingMaxVal = startingMaxVal;
	}
	
	public void createControl(Composite parent) {
		GridData gridData;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout (new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		
		new Label(composite, SWT.NONE).setText(getMinLabel());
		minText = new Text(composite, SWT.BORDER);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		minText.setLayoutData(gridData);
		minText.setText(String.valueOf(startingMinVal));
		
		new Label(composite, SWT.NONE).setText(getMaxLabel());
		maxText = new Text(composite, SWT.BORDER);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		maxText.setLayoutData(gridData);
		maxText.setText(String.valueOf(startingMaxVal));
		
		minText.forceFocus();
		
		setControl(composite);

	}
	
	private String getMaxLabel() {
		return maxLabel;
	}

	private String getMinLabel() {
		return minLabel;
	}

	@Override
	public boolean onNextPage() {
		if (getMinVal() == null || getMaxVal() == null) {
			setMessage("Please both a minimum and maximum value.");
			return false;
		}
		if (getMinVal() > getMaxVal()) {
			setMessage("Please set the maximum value equal to or greater than the minimum value.");
			return false;
		}
		return true;
	}

	public Integer getMinVal() {
		if (minText.getText()==null) return null;
		Integer intObj = null;
		try {
			int intVal = Integer.parseInt(minText.getText());
			intObj = new Integer(intVal);
		} catch (Exception e) {
			return null;
		}
		return intObj;
	}

	public Integer getMaxVal() {
		if (maxText.getText()==null) return null;
		Integer intObj = null;
		try {
			int intVal = Integer.parseInt(maxText.getText());
			intObj = new Integer(intVal);
		} catch (Exception e) {
			return null;
		}
		return intObj;
	}

	@Override
	public void addElements() {	
	}

	public void setMinLabel(String minLabel) {
		this.minLabel = minLabel;
	}

	public void setMaxLabel(String maxLabel) {
		this.maxLabel = maxLabel;
	}
}