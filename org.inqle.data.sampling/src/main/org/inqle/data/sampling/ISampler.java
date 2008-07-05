package org.inqle.data.sampling;

import java.util.Collection;
import java.util.Dictionary;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.IUniqueJenabean;

import thewebsemantic.Id;

/**
 * Implementations of this interface contain information related to
 * the execution of a Sampling algorithm plus subsequent machine
 * learning experimentation.  ISampler objects are intended to be
 * persistable to RDF.  They are persisted under 3 circumstances:
 *  * They might be saved periodically in the course of executing a 
 *    sampling strategy
 *  * They might be saved as a template for future experimentation, 
 *    as they can serve as a starting point for future learning cycles.
 *  * They might be saved at the end of an experiment, to represent 
 *    the final results of one run through the learning cycle.
 * 
 * In order to be persistable, implementations should follow the Jenabean 
 * conventions:  Here is how you do this:
 *  * Before the class name, add an annotation like this
 *  @Namespace("http://my.base.uri.com/")
 *  
 * The Jenabean engine will assume that all properties adhering to the 
 * Javabean convention will be persistable
 * 
 * @author David Donohue
 * Dec 26, 2007
 */
public interface ISampler extends IUniqueJenabean {
	
	//public static final String PROPERTY_WEIGHT = "weight";
	public static final String URI_SUBJECT_CONTAINING_COMMON_ATTRIBUTES = RDF.INQLE + "subjectContainingCommonAttributes";
	public static final String ID = "org.inqle.data.sampling.ISampler";
	public static final String SAMPLER_DATASET = "org.inqle.datasets.samplers";
	/**
	 * Get the Collection of IDs of NamedModels which are candidates for sampling.
	 * @return the list of choosable NamedModels
	 */
//	public Collection<String> getAvailableNamedModels();
	
	/**
	 * Set the Collection of IDs of NamedModel which are candidates for sampling.
	 * @param the list of choosable NamedModels
	 */
//	public void setAvailableNamedModels(Collection<String> availableNamedModels);
	
	/**
	 * Get the Collection of IDs of NamedModel which have been selected for 
	 * this sampling run.
	 * @return the list of selected NamedModels
	 */
	public Collection<String> getSelectedNamedModels();
	
	/**
	 * Set the Collection of IDs of NamedModel from which to extract data.
	 * @param the list of selected NamedModels
	 */
	public void setSelectedNamedModels(Collection<String> selectedNamedModels);
	
	//public void setDataColumns(DataColumn[] dataColumns);
	
	//public DataColumn[] getDataColumns();
	
//	public DataTable getResultDataTable();
//	
//	public void setResultDataTable(DataTable resultDataTable);

	//public Dictionary<?, ?> getProperties();
	
//	/**
//	 * Prepare this object for archiving.
//	 * Remove any attributes stored in this object, which are not of 
//	 * historical value.  This method is called before storing a ISampler object
//	 * after experimentation.  Examples of such attributes include
//	 * choosable data models,
//	 */
//	public void removeInterimData();

	@Id
	public String getId();
	
	public String getUri();
	
	//public void setProperties(Dictionary<?, ?> properties);
	
	public String getName();

	public void setName(String name);
	
	/**
	 * Execute this sampler in automated fashion
	 * @return
	 */
	public DataTable execute();
	
	public ISampler createClone();
}
