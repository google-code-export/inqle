package org.inqle.ui.rap.tree.parts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RDBModel;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;
import org.inqle.ui.rap.actions.DatabaseWizardAction;
import org.inqle.ui.rap.actions.DeleteDatabaseAction;
import org.inqle.ui.rap.actions.ModelWizardAction;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Literal;

public class DatabasePart extends PartType {
	
	private static final String ICON_PATH = "org/inqle/ui/rap/images/db.gif";
	
	private Connection connection;
	private List<ModelPart> modelParts = new ArrayList<ModelPart>();
	
	static Logger log = Logger.getLogger(DatabasePart.class);

	private boolean childrenIntialized = false;

	private List<String> modelNames = new ArrayList<String>();
	
	public static final String SPARQL_BEGIN = 
		"PREFIX rdf: <" + RDF.RDF + ">\n" + 
		"PREFIX ja: <" + RDF.JA + ">\n" + 
		"SELECT ?modelId \n" +
		"{\n" +
		"GRAPH ?g {\n" +
		"?modelUri a ja:RDBModel \n" +
		//" . ?modelUri ja:modelName ?modelName\n" +
		" . ?modelUri ja:" + RDF.JENABEAN_ID_ATTRIBUTE + " ?modelId\n";
	
	public static final String SPARQL_END =
		"\n} }\n";
		
	private String getSparqlToFindChildRDBModels() {
		String sparql = SPARQL_BEGIN;
		sparql += " . ?modelUri ja:connection <" + connection.getUri() + ">";
		sparql += SPARQL_END;
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
		
		//query for all RDBModel children
		Persister persister = Persister.getInstance();
		AppInfo appInfo = persister.getAppInfo();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(getSparqlToFindChildRDBModels());
		queryCriteria.addNamedModel(appInfo.getRepositoryNamedModel());
		RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
		
		//for each item in resultTable, add a ModelPart
		modelParts = new ArrayList<ModelPart>();
		for (QuerySolution row: resultTable.getResultList()) {
			Literal modelId = row.getLiteral("modelId");
			RDBModel rdbModel = (RDBModel)Persister.reconstitute(RDBModel.class, modelId.getLexicalForm(), persister.getMetarepositoryModel(), false);
			rdbModel.setConnection(this.connection);
			ModelPart modelPart = new ModelPart(rdbModel);
			modelPart.setParent(this);
			//modelPart.setPersister(this.persister);
			modelParts.add(modelPart);
		}
		this.childrenIntialized = true;
	}
	
	@Override
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
		//"Add a dataset" action
		ModelWizardAction newModelWizardAction = new ModelWizardAction(ModelWizardAction.MODE_NEW, "Add a dataset...", this, workbenchWindow);
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
This retrieves all RDBModel objects
Collection<?> modelObjects = persister.reconstituteList(RDBModel.class);
for (Object modelObject: modelObjects) {
	RDBModel rdbModel = (RDBModel)modelObject;
	ModelPart modelPart = new ModelPart(rdbModel, persister);
	modelPart.addListener(this.listener);
	modelPart.setParent(this);
	modelParts.add(modelPart);
	modelNames.add(modelPart.getName());
}
*/
