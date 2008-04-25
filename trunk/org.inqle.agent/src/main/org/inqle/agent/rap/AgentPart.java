/**
 * 
 */
package org.inqle.agent.rap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.agent.IAgent;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Literal;

/**
 * @author David Donohue
 * Feb 26, 2008
 */
public class AgentPart extends PartType {
	//static int agentPartCount = 0;
	//int initCount = 0;

	protected IAgentFactory agentFactory;

	private static final String ICON_PATH = "org/inqle/agent/agent.jpeg";

	private List<CustomizedAgentPart> childParts = new ArrayList<CustomizedAgentPart>();
	
	static Logger log = Logger.getLogger(AgentPart.class);

	private boolean childrenIntialized = false;

	private IAgent agent;
	
	public AgentPart(IAgentFactory agentFactory) {
		//agentPartCount++;
		this.agentFactory = agentFactory;
		this.agent = agentFactory.getBaseAgent();
	}
	
	/**
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	@Override
	public String getName() {
		if (agent == null || agent.getName() == null) {
			return agentFactory.getName();
		}
		return agent.getName();
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}
	
	public IAgentFactory getAgentFactory() {
		return agentFactory;
	}
	
	public static final String SPARQL_BEGIN = 
		"PREFIX rdf: <" + RDF.RDF + ">\n" + 
		"PREFIX ja: <" + RDF.JA + ">\n" + 
		"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
		"SELECT ?id \n" +
		"{\n" +
		"GRAPH ?g {\n" +
		"?agentUri inqle:" + RDF.JENABEAN_ID_ATTRIBUTE + " ?id \n";
	
	public static final String SPARQL_END =
		"\n} }\n";
		
	private String getSparqlToFindChildren() {
		String sparql = SPARQL_BEGIN +
			" . ?agentUri a ?classUri\n" +
			" . ?classUri <" + RDF.JAVA_CLASS + "> \"" + agent.getClass().getName() + "\" \n" +
			SPARQL_END;
		return sparql;
	}
	
	@Override
	public IPart[] getChildren() {
		if (! this.childrenIntialized) {
			initChildren();
		}
		CustomizedAgentPart[] nullPart = {};
		if (childParts.size() == 0) {
			log.debug("No customizations found.");
			return nullPart;
		}
		return childParts.toArray(nullPart);
	}
	
	public void initChildren() {
		//initCount++;
		//log.info("Agent #" + agentPartCount + ": initChildren #" + initCount);
		//query for all RDBModel children
		AppInfo appInfo = persister.getAppInfo();
		QueryCriteria queryCriteria = new QueryCriteria(persister);
		queryCriteria.setQuery(getSparqlToFindChildren());
		queryCriteria.addNamedModel(appInfo.getRepositoryNamedModel());
		RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
		
		//for each item in resultTable, add a ModelPart
		childParts = new ArrayList<CustomizedAgentPart>();
		for (QuerySolution row: resultTable.getResultList()) {
			Literal idLiteral = row.getLiteral("id");
			log.debug("Reconstituting Agent of class " + agent.getClass() + ": " + idLiteral.getLexicalForm());
			IAgent childAgent = (IAgent)Persister.reconstitute(agent.getClass(), idLiteral.getLexicalForm(), persister.getMetarepositoryModel(), true);
			
			IAgentFactory childAgentFactory = agentFactory.cloneFactory(childAgent);
			CustomizedAgentPart part = new CustomizedAgentPart(childAgentFactory);
			part.setParent(this);
			part.setPersister(persister);
			part.addListener(listener);
			childParts.add(part);
		}
		this.childrenIntialized = true;
	}
	
	@Override
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
		//"Run this wizard" action
		//IAgent replicaOfAgent = agentFactory.replicateAgent();
//		AgentWizardAction runAgentWizardAction = new AgentWizardAction(AgentWizardAction.MODE_RUN, "Run this agent", this, workbenchWindow, persister);
//		///runAgentWizardAction.setAgent(replicaOfAgent);
//		manager.add(runAgentWizardAction);
		
		if (!agentFactory.hasWizard()) {
			return;
		}
		
		//Delete action
//		DeleteAgentAction deleteAgentAction = new DeleteAgentAction("Delete", this, workbenchWindow, this.persister);
//		manager.add(deleteAgentAction);
		
		//"Clone this Agent" action.  This wizard works with a clone of the base agent
		IAgent cloneOfAgent = (IAgent)agentFactory.getBaseAgent().createClone();
		AgentWizardAction cloneAgentWizardAction = new AgentWizardAction(AgentWizardAction.MODE_CLONE, "Clone this agent", this, workbenchWindow, persister);
		cloneAgentWizardAction.setAgent(cloneOfAgent); 
		manager.add(cloneAgentWizardAction);
	}

}
