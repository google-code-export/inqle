/**
 * 
 */
package org.inqle.data.sampling.rap;

import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.sampling.ISampler;
import org.inqle.ui.rap.table.SparqlSelectorPage;

/**
 * @author David Donohue
 * Mar 21, 2008
 */
public class SamplerBackedSparqlSelectorPage extends SparqlSelectorPage {

	private String query;

	public SamplerBackedSparqlSelectorPage(IBasicJenabean modelBean,
			String modelBeanValueId, Class<?> modelListClass, String title,
			ImageDescriptor titleImage) {
		super(modelBean, modelBeanValueId, modelListClass, title, titleImage);
		assert(bean instanceof ISampler);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.table.SparqlSelectorPage#getNamedModelIds()
	 */
	@Override
	protected Collection<String> getNamedModelIds() {
		ISampler sampler = (ISampler)bean;
		return sampler.getSelectedNamedModels();
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.table.SparqlSelectorPage#getQuery()
	 */
	@Override
	protected String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
