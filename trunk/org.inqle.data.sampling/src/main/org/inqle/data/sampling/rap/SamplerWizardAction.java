package org.inqle.data.sampling.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.ISampler;
import org.inqle.ui.rap.actions.DynaWizardDialog;

public class SamplerWizardAction extends Action {

	//public static final int MODE_RUN = 1;
	public static final int MODE_OPEN = 2;
	public static final int MODE_CLONE = 3;
	private SamplerPart samplerPart;
	//private Persister persister;
	private IWorkbenchWindow window;
	private String menuText;
	private int mode;
	private ISamplerFactory samplerFactory;
	private ISampler sampler = null;

	static Logger log = Logger.getLogger(SamplerWizardAction.class);
	
	public SamplerWizardAction(int mode, String menuText, SamplerPart samplerPart, IWorkbenchWindow window) {
		// TODO Auto-generated constructor stub
		this.mode = mode;
		this.menuText = menuText;
		this.samplerPart = samplerPart;
		this.samplerFactory = samplerPart.getSamplerFactory();
		this.window = window;
		//this.persister = persister;
//		log.info("Created SamplerWizardAction.");
	}
	

	public ISampler getSampler() {
		return sampler;
	}

	public void setSampler(ISampler sampler) {
		this.sampler = sampler;
	}
	
	@Override
	public String getText() {
		return menuText;
	}
	
	@Override
	public void runWithEvent(Event event) {
		log.info("Running SamplerWizardAction");
		//SimpleSparqlSampler testSampler = new SimpleSparqlSampler();
//		log.info("Running SamplerWizardAction for sampler: " + JenabeanWriter.toString(sampler));
		//MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Opening new Wizard", event.toString());
		try {
			ISamplerWizard wizard = null;
			//if (mode == MODE_RUN) {
				//wizard = new SamplerRunnerWizard(sampler, window.getShell());
			if (mode == MODE_OPEN) {
				log.trace("Opening wizard...");
				wizard = samplerFactory.createWizardForReplica(null, window.getShell());
				wizard.setPart(samplerPart);
				DynaWizardDialog dialog = new DynaWizardDialog(window.getShell(), wizard);
				dialog.open();
				log.trace("Opened wizard for replica.");
			} else if (mode == MODE_CLONE) {
//				Persister persister = Persister.getInstance();
//				sampler.setName("Clone of " + sampler.getName());
//				persister.persist(sampler);
//				samplerPart.fireUpdatePart();
				log.trace("Cloning wizard...");
				wizard = samplerFactory.createWizardForClone(null, window.getShell());
				wizard.setPart(samplerPart);
				DynaWizardDialog dialog = new DynaWizardDialog(window.getShell(), wizard);
				dialog.open();
				log.trace("Opened wizard for clone.");
			}
		} catch (Exception e) {
			log.error("Error running SamplerWizardAction", e);
		}
	}


}
