package org.inqle.experiment.rapidminer;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.ui.rap.pages.IRadioBeanProvider;

public class SamplerBeanProvider implements IRadioBeanProvider {

	private LearningCycle learningCycle;

	private static Logger log = Logger.getLogger(SamplerBeanProvider.class);
	
	public Object getValue() {
		//log.info("SamplerBeanProvider.getValue() from:" + JenabeanWriter.toString(experimenterAgent));
		return learningCycle.getSamplerMode();
	}

	public void setBean(Object bean) {
		this.learningCycle = (LearningCycle) bean;
	}

	public void setValue(Object value) {
		int intVal = ((Integer)value).intValue();
		learningCycle.setSamplerMode(intVal);
	}

}
