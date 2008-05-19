package org.inqle.experiment.rapidminer.agent;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.ui.rap.pages.IRadioBeanProvider;

public class ExperimenterBeanProvider implements IRadioBeanProvider {

	private ExperimenterAgent experimenterAgent;

	private static Logger log = Logger.getLogger(ExperimenterBeanProvider.class);
	
	public Object getValue() {
		//log.info("SamplerBeanProvider.getValue() from:" + JenabeanWriter.toString(experimenterAgent));
		return experimenterAgent.getLearningCycleMode();
	}

	public void setBean(Object bean) {
		this.experimenterAgent = (ExperimenterAgent) bean;
	}

	public void setValue(Object value) {
		int intVal = ((Integer)value).intValue();
		experimenterAgent.setLearningCycleMode(intVal);
	}

}
