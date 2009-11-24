package org.inqle.data.sampling.rap;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.util.BeanTool;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SimpleSubjectSparqlSampler;

import com.hp.hpl.jena.rdf.model.Model;

public class SimpleSubjectSparqlSamplerFactory implements ISamplerFactory {

	private String name;
	private String description;
	
	static Logger log = Logger.getLogger(SimpleSubjectSparqlSamplerFactory.class);

	private SimpleSubjectSparqlSampler startingSampler;

	/**
	 * Return the base sampler
	 */
	public ISampler getStartingSampler() {
		//log.info("getBaseSampler() called");
		if (startingSampler == null) {
			startingSampler = newSampler();
		}
		return startingSampler;
	}

	public SimpleSubjectSparqlSamplerWizard createWizard(Model model, Shell shell) {
		SimpleSubjectSparqlSamplerWizard wizard = new SimpleSubjectSparqlSamplerWizard(shell);
		return wizard;
	}

	public String getName() {
		return name;
	}

	public void setStartingSampler(ISampler sampler) {
		startingSampler = (SimpleSubjectSparqlSampler)sampler;
	}

	public boolean hasWizard() {
		return true;
	}

//	public ISampler cloneSampler() {
//		SimpleSubjectSparqlSampler sampler = new SimpleSubjectSparqlSampler();
//		sampler.clone(getBaseSampler());
//		return sampler;
//	}

	public SimpleSubjectSparqlSampler newSampler() {
		SimpleSubjectSparqlSampler sampler = new SimpleSubjectSparqlSampler();
		sampler.setName(getName());
		sampler.setDescription(getDescription());
		return sampler;
	}

//	public SimpleSubjectSparqlSampler replicateSampler() {
////		log.info("Before replicating, baseSampler=" + JenabeanWriter.toString(baseSampler));
////		log.info("Before replicating, getBaseSampler()=" + JenabeanWriter.toString((SimpleSubjectSparqlSampler)getBaseSampler()));
//		SimpleSubjectSparqlSampler replicaSampler = baseSampler.createReplica();
//		//log.info("Created replicaSampler=" + JenabeanWriter.toString(replicaSampler));
//		return replicaSampler;
//	}

	public ISamplerWizard createWizardForReplica(Model model, Shell shell) {
		SimpleSubjectSparqlSamplerWizard wizard = createWizard(model, shell);
//		SimpleSubjectSparqlSampler replica = baseSampler.createReplica();
		SimpleSubjectSparqlSampler replica = BeanTool.replicate(startingSampler);
		wizard.setSampler(replica);
		return wizard;
	}
	
	public ISamplerWizard createWizardForClone(Model model, Shell shell) {
		SimpleSubjectSparqlSamplerWizard wizard = createWizard(model, shell);
//		SimpleSubjectSparqlSampler clone = baseSampler.createClone();
		SimpleSubjectSparqlSampler clone = BeanTool.clone(startingSampler);
		wizard.setSampler(clone);
		return wizard;
	}
	
	public ISamplerFactory cloneFactory() {
		SimpleSubjectSparqlSamplerFactory newFactory = new SimpleSubjectSparqlSamplerFactory();
		newFactory.setStartingSampler(startingSampler);
		newFactory.setName(getName());
		newFactory.setDescription(getDescription());
		return newFactory;
	}
	
//	public ISamplerFactory cloneFactory(ISampler newBaseSampler) {
//		SimpleSubjectSparqlSampler newSSSampler = (SimpleSubjectSparqlSampler)newBaseSampler;
//		//log.info("SimpleSubjectSparqlSamplerFactory.clone(newBaseSampler)");
//		SimpleSubjectSparqlSamplerFactory newFactory = new SimpleSubjectSparqlSamplerFactory();
//		newFactory.setBaseSampler(newSSSampler);
//		return newFactory;
//	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return getClass() + "(" + getName() + ")";
	}
}
