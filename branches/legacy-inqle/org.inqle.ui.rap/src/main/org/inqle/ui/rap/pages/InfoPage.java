/**
 * 
 */
package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A wizard page which contains only information
 * @author David Donohue
 * Jul 26, 2008
 */
public class InfoPage extends WizardPage {

	private String detail;

	private static Logger log = Logger.getLogger(InfoPage.class);
	
	public InfoPage(String pageName, String description, String detail) {
		super(pageName, pageName, null);
		setDescription(description);
		this.detail = detail;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(1, true);
		composite.setLayout(gl);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Text detailText = new Text(composite, SWT.WRAP | SWT.READ_ONLY | SWT.MULTI);
		detailText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		detailText.setText(detail);
		setControl(composite);
	}

}
