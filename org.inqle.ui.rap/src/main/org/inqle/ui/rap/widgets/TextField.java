package org.inqle.ui.rap.widgets;

import org.apache.log4j.Logger;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class TextField extends Composite {

	private Text text;
	private Text descriptionText;
	private Label label;
	
	private static final Logger log = Logger.getLogger(TextField.class);
	
	public TextField(Composite parent, String labelString, String descriptionString) {
		this(parent, labelString, descriptionString, 0, null, SWT.NONE, SWT.BORDER, null);
	}
	
	public TextField(Composite parent, String labelString, String descriptionString, int spacerSize, String toolTipString, int compositeStyle, int textStyle, Layout formLayout) {
		super(parent, compositeStyle);
		//Composite composite = new Composite(parent, style);
		Composite composite = this;
		if (formLayout == null) {
			if (spacerSize > 0) {
				formLayout = new GridLayout(3, false);
			} else {
				formLayout = new GridLayout(2, false);
			}
		}
		composite.setLayout(formLayout);
		GridData gridData;
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//		composite.setLayoutData(gridData);
		
		//create the controls
		label = new Label(composite, SWT.NONE);
//		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
//		label.setLayoutData(gridData);
		//add left padding, if desired (right padding gets removed)
//		int currentLabelSize = labelString.length();
//		if (labelSize > 0 && labelSize > currentLabelSize) {
//			int paddingSize = labelSize - currentLabelSize;
//			String labelPadding = "";
//			for (int i=0; i<paddingSize; i++) {
//				labelPadding += " ";
//			}
//			labelString = labelPadding + labelString;
//		}
		label.setText(labelString);

		if (spacerSize > 0) {
			Label spacer = new Label(composite, SWT.NONE);
			String spacerString = "";
			for (int i=0; i<spacerSize; i++) {
				spacerString += ".";
			}
			spacer.setText(spacerString);
			spacer.setVisible(false);
		}
		
//		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		text = new Text(composite, textStyle);
		text.setLayoutData(gridData);
		if (toolTipString != null) {
			text.setToolTipText(toolTipString);
		}
		
		if (descriptionString != null && descriptionString.length() > 0) {
			//in next row, skip the label column
			new Label(composite, SWT.NONE);
			//...and skip the spacer column, if present
			if (spacerSize > 0) {
				new Label(composite, SWT.NONE);
			}
			
			//add the description
			descriptionText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
			descriptionText.setText(descriptionString);
			Font currentFont = descriptionText.getFont();
			String fontName = null;
			int fontHeight = 12;
			FontData[] fontData = currentFont.getFontData();
				
			for (FontData fontDataItem: fontData) {
				if (fontDataItem.getName() != null) {
					fontName = fontDataItem.getName();
				}
				fontHeight = fontDataItem.getHeight();
			}
			Font font = Graphics.getFont(fontName, fontHeight, SWT.ITALIC);
			descriptionText.setFont(font);
		}
	}
	
	public String getTextValue() {
		return text.getText();
	}
	
	public void setTextValue(String textValue) {
		text.setText(textValue);
	}
	
	public void setEnabled(boolean enabled) {
		label.setEnabled(enabled);
		text.setEnabled(enabled);
		descriptionText.setEnabled(enabled);
	}

}
