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

public class TextFieldShower {

	private Text text;
	private Text descriptionText;
	
	private static final Logger log = Logger.getLogger(TextFieldShower.class);
	
	/**
	 * Adds a text field to an existing composite.  Expects that composite to have a 2 column GridLayout.
	 * A text field consists of 
	 *  * a label
	 *  * a text input widget
	 *  * optionally a tool tip text (on mouseover)
	 *  * optionally below this, a description field
	 * @param composite
	 * @param labelString
	 * @param descriptionString
	 * @param toolTipString
	 * @param textStyle
	 */
	public TextFieldShower(Composite composite, String labelString, String descriptionString, String toolTipString, int textStyle) {
		GridData gridData;
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		//create the controls
		Label label = new Label(composite, SWT.NONE);
		label.setText(labelString);

//		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		text = new Text(composite, textStyle);
		text.setLayoutData(gridData);
		if (toolTipString != null) {
			text.setToolTipText(toolTipString);
		}
		
		if (descriptionString != null && descriptionString.length() > 0) {
			//in next row, skip the label column
			new Label(composite, SWT.NONE);
			
			//add the description
			descriptionText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
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
		text.setEnabled(enabled);
	}
}
