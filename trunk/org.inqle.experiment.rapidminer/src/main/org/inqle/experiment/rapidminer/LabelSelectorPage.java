/**
 * 
 */
package org.inqle.experiment.rapidminer;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.sampling.DataColumn;
import org.inqle.data.sampling.DataTable;
import org.inqle.data.sampling.IDataTableProvider;
import org.inqle.ui.rap.actions.DynaWizardPage;
import org.inqle.ui.rap.actions.ListSelectorPage;

/**
 * @author David Donohue
 * Apr 22, 2008
 */
public class LabelSelectorPage extends ListSelectorPage {

	private IDataTableProvider dataTableProvider;
	
	private static Logger log = Logger.getLogger(LabelSelectorPage.class);
	
	public LabelSelectorPage(IBasicJenabean bean, String beanValueId, String title,
			ImageDescriptor titleImage) {
		super(bean, beanValueId, title, titleImage);
		assert(bean instanceof LearningCycle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void addElements(Composite composite) {
		this.composite = composite;
		assert(composite != null);
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		log.info("onEnterPageFromPrevious() called");
		DataTable dataTable = dataTableProvider.getDataTable();
		log.info("dataTable=" + dataTable);
		log.info("dataTable.getLearnableColumns()=" + dataTable.getLearnableColumns());
		DataColumn[] nullArray = new DataColumn[]{};
		setListItems(dataTable.getLearnableColumns().toArray(nullArray));
		log.info("Created list of learnable columns: " + dataTable.getLearnableColumns());
		createList();
		super.onEnterPageFromPrevious();
	}

	public IDataTableProvider getDataTableProvider() {
		return dataTableProvider;
	}

	public void setDataTableProvider(IDataTableProvider dataTableProvider) {
		this.dataTableProvider = dataTableProvider;
	}

}
