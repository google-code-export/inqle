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

/**
 * This class is not yet used.
 * TODO replace dateTime with a DateTime type
 * @author David Donohue
 * Sep 17, 2008
 */
public class DateTimeFieldShower implements IDataFieldShower {

	private Text dateTime;
	private Text descriptionText;
	private String fieldUri;
	private Label label;
	private Label spacerLabel;
	private String fieldPropertyType;
	private static final Logger log = Logger.getLogger(DateTimeFieldShower.class);
	
	/**
	 * Adds a dateTime field to an existing composite.  Expects that composite to have a 2 column GridLayout.
	 * A dateTime field consists of 
	 *  * a label
	 *  * a dateTime input widget
	 *  * optionally a tool tip dateTime (on mouseover)
	 *  * optionally below this, a description field
	 * @param composite
	 * @param labelString
	 * @param descriptionString
	 * @param toolTipString
	 * @param textStyle
	 */
	public DateTimeFieldShower(
			Composite composite, 
			String labelString, 
			String descriptionString, 
			String toolTipString, 
			int textStyle) {
		GridData gridData;
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		//create the controls
		label = new Label(composite, SWT.NONE);
		label.setText(labelString);

//		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		dateTime = new Text(composite, textStyle);
		dateTime.setLayoutData(gridData);
		if (toolTipString != null) {
			dateTime.setToolTipText(toolTipString);
		}
		
		if (descriptionString != null && descriptionString.length() > 0) {
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			//in next row, skip the label column
			spacerLabel = new Label(composite, SWT.NONE);
			
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
		return dateTime.getText();
	}
	
	public void setTextValue(String textValue) {
		dateTime.setText(textValue);
	}

	public void setEnabled(boolean enabled) {
		dateTime.setEnabled(enabled);
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
		dateTime.dispose();
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
