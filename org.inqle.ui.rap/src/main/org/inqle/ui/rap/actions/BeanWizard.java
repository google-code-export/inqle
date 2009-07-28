/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.pages.SingleTextPage;

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
 * Removing data binding therefore deprecated
 * @author David Donohue
 * Feb 20, 2008
 */
@Deprecated
public abstract class BeanWizard extends DynaWizard {

	private static Logger log = Logger.getLogger(BeanWizard.class);
	
	protected Model saveToModel;
	protected Shell shell;
	protected IPart part = null;

	protected IBasicJenabean bean;
	/**
	 * @param bean
	 */
	public BeanWizard(Model saveToModel, Shell shell) {
		super(shell);
		this.saveToModel = saveToModel;
		this.shell = shell;
	}
	
	/**
	 * Save the bean to the saveToModel.  If this is null, save to the bean's target dataset
	 */
	@Override
	public boolean performFinish() {
		Persister persister = Persister.getInstance();
		//focus away from current item on current page, ensuring that databinding happens
		getContainer().getCurrentPage().getControl().forceFocus();
		
		log.info("PPPPPPPPPPPPPPPPPPPP Persisting:" + JenabeanWriter.toString(getBean()) + "\n...persisting to model of role: " + Persister.getTargetDatamodelId(getBean()));
		if (saveToModel != null) {
			persister.persist(getBean(), saveToModel, true);
		} else {
			persister.persist(getBean());
		}
		log.info("Persisted.  Now update UI...");
		if (part != null && part.getParent() != null) {
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
	
	/**
	 * Override if any operations are needed prior to closing the wizard.  This 
	 * is called upon a close event from the DynaWizardDialog
	 */
	public void prepareForClose() {
	}

}
