/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * This class is intended to generate a wizard for editing a jenabean.
 * Subclasses should provide mappings from the RDF-backed Jenabean class
 * to the DynaWizardPage classes which generate the respective form elements.
 * This is done by adding all pages in method addPages().
 * 
 * Upon finishing, the DynaWizard simply saves the bean to the provided RDF Model.
 * If an implementation needs to do more, should
 * either override method performFinish or call super.performFinish() and add extra code
 * before or after this call.
 * 
 * @see SingleTextPage
 * 
 * @author David Donohue
 * Feb 20, 2008
 */
public abstract class DynaWizard extends Wizard {

	private static Logger log = Logger.getLogger(DynaWizard.class);
	
	protected Model saveToModel;
	protected Persister persister;
	protected Shell shell;
	protected IPart part = null;

	protected IBasicJenabean bean;
	/**
	 * @param bean
	 */
	public DynaWizard(Model saveToModel, Persister persister, Shell shell) {
		assert(saveToModel != null);
		assert(persister != null);
		assert(shell != null);
		this.saveToModel = saveToModel;
		this.persister = persister;
		this.shell = shell;
	}
	
	@Override
	public boolean performFinish() {
		//focus away from current item on current page, ensuring that databinding happens
		getContainer().getCurrentPage().getControl().forceFocus();
		log.info("Persisting:" + JenabeanWriter.toString(getBean()) + "\n...persisting to model of size:" + saveToModel.size());
		persister.persist(getBean(), saveToModel, true);
		
		if (part != null) {
			part.getParent().fireUpdatePart();
		}
		return true;
	}
	
	@Override
	public abstract void addPages();

	public IPart getPart() {
		return part;
	}

	public void setPart(IPart part) {
		this.part = part;
	}
	
	public IBasicJenabean getBean() {
		return bean;
	}

	public void setBean(IBasicJenabean bean) {
		this.bean = bean;
	}

}
