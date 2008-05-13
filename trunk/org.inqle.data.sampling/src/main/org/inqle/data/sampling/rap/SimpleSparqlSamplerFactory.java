package org.inqle.data.sampling.rap;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SimpleSparqlSampler;

import com.hp.hpl.jena.rdf.model.Model;

public class SimpleSparqlSamplerFactory implements ISamplerFactory {

	private String name;
	private String description;
	
	static Logger log = Logger.getLogger(SimpleSparqlSamplerFactory.class);

	private SimpleSparqlSampler baseSampler;

	/**
	 * Return the base sampler
	 */
	public ISampler getBaseSampler() {
		//log.info("getBaseSampler() called");
		if (baseSampler == null) {
			baseSampler = new SimpleSparqlSampler();
		}
		return baseSampler;
	}

	public SimpleSparqlSamplerWizard createWizard(Model model, Persister persister, Shell shell) {
		if (model == null) {
			model = persister.getMetarepositoryModel();
		}
		SimpleSparqlSamplerWizard wizard = new SimpleSparqlSamplerWizard(model, persister, shell);
		return wizard;
	}

	public String getName() {
		return name;
	}

	public void setBaseSampler(ISampler sampler) {
		baseSampler = (SimpleSparqlSampler)sampler;
	}

	public boolean hasWizard() {
		return true;
	}

//	public ISampler cloneSampler() {
//		SimpleSparqlSampler sampler = new SimpleSparqlSampler();
//		sampler.clone(getBaseSampler());
//		return sampler;
//	}

	public ISampler newSampler() {
		SimpleSparqlSampler sampler = new SimpleSparqlSampler();
		return sampler;
	}

//	public SimpleSparqlSampler replicateSampler() {
////		log.info("Before replicating, baseSampler=" + JenabeanWriter.toString(baseSampler));
////		log.info("Before replicating, getBaseSampler()=" + JenabeanWriter.toString((SimpleSparqlSampler)getBaseSampler()));
//		SimpleSparqlSampler replicaSampler = baseSampler.createReplica();
//		//log.info("Created replicaSampler=" + JenabeanWriter.toString(replicaSampler));
//		return replicaSampler;
//	}

	public ISamplerWizard createWizardForReplica(Model model,
			Persister persister, Shell shell) {
		//log.info("createWizardForReplica()...");
		SimpleSparqlSamplerWizard wizard = createWizard(model, persister, shell);
		SimpleSparqlSampler replica = baseSampler.createReplica();
		wizard.setBean(replica);
		//log.info("Created wizard with (replica) Sampler: " + JenabeanWriter.toString(replica));
		return wizard;
	}
	
	public ISamplerFactory cloneFactory() {
		SimpleSparqlSamplerFactory newFactory = new SimpleSparqlSamplerFactory();
		newFactory.setBaseSampler(baseSampler);
		return newFactory;
	}
	
//	public ISamplerFactory cloneFactory(ISampler newBaseSampler) {
//		SimpleSparqlSampler newSSSampler = (SimpleSparqlSampler)newBaseSampler;
//		//log.info("SimpleSparqlSamplerFactory.clone(newBaseSampler)");
//		SimpleSparqlSamplerFactory newFactory = new SimpleSparqlSamplerFactory();
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
