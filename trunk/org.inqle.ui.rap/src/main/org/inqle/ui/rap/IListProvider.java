package org.inqle.ui.rap;

import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;

public interface IListProvider {

	public List<Object> getList(IWizardPage page);

}
