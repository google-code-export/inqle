package org.inqle.ui.rap.widgets;

import java.net.URI;

import org.apache.log4j.Logger;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class TextFieldShower implements IDataFieldShower {

	private Text text;
	private Text descriptionText;
	private String fieldUri;
	private Label label;
	private Label spacerLabel;
	private String fieldPropertyType;
	private static final Logger log = Logger.getLogger(TextFieldShower.class);
	
	public TextFieldShower(
			Composite composite, 
			String labelString, 
			String descriptionString, 
			String toolTipString, 
			int textStyle) {
		this (
			composite, true, labelString, descriptionString, toolTipString, textStyle
				);
	}
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
	public TextFieldShower(
			Composite composite, 
			boolean skipColumnBeforeDescription,
			String labelString, 
			String descriptionString, 
			String toolTipString, 
			int textStyle) {
		GridData gridData;
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		if (labelString==null) labelString="";
		if (descriptionString==null) descriptionString="";
		//create the controls
		label = new Label(composite, SWT.NONE);
		label.setText(labelString);

//		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		text = new Text(composite, textStyle);
		text.setLayoutData(gridData);
		if (toolTipString != null) {
			text.setToolTipText(toolTipString);
		}
		
		if (descriptionString != null && descriptionString.length() > 0) {
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			//in next row, skip the label column
			if (skipColumnBeforeDescription) {
				spacerLabel = new Label(composite, SWT.NONE);
			}
			
			//add the description
			descriptionText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
			descriptionText.setText(descriptionString);
			descriptionText.setLayoutData(gridData);
			Font currentFont = descriptionText.getFont();
			String fontName = null;
			int fontHeight = 10;
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
	
	public String getValue() {
		return text.getText();
	}
	
	public void setValue(String value) {
		text.setText(value);
	}
	
	public void setTextValue(String textValue) {
		if (textValue==null) return;
		text.setText(textValue);
	}

	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
	}

	/**
	 * Sometimes, this object represents a particular RDF property/predicate.  This field contains the URI of such property.
	 * @return
	 */
	public String getFieldUri() {
		return fieldUri;
	}

	public void setFieldUri(String fieldUri) {
		if (fieldUri != null) {
			this.fieldUri = fieldUri.trim();
		} else {
			this.fieldUri = fieldUri;
		}
		
	}
	
	/**
	 * Return the provided value, as a literal
	 * TODO return appropriate data type, according to a value in the query
	 * @return
	 */
	public RDFNode getRDFNodeValue() {
		if (getValue() == null) {
			return null;
		}
		Literal literalValue = ResourceFactory.createTypedLiteral(getValue());
		return literalValue;
	}

	public void remove() {
		label.dispose();
		text.dispose();
		if (spacerLabel != null) {
			spacerLabel.dispose();
			descriptionText.dispose();
		}
	}
	
	public String getFieldPropertyType() {
		return fieldPropertyType;
	}
	public void setFieldPropertyType(String fieldPropertyType) {
		if (fieldPropertyType != null) {
			this.fieldPropertyType = fieldPropertyType.trim();
		} else {
			this.fieldPropertyType = fieldPropertyType;
		}
		this.fieldPropertyType = fieldPropertyType;
	}
}