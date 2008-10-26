package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.DataColumn;
import org.inqle.data.sampling.IDataTable;
import org.inqle.data.sampling.IDataTableProvider;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.rap.DataTablePage;
import org.inqle.ui.rap.table.RdfTablePage;

/**
 * This wizard page is designed to show the resulting table of data from sampling,
 * and include a button to run the sampler.
 * @author David Donohue
 * Apr 10, 2008
 */
public class SamplingResultPage extends DataTablePage implements IDataTableProvider {

	private Persister persister;

	private IDataTable dataTable;

	static Logger log = Logger.getLogger(SamplingResultPage.class);
	
//	@Deprecated
//	public SamplingResultPage(Object modelBean, String modelBeanValueId,
//			Class<?> modelListClass, String title, ImageDescriptor titleImage) {
//		super(modelBean, modelBeanValueId, modelListClass, title, titleImage);
//	}

	public SamplingResultPage(LearningCycle learningCycle, String title) {
		super(learningCycle, null, null, title, null);
	}

	@Override
	public void onEnterPageFromPrevious() {
		//this.getShell().redraw();
		ISampler origSampler = ((LearningCycle)bean).getSampler();
		if (origSampler == null) {
			return;
		}
		ISampler sampler = (ISampler) origSampler.createReplica();
		//ISampler sampler = ((LearningCycle)bean).getSampler();
		//log.info("onEnterPage(): sampler=" + JenabeanWriter.toString(sampler));
		this.dataTable = sampler.execute();
		//log.info("resultDataTable.getRdfTable()=" + RdfTableWriter.dataTableToString(dataTable.getRdfTable()));
		setRdfTable(dataTable.getRdfTable());
		List<String> columnNames = new ArrayList<String>();
		for (Arc arc: dataTable.getColumns()) {
			columnNames.add(arc.toString());
		}
		
		setPropertyNames(columnNames);
		refreshTableData();
		//getShell().pack();
	}
	
	/**
	 * Prevent the back button from being pressed.  
	 * Due to a limitation of RAP, we cannot refresh the table with new info.
	 */
	@Override
	public boolean onPreviousPage() {
		this.setMessage("Due to RAP limitation, we are unable to go back.  You may instead close and restart this wizard.");
		return false;
	}
	
	public void setPersister(Persister persister) {
		this.persister = persister;
	}

	public DataTable getDataTable() {
		return dataTable;
	}
}
