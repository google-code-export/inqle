/**
 * 
 */
package org.inqle.ui.rap.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * A wizard page which contains only information
 * @author David Donohue
 * Jul 26, 2008
 */
public class InfoPage extends WizardPage {

	private String detail;

	public InfoPage(String pageName, String description, String detail) {
		super(pageName);
		setDescription(description);
		this.detail = detail;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		Label detailLabel = new Label(composite, SWT.WRAP);
		detailLabel.setText(detail);
		
		setControl(composite);
	}

}
