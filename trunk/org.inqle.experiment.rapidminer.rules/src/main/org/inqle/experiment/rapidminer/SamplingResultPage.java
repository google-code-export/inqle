package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.IDataTable;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.rap.DataTablePage;

/**
 * This wizard page is designed to show the resulting table of data from sampling,
 * and include a button to run the sampler.
 * @author David Donohue
 * Apr 10, 2008
 */
public class SamplingResultPage extends DataTablePage {

	private IDataTable dataTable;

	static Logger log = Logger.getLogger(SamplingResultPage.class);

	public SamplingResultPage(LearningCycle learningCycle, String title) {
		super(learningCycle, null, null, title, null);
	}

	@Override
	public void onEnterPageFromPrevious() {
		clearTableData();
		//this.getShell().redraw();
		ISampler origSampler = ((LearningCycle)bean).getSampler();
		if (origSampler == null) {
			return;
		}
		ISampler sampler = (ISampler) origSampler.createReplica();
		//ISampler sampler = ((LearningCycle)bean).getSampler();
		//log.info("onEnterPage(): sampler=" + JenabeanWriter.toString(sampler));
		this.dataTable = sampler.execute();
		if (dataTable == null) {
			log.info("Data table is null.  Adding no rows to table.");
			return;
		}
		
		//log.info("resultDataTable.getRdfTable()=" + RdfTableWriter.dataTableToString(dataTable.getRdfTable()));
//		setRdfTable(dataTable.getRdfTable());
		setDataTable(dataTable);
		List<String> columnNames = new ArrayList<String>();
		for (Arc arc: dataTable.getColumns()) {
//			columnNames.add(arc.toString());
			columnNames.add(arc.getQnameRepresentation());
		}
		log.info("Adding column names: " + columnNames);
		setPropertyNames(columnNames);
		refreshTableData();
	}
	
	/**
	 * Prevent the back button from being pressed.  
	 * Due to a limitation of RAP, we cannot refresh the table with new info.
	 */
//	@Override
//	public boolean onPreviousPage() {
//		this.setMessage("Due to RAP limitation, we are unable to go back.  You may instead close and restart this wizard.");
//		return false;
//	}

	public IDataTable getDataTable() {
		return dataTable;
	}
}
