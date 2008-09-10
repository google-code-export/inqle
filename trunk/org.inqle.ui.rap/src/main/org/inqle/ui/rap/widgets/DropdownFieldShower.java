package org.inqle.ui.rap.widgets;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class DropdownFieldShower implements IDataFieldShower {

	private List list;
	private Text descriptionText;
	private String fieldUri;
	private String fieldSubjectType;
	private static final Logger log = Logger.getLogger(DropdownFieldShower.class);
	
	public DropdownFieldShower (
			Composite composite, 
			String[] options, 
			String labelString, 
			String descriptionString) {
		this(composite, options, labelString, descriptionString, null, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
	}
	/**
	 * Adds a dropdown select field to an existing composite.  Expects that composite to have a 2 column GridLayout.
	 * A dropdown select field consists of 
	 *  * a label
	 *  * a select box widget
	 *  * optionally a tool tip list (on mouseover)
	 *  * optionally below this, a description field
	 * @param composite
	 * @param labelString
	 * @param descriptionString
	 * @param toolTipString
	 * @param listStyle
	 */
	public DropdownFieldShower(
			Composite composite, 
			String[] options, 
			String labelString, 
			String descriptionString, 
			String toolTipString, 
			int listStyle) {
		java.util.List<String> allOptions = new ArrayList<String>();
		allOptions.add("");
		allOptions.addAll(Arrays.asList(options));
		GridData gridData;
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		//create the controls
		Label label = new Label(composite, SWT.NONE);
		label.setText(labelString);

//		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		list = new List(composite, listStyle);
		list.setLayoutData(gridData);
		if (toolTipString != null) {
			list.setToolTipText(toolTipString);
		}
		String[] nullStr = {};
		list.setItems(allOptions.toArray(nullStr));
		
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
	
	public int getSelectedIndex() {
		return list.getSelectionIndex();
	}
	
	public String getValue() {
		if (list == null) return null;
		String val = list.getItem(list.getSelectionIndex());
		if (val == null || val.length()==0) {
			return null;
		}
		return val;
	}
	
	public void setSelectedIndex(int selectedIndex) {
		list.setSelection(selectedIndex);
	}

	public void setEnabled(boolean enabled) {
		list.setEnabled(enabled);
	}

	/**
	 * Sometimes, this object represents a particular RDF property/predicate.  This field contains the URI of such property.
	 * @return
	 */
	public String getFieldUri() {
		return fieldUri;
	}

	public void setFieldUri(String fieldUri) {
		this.fieldUri = fieldUri;
	}
	public void remove() {
		list.removeAll();
		list.dispose();
		descriptionText.dispose();
		
	}
	public String getFieldPropertyType() {
		return fieldSubjectType;
	}
	public void setFieldSubjectType(String fieldSubjectType) {
		this.fieldSubjectType = fieldSubjectType;
	}
}
