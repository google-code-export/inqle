package org.inqle.data.sampling.rap;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SimpleSubjectSparqlSampler;

import com.hp.hpl.jena.rdf.model.Model;

public class SimpleSubjectSparqlSamplerFactory implements ISamplerFactory {

	private String name;
	private String description;
	
	static Logger log = Logger.getLogger(SimpleSubjectSparqlSamplerFactory.class);

	private SimpleSubjectSparqlSampler baseSampler;

	/**
	 * Return the base sampler
	 */
	public ISampler getBaseSampler() {
		//log.info("getBaseSampler() called");
		if (baseSampler == null) {
			baseSampler = newSampler();
		}
		return baseSampler;
	}

	public SimpleSubjectSparqlSamplerWizard createWizard(Model model, Shell shell) {
		SimpleSubjectSparqlSamplerWizard wizard = new SimpleSubjectSparqlSamplerWizard(model, shell);
		return wizard;
	}

	public String getName() {
		return name;
	}

	public void setBaseSampler(ISampler sampler) {
		baseSampler = (SimpleSubjectSparqlSampler)sampler;
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
		//log.info("createWizardForReplica()...");
		SimpleSubjectSparqlSamplerWizard wizard = createWizard(model, shell);
		SimpleSubjectSparqlSampler replica = baseSampler.createReplica();
		wizard.setBean(replica);
		//log.info("Created wizard with (replica) Sampler: " + JenabeanWriter.toString(replica));
		return wizard;
	}
	
	public ISamplerFactory cloneFactory() {
		SimpleSubjectSparqlSamplerFactory newFactory = new SimpleSubjectSparqlSamplerFactory();
		newFactory.setBaseSampler(baseSampler);
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