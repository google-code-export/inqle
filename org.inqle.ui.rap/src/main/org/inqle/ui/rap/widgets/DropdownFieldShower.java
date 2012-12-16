package org.inqle.ui.rap.widgets;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.inqle.rdf.RDF;

public class DropdownFieldShower implements IDataFieldShower {

//	private List list;
	private Combo list;
	private Text descriptionText;
	private String fieldUri;
	private String fieldPropertyType = RDF.DATA_PROPERTY;
	private ArrayList<String> allOptions;
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
		allOptions = new ArrayList<String>();
		allOptions.add("");
		//create a list of lower case version of the options
		java.util.List<String> lcOptions = new ArrayList<String>();
		
		if (options != null) {
			allOptions.addAll(Arrays.asList(options));
			
			for (String option: options) {
				if (option == null) lcOptions.add("");
				lcOptions.add(option.toLowerCase().trim());
			}
		}
		
		
		
		
		GridData gridData;
		
		//create the controls
		Label label = new Label(composite, SWT.NONE);
		if (labelString != null) {
			label.setText(labelString);
		}

		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		list = new Combo(composite, listStyle | SWT.READ_ONLY);
		list.setLayoutData(gridData);
		if (toolTipString != null) {
			list.setToolTipText(toolTipString);
		}
		String[] nullStr = {};
		list.setItems(allOptions.toArray(nullStr));
		
		//if possible, pre-select the value which matches the label
		try {
			String matchLabel = labelString.toLowerCase().trim();
			if (lcOptions.indexOf(matchLabel) >= 0) {
				list.select(lcOptions.indexOf(matchLabel) + 1);
			}
		} catch (RuntimeException e) {
			//error.  never mind pre-selecting the value which matches the label
		}
		
		//if a description was added, show it
		if (descriptionString != null && descriptionString.length() > 0) {
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			
			//in next row, skip the label column
			new Label(composite, SWT.NONE);
			
			//add the description
			descriptionText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
			if (descriptionString != null) {
				descriptionText.setText(descriptionString);
			}
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
	
	public int getSelectedIndex() {
		return list.getSelectionIndex();
	}
	
	public String getValue() {
		if (list == null || list.getSelectionIndex()<0) return null;
		String val = list.getItem(list.getSelectionIndex());
		if (val == null || val.length()==0) {
			return null;
		}
		return val;
	}
	
	public void setSelectedIndex(int selectedIndex) {
		list.select(selectedIndex);
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
		if (fieldUri != null) {
			this.fieldUri = fieldUri.trim();
		}
	}
	
	public void remove() {
		list.removeAll();
		list.dispose();
		descriptionText.dispose();
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
	}
	public void select(String header) {
		int headerIndex = allOptions.indexOf(header);
		if (headerIndex >= 0) {
			list.select(headerIndex);
		}
		
	}
}
