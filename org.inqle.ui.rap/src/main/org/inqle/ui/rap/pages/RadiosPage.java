package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class RadiosPage extends WizardPage {

//	private Composite composite;

	private List<Button> buttons = new ArrayList<Button>();
	private List<String> radioOptionTexts = new ArrayList<String>();
	
	public RadiosPage(String pageName, String pageDescription) {
		this(pageName, pageName, null);
		setMessage(pageDescription);
	}
	
	public RadiosPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout (new RowLayout (SWT.VERTICAL));
		
		int index = 0;
		for (String radioOptionText: getRadioOptionTexts()) {
			Button button = new Button (composite, SWT.RADIO);
			button.setText (radioOptionText);
			if (index == 0) {
				button.setSelection (true);
			}
			buttons.add(button);
			index++;
		}
		setControl(composite);
	}

	private List<String> getRadioOptionTexts() {
		return radioOptionTexts;
	}

	public void setRadioOptionTexts(List<String> radioOptionTexts) {
		this.radioOptionTexts = radioOptionTexts;
	}

	public int getSelectedIndex() {
		int selectedIndex = -1;
		int index = 0;
		for (Button button: buttons) {
			if (button.getSelection()) {
				selectedIndex = index;
			}
			index++;
		}
		return selectedIndex;
	}


}
