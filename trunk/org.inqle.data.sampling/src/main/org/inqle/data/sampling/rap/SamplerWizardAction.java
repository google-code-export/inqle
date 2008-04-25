package org.inqle.data.sampling.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.ISampler;
import org.inqle.ui.rap.actions.DynaWizardDialog;

public class SamplerWizardAction extends Action {

	public static final int MODE_RUN = 1;
	public static final int MODE_OPEN = 2;
	public static final int MODE_CLONE = 3;
	private SamplerPart samplerPart;
	private Persister persister;
	private IWorkbenchWindow window;
	private String menuText;
	private int mode;
	private ISamplerFactory samplerFactory;
	private ISampler sampler = null;

	static Logger log = Logger.getLogger(SamplerWizardAction.class);
	
	public SamplerWizardAction(int mode, String menuText, SamplerPart samplerPart, IWorkbenchWindow window, Persister persister) {
		// TODO Auto-generated constructor stub
		this.mode = mode;
		this.menuText = menuText;
		this.samplerPart = samplerPart;
		this.samplerFactory = samplerPart.getSamplerFactory();
		this.window = window;
		this.persister = persister;
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
		//SimpleSparqlSampler testSampler = new SimpleSparqlSampler();
		//log.info(JenabeanWriter.toString(testSampler));
		//MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Opening new Wizard", event.toString());
		ISamplerWizard wizard = null;
		if (mode == MODE_RUN) {
			//wizard = new SamplerRunnerWizard(sampler, window.getShell());
		} else if (mode == MODE_OPEN) {
			wizard = samplerFactory.createWizardForReplica(persister.getMetarepositoryModel(), persister, window.getShell());
			wizard.setPart(samplerPart);
			DynaWizardDialog dialog = new DynaWizardDialog(window.getShell(), wizard);
			dialog.open();
		} else if (mode == MODE_CLONE) {
			sampler.setName("Clone of " + sampler.getName());
			persister.persist(sampler, persister.getMetarepositoryModel());
			samplerPart.fireUpdatePart();
		}
	}


}
