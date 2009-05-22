package org.inqle.ui.rap.wiki.parts;

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
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.ui.rap.IDisposableViewer;
import org.inqle.ui.rap.views.ObjectViewer;

public class WikiPageViewer extends Viewer implements IDisposableViewer {

	private Composite composite;
	private Object pageObject;
	private Text titleText;
	private Text descriptionText;
	private Datamodel datamodel;
	private Text typeText;
	private Text relationshipsText;
	
	private static final Logger log = Logger.getLogger(WikiPageViewer.class);
	
	public WikiPageViewer(Composite parentComposite, Datamodel datamodel, Object bean) {
		this.datamodel = datamodel;
		this.pageObject = bean;
	
		composite = new Composite(parentComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		WikiDataExtractor dataExtractor = new WikiDataExtractor(datamodel, pageObject);
		
		typeText = new Text(composite, SWT.READ_ONLY);
		typeText.setText(dataExtractor.getType());
		
		titleText = new Text(composite, SWT.READ_ONLY);
		titleText.setText(dataExtractor.getTitle());
		
		descriptionText = new Text(composite, SWT.READ_ONLY);
		descriptionText.setText(dataExtractor.getDescription());
		
		relationshipsText = new Text(composite, SWT.READ_ONLY | SWT.MULTI);
		relationshipsText.setText(String.valueOf(dataExtractor.getStatements()));
		relationshipsText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		
		refresh();
	}
	
	public void clearData() {
		typeText.setText("");
		titleText.setText("");
		descriptionText.setText("");
		relationshipsText.setText("");
	}

	public void dispose() {
		typeText.dispose();
		titleText.dispose();
		descriptionText.dispose();
		relationshipsText.dispose();
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
		return pageObject;
	}

	@Override
	public void setInput(Object pageObject) {
		this.pageObject = pageObject;
		refresh();
	}

}
