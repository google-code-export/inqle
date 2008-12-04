/**
 * 
 */
package org.inqle.ui.rap.widgets;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileUtils;

/**
 * @author David Donohue
 * Dec 3, 2008
 */
public class HeaderPropertiesDialog extends AScrolledOntResourceDialog implements SelectionListener {

	private List<String> headerList;
	private List<Button> checkBoxes = new ArrayList<Button>();
	private List<Text> uriTexts = new ArrayList<Text>();
	private List<Text> nameTexts = new ArrayList<Text>();
	private List<Text> descriptionTexts = new ArrayList<Text>();
	private String subjectClass;

	protected HeaderPropertiesDialog(Shell parentShell, OntModel ontModel, List<String> headerList, String subjectClass) {
		super(parentShell);
		setOntModel(ontModel);
		this.headerList = headerList;
		this.subjectClass = subjectClass;
	}

	@Override
	protected void addFormElements() {
		int index=0;
		for (String header: headerList) {
			if (header == null) continue;
			String headerId = header.replaceAll(" ", "_");
			try {
				headerId = URLEncoder.encode(headerId, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				//leave as default
			}
			Button checkBox = new Button(formComposite, SWT.CHECK);
			checkBox.setData(index);
			checkBox.addSelectionListener(this);
			Text uriText = new Text(formComposite, SWT.BORDER);
			uriText.setText(getUriPrefix() + headerId);
			uriText.setEnabled(false);
			Text nameText = new Text(formComposite, SWT.BORDER);
			nameText.setText(header);
			nameText.setEnabled(false);
			Text descriptionText = new Text(formComposite, SWT.BORDER);
			descriptionText.setEnabled(false);
			checkBoxes.add(checkBox);
			uriTexts.add(uriText);
			nameTexts.add(nameText);
			descriptionTexts.add(descriptionText);
			
			index++;
		}

	}

	@Override
	protected OntModel createModel() {
		//TODO add stuff to model
		return ontModel;
	}

	@Override
	protected boolean validate() {
		return true;
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		Button clickedButton = (Button)clickedObject;
		Object buttonVal = clickedButton.getData();
		int buttonInt = (Integer)buttonVal;
		uriTexts.get(buttonInt).setEnabled(clickedButton.getEnabled());
		nameTexts.get(buttonInt).setEnabled(clickedButton.getEnabled());
		descriptionTexts.get(buttonInt).setEnabled(clickedButton.getEnabled());
		
	}

}
