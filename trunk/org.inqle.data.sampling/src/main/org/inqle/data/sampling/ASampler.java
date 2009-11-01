package org.inqle.data.sampling;

import java.util.Collection;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.TargetDatamodelName;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Namespace;

/**
 * This is a basic, abstract implementation of the ISampler 
 * interface.  It is intended to be subclassed by new Sampler algorithms.
 * New implementations should:
 *  * to enable persisting your class to RDF store,
 *    add a Namespace annotation to the class as in
 *    @Namespace("http://my.namespace.org")
 *    public class MySampler extends ASampler
 *  * add any new fields and associated getters and setters using
 *    the Javabeans convention
 *  * override createClone(): First call super.createClone() to create new object,
 *    then add any new fields to your clone
 *  * override execute(): perform all steps to populate the resultDataTable field with
 *    a final result
 * @author David Donohue
 * Feb 29, 2008
 */
@TargetDatamodelName(SamplerInfo.SAMPLER_DATASET)
@Namespace(RDF.INQLE)
public abstract class ASampler extends UniqueJenabean implements ISampler {

//	protected Collection<String> availableDatamodels;

	protected Collection<String> selectedDatamodels;

	public void setSelectedDatamodels(Collection<String> selectedDatamodels) {
		this.selectedDatamodels = selectedDatamodels;
	}

	protected Arc labelArc;
	
	public Arc getLabelArc() {
		return this.labelArc;
	}
//	/**
//	 * Add all field values from the provided template sampler to this sampler,
//	 * except the ID field
//	 * @param sampler the provided sampler
//	 */
//	public void clone(ISampler templateSampler) {
//		setSelectedDatamodels(templateSampler.getSelectedDatamodels());
//		setLabelArc(templateSampler.getLabelArc());
//		super.clone(templateSampler);
//	}
//	
//	public void replicate(ISampler objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//	}

	public Collection<String> getSelectedDatamodels() {
		return selectedDatamodels;
	}

	@Override
	public String getName() {
		if (name == null) {
			return this.getClass().getName();
		}
		return name;
	}
	
	public void setLabelArc(Arc labelArc) {
		this.labelArc = labelArc;
	}
	
	/**
	 * This basic implementation does nothing.  
	 * Custom subclasses of ASampler can use this method to enable samplers to 
	 * preserve state info.  To do so, override this method and extract some info from the custom sampler.
	 * First check that it is an instance of the same class.  Avoid storing the sampler as a field,
	 * as this would lead to chaining of samplers in memory, and perhaps Stack Overflow Error or the like.
	 */
	public void setPreviousSampler(ISampler previousSampler) {
		//do nothing
	}
	
	/**
	 * Custom sampler classes can override this method, to support the ability to complete a broader
	 * sampling strategy.  Default: there is no strategy, except to sample repeatedly, so always return false
	 * 
	 */
	public boolean isFinishedSamplingStrategy() {
		return false;
	}

}
