package org.inqle.ui.rap.tree.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.ExternalDataset;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;
import org.inqle.ui.rap.actions.SDBDatabaseWizardAction;
import org.inqle.ui.rap.actions.DatasetWizardAction;
import org.inqle.ui.rap.actions.DeleteDatabaseAction;

public class DatabasePart extends PartType {
	
	private static final String ICON_PATH = "org/inqle/ui/rap/images/db.gif";
	
	private IDatabase database;
	private List<ModelPart> modelParts = new ArrayList<ModelPart>();
	
	static Logger log = Logger.getLogger(DatabasePart.class);

	private boolean childrenIntialized = false;

	private List<String> modelNames = new ArrayList<String>();
		
	private String getSparqlToFindChildDatasets() {
		String sparql = " PREFIX inqle: <" + RDF.INQLE + "> \n " + 
		" PREFIX xsd: <" + RDF.XSD + "> \n " + 
		" SELECT ?datasetId \n " +
		" { \n " +
		" GRAPH ?g { \n " +
		" ?datasetUri a inqle:ExternalDataset \n " +
		" . ?datasetUri inqle:id ?datasetId \n " +
		" . ?datasetUri inqle:connectionId \"" + database.getId() + "\"^^xsd:string \n" +
		//" . ?datasetUri inqle:connectionId " + literal + " \n " +
		//" . ?datasetUri inqle:connectionId ?anyConnectionId" +
		//" . ?datasetUri inqle:id \"dave_1\"^^http://www.w3.org/2001/XMLSchema#string " +
		" } }\n";
		return sparql;
	}
	
	public DatabasePart(IDatabase database) {
		this.database = database;
		//this.persister = persister;
		modelParts = new ArrayList<ModelPart>();
	}

	@Override
	public String getName() {
//		return database.getDbUser() + "@" + database.getDbURL();
		return database.getDisplayName();
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
		
		//query for all ExternalDataset children
		Persister persister = Persister.getInstance();
		
//		AppInfo appInfo = persister.getAppInfo();
//		QueryCriteria queryCriteria = new QueryCriteria();
//		queryCriteria.setQuery(getSparqlToFindChildDatasets());
//		log.trace("SPARQL=" + getSparqlToFindChildDatasets());
//		queryCriteria.addNamedModel(appInfo.getMetarepositoryDataset());
//		List<String> datasetIds = Queryer.selectSimpleList(queryCriteria, "datasetId");
//		log.trace("datasetIds=" + datasetIds);
		
//		SDBConnector dbConnector = new SDBConnector(getConnection());
//		List<Dataset> datasets = dbConnector.getDatasets();
//		log.trace("datasets=" + datasets);
		
		//for Dataset, add a ModelPart
		IDBConnector connector = DBConnectorFactory.getDBConnector(database);
		com.hp.hpl.jena.query.Dataset modelSet = connector.getDataset();
		if (modelSet==null) return;
		Iterator modelI = modelSet.listNames();
		modelParts = new ArrayList<ModelPart>();
//		for (String datasetId: datasetIds) {
		while (modelI.hasNext()) {
			String datasetId = (String)modelI.next();
			ExternalDataset dataset = (ExternalDataset)persister.reconstitute(ExternalDataset.class, datasetId, true);
			dataset.setConnectionId(this.database.getId());
//			log.info("DatabasePart Loaded ExternalDataset: " + JenabeanWriter.toString(dataset));
			ModelPart modelPart = new ModelPart(dataset);
			modelPart.setParent(this);
			//modelPart.setPersister(this.persister);
			modelParts.add(modelPart);
		}
		
		this.childrenIntialized = true;
	}
	
	@Override
//	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		List<IAction> actions = new ArrayList<IAction>();
		//"Add a dataset" action
		DatasetWizardAction newModelWizardAction = new DatasetWizardAction(DatasetWizardAction.MODE_NEW, "Add a dataset...", this, workbenchWindow);
		newModelWizardAction.setDataset(getNewDataset());
		actions.add(newModelWizardAction);
		
		//"Add an ontology dataset" action
//		DatasetWizardAction newOntologyDatasetWizardAction = new DatasetWizardAction(DatasetWizardAction.MODE_NEW, "Add an ontology dataset...", this, workbenchWindow);
//		newOntologyDatasetWizardAction.setDataset(getNewOntologyDataset());
//		actions.add(newOntologyDatasetWizardAction);
		
		//"Edit this database" action
		SDBDatabaseWizardAction editDatabaseWizardAction = new SDBDatabaseWizardAction(SDBDatabaseWizardAction.MODE_EDIT, "Edit this database...", this.getParent(), workbenchWindow);
		editDatabaseWizardAction.setDatabasePart(this);
		actions.add(editDatabaseWizardAction);
		
		//"Clone this database" action
		SDBDatabaseWizardAction cloneDatabaseWizardAction = new SDBDatabaseWizardAction(SDBDatabaseWizardAction.MODE_CLONE, "Clone this database...", this.getParent(), workbenchWindow);
		cloneDatabaseWizardAction.setDatabasePart(this);
		actions.add(cloneDatabaseWizardAction);
		
		//Delete action
		DeleteDatabaseAction deleteDatabaseAction = new DeleteDatabaseAction("Delete", this, workbenchWindow);
		actions.add(deleteDatabaseAction);
		
		return actions;
	}

	private ExternalDataset getNewDataset() {
		ExternalDataset newDataset = new ExternalDataset();
		newDataset.setConnectionId(this.getDatabase().getId());
		return newDataset.createClone();
	}
	
//	private OntologyDataset getNewOntologyDataset() {
//		OntologyDataset newDataset = new OntologyDataset();
//		newDataset.setConnectionId(this.getConnection().getId());
//		return newDataset.createClone();
//	}

	public IDatabase getDatabase() {
		return database;
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
		return database;
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
