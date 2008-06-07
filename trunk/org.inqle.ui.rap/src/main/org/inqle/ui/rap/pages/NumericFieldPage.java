/**
 * 
 */
package org.inqle.ui.rap.pages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

/**
 * Generates a page with a single numeric field.
 * @author David Donohue
 * May 16, 2008
 */
public class NumericFieldPage extends BeanWizardPage {

	public NumericFieldPage(IBasicJenabean bean, String beanValueId, String title, String labelText,
			ImageDescriptor titleImage) {
		super(bean, beanValueId, title, titleImage);
		this.labelText = labelText;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.pages.DynaWizardPage#addElements(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void addElements() {
		Composite composite = selfComposite;
		new Label (composite, SWT.NONE).setText(labelText);	
		Text numericField = new Text(composite, SWT.BORDER);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		numericField.setLayoutData(gridData);
		bindText(numericField, bean, beanValueId);
	}
}
