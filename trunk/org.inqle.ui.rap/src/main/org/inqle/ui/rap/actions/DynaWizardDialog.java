/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.inqle.ui.rap.pages.DynaWizardPage;

/**
 * This class intercepts a few wizard events
 * to permit more dynamic behavior of the wizard,
 * expects DynaWizardPages but does not require
 * @author David Donohue
 * Apr 6, 2008
 */
public class DynaWizardDialog extends WizardDialog {

	private static Logger log = Logger.getLogger(DynaWizardDialog.class);
	
	public DynaWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}
	
	@Override
	/**
	 * Next button was pressed.  Call onNextPage for current DynaWizardPage.  If false is returned, then
	 * do not advance to the next page.  Otherwise call onNextPage on the next DynaWizardPage, 
	 * and then advance to it.
	 */
	public void nextPressed() {
		//log.info("DynaWizardDialog.nextPressed()");
		if (getCurrentPage() instanceof DynaWizardPage) {
			boolean thisPageValid = ((DynaWizardPage)getCurrentPage()).onNextPage();
			if (!thisPageValid) {
				return;
			}
		}
		IWizardPage nextPage = getCurrentPage().getNextPage();
		//log.info("next page =" + nextPage.getName());
		if (nextPage instanceof DynaWizardPage) {
			((DynaWizardPage)nextPage).onEnterPageFromPrevious();
		}
		super.nextPressed();
	}
	
	@Override
	/**
	 * Next button was pressed.  Call onNextPage for current DynaWizardPage.  If false is returned, then
	 * do not advance to the next page.  Otherwise call onNextPage on the next DynaWizardPage, 
	 * and then advance to it.
	 */
	public void backPressed() {
		if (getCurrentPage() instanceof DynaWizardPage) {
			boolean thisPageValid = ((DynaWizardPage)getCurrentPage()).onPreviousPage();
			if (!thisPageValid) {
				return;
			}
		}
		IWizardPage prevPage = getCurrentPage().getPreviousPage();
		if (prevPage instanceof DynaWizardPage) {
			((DynaWizardPage)prevPage).onEnterPageFromNext();
		}
		super.backPressed();
	}
	
	/**
	 * this disposes of uploader on clicking the wizard's cancel button
	 */
	@Override
	protected void cancelPressed() {
		prepareWizardForClose();
	}

	/**
	 * this disposes of uploader on clicking the wizard's "X" in upper right
	 */
	@Override
	protected boolean canHandleShellCloseEvent() {
		prepareWizardForClose();
		return true;
	}
	
	private void prepareWizardForClose() {
		log.info("prepareWizardForClose()...");
		IWizard wizard = getWizard();
		if (wizard instanceof DynaWizard) {
			((DynaWizard)wizard).prepareForClose();
		}
		close();
	}

}
