package org.inqle.ui.rap.wiki.parts;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jenabean.INamedAndDescribedJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IDisposableViewer;
import org.inqle.ui.rap.views.ObjectViewer;
import org.inqle.ui.rap.widgets.PropertiesTable;
import org.inqle.ui.rap.wiki.WikiData;

import com.hp.hpl.jena.rdf.model.Model;

public class WikiPageViewer extends Viewer implements IDisposableViewer {

	private Composite composite;
	private String pageResourceUri;
	private Text titleText;
	private Text descriptionText;
//	private Datamodel datamodel;
	private Text typeText;
	private Text relationshipsText;
	private PropertiesTable propertiesTable;
	private WikiData wikiData;
	
	private static final Logger log = Logger.getLogger(WikiPageViewer.class);
	
	public WikiPageViewer(Composite parentComposite, Datamodel datamodel, String resourceUri) {
//		this.datamodel = datamodel;
		this.pageResourceUri = resourceUri;
	
		composite = new Composite(parentComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Persister persister = Persister.getInstance();
		Model model = persister.getModel(datamodel);
		wikiData = new WikiData(model, pageResourceUri);
		
		typeText = new Text(composite, SWT.READ_ONLY);
		typeText.setText(wikiData.getClassUri());
		
		titleText = new Text(composite, SWT.READ_ONLY);
		titleText.setText(wikiData.getTitle());
		
		descriptionText = new Text(composite, SWT.READ_ONLY);
		descriptionText.setText(wikiData.getDescription());
		
//		Map<String, Object> properties = wikiData.getProperties();
//		propertiesTable = new PropertiesTable(composite, SWT.READ_ONLY);
//		propertiesTable.setProperties(properties);
//		refresh();
	}
	
	public void clearData() {
		typeText.setText("");
		titleText.setText("");
		descriptionText.setText("");
		relationshipsText.setText("");
		propertiesTable.clearData();
	}

	public void dispose() {
		typeText.dispose();
		titleText.dispose();
		descriptionText.dispose();
		propertiesTable.dispose();
	}

	@Override
	public Control getControl() {
		return composite;
	}

	@Override
	public ISelection getSelection() {
		//return selection;
		return null;
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public void setSelection(ISelection selection, boolean arg1) {
		log.trace("setSelection(" + selection + ", " + arg1 + ")");
		//this.selection = selection;
	}
	
	@Override
	public void inputChanged(Object input, Object oldInput) {
		log.trace("inputChanged(" + input + ", " + oldInput + ")");
	}

	@Override
	public Object getInput() {
		return wikiData;
	}

	@Override
	public void setInput(Object pageObject) {
		if (pageObject instanceof WikiData) {
			this.wikiData = (WikiData)pageObject;
			refresh();
		}
	}

}
