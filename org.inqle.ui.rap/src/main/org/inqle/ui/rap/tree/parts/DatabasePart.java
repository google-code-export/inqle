package org.inqle.ui.rap.tree.parts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.PurposefulDatamodel;
import org.inqle.data.rdf.jena.SystemDatamodel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.rdf.RDF;
import org.inqle.rdf.beans.util.BeanTool;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;
import org.inqle.ui.rap.actions.DatamodelWizardAction;

public class DatabasePart extends PartType {
	
	private static final String ICON_PATH = "org/inqle/ui/rap/images/db.gif";
	
	private IDatabase database;
	private List<ModelPart> modelParts = new ArrayList<ModelPart>();
	
	private static Logger log = Logger.getLogger(DatabasePart.class);

	private boolean childrenIntialized = false;

	private List<String> modelNames = new ArrayList<String>();
		
//	private String getSparqlToFindChildDatasets() {
//		String sparql = " PREFIX inqle: <" + RDF.INQLE + "> \n " + 
//		" PREFIX xsd: <" + RDF.XSD + "> \n " + 
//		" SELECT ?datamodelId \n " +
//		" { \n " +
//		" GRAPH ?g { \n " +
//		" ?datamodelUri a inqle:UserDatamodel \n " +
//		" . ?datamodelUri inqle:id ?datamodelId \n " +
//		" . ?datamodelUri inqle:connectionId \"" + database.getId() + "\"^^xsd:string \n" +
//		" } }\n";
//		return sparql;
//	}
	
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
		
		//query for all UserDatamodel children
		Persister persister = Persister.getInstance();
		
//		AppInfo appInfo = persister.getAppInfo();
//		QueryCriteria queryCriteria = new QueryCriteria();
//		queryCriteria.setQuery(getSparqlToFindChildDatasets());
//		log.trace("SPARQL=" + getSparqlToFindChildDatasets());
//		queryCriteria.addNamedModel(appInfo.getMetarepositoryDataset());
//		List<String> datamodelIds = Queryer.selectSimpleList(queryCriteria, "datamodelId");
//		log.trace("datamodelIds=" + datamodelIds);
		
//		SDBConnector dbConnector = new SDBConnector(getConnection());
//		List<Datamodel> datamodels = dbConnector.getDatasets();
//		log.trace("datamodels=" + datamodels);
		
		//for Datamodel, add a ModelPart
		IDBConnector connector = DBConnectorFactory.getDBConnector(database.getId());
		List<String> modelNames = connector.listModelNames(RDF.SUBDATABASE_DATA);
		for (String modelName: modelNames) {
			String modelId = database.getId() + "/" + RDF.SUBDATABASE_DATA + "/" + modelName;
			PurposefulDatamodel datamodel = persister.getDatabaseBackedDatamodel(PurposefulDatamodel.class, modelId);
			if (datamodel==null) {
				log.warn("Model of ID " + modelId + " does not exist but should.");
				continue;
			}
			ModelPart modelPart = new ModelPart(datamodel);
			modelPart.setParent(this);
			modelPart.addListener(this.listener);
			modelParts.add(modelPart);
		}
		
		modelNames = connector.listModelNames(RDF.SUBDATABASE_SYSTEM);
		for (String modelName: modelNames) {
//			String modelId = database.getId() + "/" + IDBConnector.SUBDATABASE_SYSTEM + "/"  + modelName;
			SystemDatamodel datamodel = new SystemDatamodel();
			datamodel.setName(modelName);
			datamodel.setDatabaseId(database.getId());
			ModelPart modelPart = new ModelPart(datamodel);
			modelPart.setParent(this);
			modelPart.addListener(this.listener);
			modelParts.add(modelPart);
		}
		
//		com.hp.hpl.jena.query.Dataset modelSet = connector.getDataset();
//		if (modelSet==null) return;
//		Iterator modelI = modelSet.listNames();
//		modelParts = new ArrayList<ModelPart>();
//		while (modelI.hasNext()) {
//			String datamodelId = (String)modelI.next();
//			UserDatamodel datamodel = (UserDatamodel)persister.reconstitute(UserDatamodel.class, datamodelId, true);
//			datamodel.setConnectionId(this.database.getId());
////			log.info("DatabasePart Loaded UserDatamodel: " + JenabeanWriter.toString(datamodel));
//			ModelPart modelPart = new ModelPart(datamodel);
//			modelPart.setParent(this);
//			//modelPart.setPersister(this.persister);
//			modelParts.add(modelPart);
//		}
		
		this.childrenIntialized = true;
	}
	
	@Override
//	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		List<IAction> actions = new ArrayList<IAction>();
		//"Create a datamodel" action
		DatamodelWizardAction newModelWizardAction = new DatamodelWizardAction(DatamodelWizardAction.MODE_NEW, "Create new datamodel...", this, workbenchWindow);
		PurposefulDatamodel newDatamodel = getNewDatamodel();
		log.info("Created new datamodel: " + newDatamodel.getId());
		newModelWizardAction.setDatamodel(getNewDatamodel());
		actions.add(newModelWizardAction);
		
		//"Edit this database" action
//		DatabaseWizardAction editDatabaseWizardAction = new DatabaseWizardAction(DatabaseWizardAction.MODE_EDIT, "Edit this database...", getDatabase(), this.getParent(), workbenchWindow);
//		editDatabaseWizardAction.setDatabasePart(this);
//		actions.add(editDatabaseWizardAction);
//		
//		//"Clone this database" action
//		DatabaseWizardAction cloneDatabaseWizardAction = new DatabaseWizardAction(DatabaseWizardAction.MODE_CLONE, "Clone this database...", getDatabase(), this.getParent(), workbenchWindow);
//		cloneDatabaseWizardAction.setDatabasePart(this);
//		actions.add(cloneDatabaseWizardAction);
//		
//		//Delete action
//		DeleteDatabaseAction deleteDatabaseAction = new DeleteDatabaseAction("Delete", this, workbenchWindow);
//		actions.add(deleteDatabaseAction);
		
		return actions;
	}

	private PurposefulDatamodel getNewDatamodel() {
		PurposefulDatamodel newDatamodel = new PurposefulDatamodel();
		newDatamodel.setDatabaseId(this.getDatabase().getId());
//		return newDataset.createClone();
		return BeanTool.clone(newDatamodel);
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
This retrieves all Datamodel objects
Collection<?> modelObjects = persister.reconstituteList(Datamodel.class);
for (Object modelObject: modelObjects) {
	Datamodel rdbModel = (Datamodel)modelObject;
	ModelPart modelPart = new ModelPart(rdbModel, persister);
	modelPart.addListener(this.listener);
	modelPart.setParent(this);
	modelParts.add(modelPart);
	modelNames.add(modelPart.getName());
}
*/
