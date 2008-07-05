package org.inqle.ui.rap.tree.parts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.ExternalDataset;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;
import org.inqle.ui.rap.actions.DatabaseWizardAction;
import org.inqle.ui.rap.actions.DatasetWizardAction;
import org.inqle.ui.rap.actions.DeleteDatabaseAction;

public class DatabasePart extends PartType {
	
	private static final String ICON_PATH = "org/inqle/ui/rap/images/db.gif";
	
	private Connection connection;
	private List<ModelPart> modelParts = new ArrayList<ModelPart>();
	
	static Logger log = Logger.getLogger(DatabasePart.class);

	private boolean childrenIntialized = false;

	private List<String> modelNames = new ArrayList<String>();
		
	private String getSparqlToFindChildDatasets() {
		//Literal literal = ResourceFactory.createTypedLiteral(connection.getId());
		String sparql = " PREFIX inqle: <" + RDF.INQLE + "> \n " + 
		" PREFIX xsd: <" + RDF.XSD + "> \n " + 
		" SELECT ?datasetId \n " +
		" { \n " +
		" GRAPH ?g { \n " +
		" ?datasetUri a inqle:Dataset \n " +
		" . ?datasetUri inqle:id ?datasetId \n " +
		" . ?datasetUri inqle:connectionId \"" + connection.getId() + "\"^^xsd:string \n" +
		//" . ?datasetUri inqle:connectionId " + literal + " \n " +
		//" . ?datasetUri inqle:connectionId ?anyConnectionId" +
		//" . ?datasetUri inqle:id \"dave_1\"^^http://www.w3.org/2001/XMLSchema#string " +
		" } }\n";
		return sparql;
	}
	
	public DatabasePart(Connection connection) {
		this.connection = connection;
		//this.persister = persister;
		modelParts = new ArrayList<ModelPart>();
	}

	@Override
	public String getName() {
		return connection.getDbUser() + "@" + connection.getDbURL();
	}
	
	@Override
	public IPart[] getChildren() {
		if (! this.childrenIntialized) {
			initChildren();
		}
		ModelPart[] nullPart = {};
		if (modelParts.size() == 0) {
			log.debug("No Model objects found.");
			return nullPart;
		}
		return modelParts.toArray(nullPart);
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}
	
	public void initChildren() {
		
		//query for all Dataset children
		Persister persister = Persister.getInstance();
		AppInfo appInfo = persister.getAppInfo();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(getSparqlToFindChildDatasets());
		log.info("SPARQL=" + getSparqlToFindChildDatasets());
		queryCriteria.addNamedModel(appInfo.getMetarepositoryDataset());
		//RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
		List<String> datasetIds = Queryer.selectSimpleList(queryCriteria, "datasetId");
		log.info("datasetIds=" + datasetIds);
//		DBConnector dbConnector = new DBConnector(getConnection());
//		List<Dataset> datasets = dbConnector.getDatasets();
//		log.info("datasets=" + datasets);
		
		//for each item in resultTable, add a ModelPart
		modelParts = new ArrayList<ModelPart>();
		//for (QuerySolution row: resultTable.getResultList()) {
//			Literal modelId = row.getLiteral("modelId");
		for (String datasetId: datasetIds) {
			ExternalDataset dataset = (ExternalDataset)persister.reconstitute(ExternalDataset.class, datasetId, false);
			dataset.setConnectionId(this.connection.getId());
			ModelPart modelPart = new ModelPart(dataset);
			modelPart.setParent(this);
			//modelPart.setPersister(this.persister);
			modelParts.add(modelPart);
		}
		
		this.childrenIntialized = true;
	}
	
	@Override
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
		//"Add a dataset" action
		DatasetWizardAction newModelWizardAction = new DatasetWizardAction(DatasetWizardAction.MODE_NEW, "Add a dataset...", this, workbenchWindow);
		newModelWizardAction.setDataset(getNewDataset());
		manager.add(newModelWizardAction);
		
		//"Edit this database" action
		DatabaseWizardAction editDatabaseWizardAction = new DatabaseWizardAction(DatabaseWizardAction.MODE_EDIT, "Edit this database...", this.getParent(), workbenchWindow);
		editDatabaseWizardAction.setDatabasePart(this);
		manager.add(editDatabaseWizardAction);
		
		//"Clone this database" action
		DatabaseWizardAction cloneDatabaseWizardAction = new DatabaseWizardAction(DatabaseWizardAction.MODE_CLONE, "Clone this database...", this.getParent(), workbenchWindow);
		cloneDatabaseWizardAction.setDatabasePart(this);
		manager.add(cloneDatabaseWizardAction);
		
		//Delete action
		DeleteDatabaseAction deleteDatabaseAction = new DeleteDatabaseAction("Delete", this, workbenchWindow);
		manager.add(deleteDatabaseAction);
	}

	private ExternalDataset getNewDataset() {
		ExternalDataset newDataset = new ExternalDataset();
		newDataset.setConnectionId(this.getConnection().getId());
		return newDataset.createClone();
	}

	public Connection getConnection() {
		return this.connection;
	}

	/**
	 * tests to see if the database has a model of this name
	 * @param modelName
	 * @return
	 */
	public boolean hasModelNamed(String modelName) {
		return modelNames.contains(modelName);
	}

	@Override
	public Object getObject() {
		return connection;
	}
}

/*
This retrieves all Dataset objects
Collection<?> modelObjects = persister.reconstituteList(Dataset.class);
for (Object modelObject: modelObjects) {
	Dataset rdbModel = (Dataset)modelObject;
	ModelPart modelPart = new ModelPart(rdbModel, persister);
	modelPart.addListener(this.listener);
	modelPart.setParent(this);
	modelParts.add(modelPart);
	modelNames.add(modelPart.getName());
}
*/
