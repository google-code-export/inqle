package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.inqle.data.rdf.jenabean.ICloneableJenabean;

public class RadioOrListSelectorPage extends ListSelectorPage implements SelectionListener {

	private List<String> radioOptionTexts = new ArrayList<String>();
	//private int selectedOptionIndex = 0;
	private List<Button> buttons = new ArrayList<Button>();
	private IRadioBeanProvider radioBeanProvider;

	private static final Logger log = Logger.getLogger(RadioOrListSelectorPage.class);
	
	public RadioOrListSelectorPage(ICloneableJenabean bean, String beanValueId,
			String title, ImageDescriptor titleImage) {
		super(bean, beanValueId, title, titleImage);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(1, false);
		selfComposite.setLayout(gl);
		assert(initialItems != null);
		
		createRadios();
		
		createList();
	}

	protected void createRadios() {
		Composite radiosComposite = new Composite(selfComposite, SWT.NONE);
		radiosComposite.setLayout (new RowLayout (SWT.VERTICAL));
		
		int index = 0;
		for (String radioOptionText: getRadioOptionTexts()) {
			Button button = new Button (radiosComposite, SWT.RADIO);
			button.setText (radioOptionText);
			if (getSelectedOptionIndex() == index) {
				button.setSelection (true);
			}
			buttons .add(button);
			button.addSelectionListener(this);
			index++;
			
		}
		
	}

	private List<String> getRadioOptionTexts() {
		return radioOptionTexts;
	}

	public void setRadioOptionTexts(List<String> radioOptionTexts) {
		this.radioOptionTexts = radioOptionTexts;
	}

//	public void setSelectedOptionIndex(int selectedOptionIndex) {
//		this.selectedOptionIndex = selectedOptionIndex;
//	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		
		Button selectedButton = (Button)selectionEvent.getSource();
		//log.info("Button pressed:" + selectedButton.getText());
		int selectedIndex = getSelectedIndex(selectedButton);
		setSelectedIndex(selectedIndex);
		//if this is the last item, toggle the status of the List
		updateListViewer();
		
		
	}

	private void setSelectedIndex(int selectedIndex) {
		radioBeanProvider.setValue(new Integer(selectedIndex));
	}

	private void updateListViewer() {
		if (getSelectedOptionIndex() == getRadioOptionTexts().size() - 1) {
			//log.info("Last item selected");
			listViewer.getList().setEnabled(true);
		} else {
			listViewer.getList().deselectAll();
			listViewer.getList().setEnabled(false);
		}
		
	}

	private int getSelectedIndex(Button selectedItem) {
		int selectedIndex = -1;
		int index = 0;
		for (Button button: buttons) {
			if (selectedItem == button) {
			//if (selectedItem.getText().equals(button.getText())) {
				selectedIndex = index;
			}
			index++;
		}
		return selectedIndex;
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		super.onEnterPageFromPrevious();
		//log.info("Updating radios with selectedIndex = " + getSelectedOptionIndex());
		updateRadios();
		updateListViewer();
		//setSelectedOptionIndex();
	}
	
	private void updateRadios() {
		Button selectedButton = buttons.get(getSelectedOptionIndex());
		//log.info("Selecting button: " + selectedButton.getText());
		selectedButton.setSelection(true);
	}

	public int getSelectedOptionIndex() {
		return ((Integer)radioBeanProvider.getValue()).intValue();
	}

	public void setRadioBeanProvider(IRadioBeanProvider radioBeanProvider) {
		this.radioBeanProvider = radioBeanProvider;
	}
}
