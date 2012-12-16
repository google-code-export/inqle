package org.inqle.data.sampling;

/**
 * @author David Donohue
 * Jan 21, 2008
 */
@Deprecated
public interface ISamplerDecider {

	public ISampler decide();
	
	public void addSampler(ISampler des);
	
	public void removeSampler(ISampler des);
	
	public int countSamplers();
}
