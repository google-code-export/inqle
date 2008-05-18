/**
 * 
 */
package org.inqle.data.sampling.rap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;

/**
 * @author David Donohue
 * Feb 26, 2008
 */
public class SamplerPart extends PartType {
	//static int samplerPartCount = 0;
	//int initCount = 0;

	protected ISamplerFactory samplerFactory;

	private static final String ICON_PATH = "org/inqle/data/sampling/rap/images/sampler.jpeg";

	private List<CustomizedSamplerPart> childParts = new ArrayList<CustomizedSamplerPart>();
	
	static Logger log = Logger.getLogger(SamplerPart.class);

	private boolean childrenIntialized = false;

	private ISampler sampler;
	
	public SamplerPart(ISamplerFactory samplerFactory) {
		//samplerPartCount++;
		this.samplerFactory = samplerFactory;
		this.sampler = samplerFactory.getBaseSampler();
	}
	
	/**
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	@Override
	public String getName() {
		if (sampler == null || sampler.getName() == null) {
			return samplerFactory.getName();
		}
		return sampler.getName();
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}
	
	public ISamplerFactory getSamplerFactory() {
		return samplerFactory;
	}
	
//	public static final String SPARQL_BEGIN = 
//		"PREFIX rdf: <" + RDF.RDF + ">\n" + 
//		"PREFIX ja: <" + RDF.JA + ">\n" + 
//		"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
//		"SELECT ?id \n" +
//		"{\n" +
//		"GRAPH ?g {\n" +
//		"?samplerUri inqle:" + RDF.JENABEAN_ID_ATTRIBUTE + " ?id \n";
//	
//	public static final String SPARQL_END =
//		"\n} }\n";
//		
//	private String getSparqlToFindChildren() {
//		String sparql = SPARQL_BEGIN +
//			" . ?samplerUri a ?classUri\n" +
//			" . ?classUri <" + RDF.JAVA_CLASS + "> \"" + sampler.getClass().getName() + "\" \n" +
//			SPARQL_END;
//		return sparql;
//	}
	
	@Override
	public IPart[] getChildren() {
		if (! this.childrenIntialized) {
			initChildren();
		}
		CustomizedSamplerPart[] nullPart = {};
		if (childParts.size() == 0) {
			log.debug("No customizations found.");
			return nullPart;
		}
		return childParts.toArray(nullPart);
	}
	
	public void initChildren() {
		//initCount++;
		//log.info("Sampler #" + samplerPartCount + ": initChildren #" + initCount);
		//query for all RDBModel children
//		AppInfo appInfo = persister.getAppInfo();
//		QueryCriteria queryCriteria = new QueryCriteria(persister);
//		queryCriteria.setQuery(getSparqlToFindChildren());
//		queryCriteria.addNamedModel(appInfo.getRepositoryNamedModel());
//		RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
//		
//		//for each item in resultTable, add a ModelPart
//		childParts = new ArrayList<CustomizedSamplerPart>();
//		for (QuerySolution row: resultTable.getResultList()) {
//			Literal idLiteral = row.getLiteral("id");
//			log.debug("Reconstituting Sampler of class " + sampler.getClass() + ": " + idLiteral.getLexicalForm());
//			ISampler childSampler = (ISampler)Persister.reconstitute(sampler.getClass(), idLiteral.getLexicalForm(), persister.getMetarepositoryModel(), true);
			
		childParts = new ArrayList<CustomizedSamplerPart>();
		for (ISampler childSampler: SamplerLister.listCustomSamplers(sampler)) {
			ISamplerFactory childSamplerFactory = samplerFactory.cloneFactory();
			childSamplerFactory.setBaseSampler(childSampler);
			CustomizedSamplerPart part = new CustomizedSamplerPart(childSamplerFactory);
			part.setParent(this);
			//part.setPersister(persister);
			part.addListener(listener);
			childParts.add(part);
		}
		this.childrenIntialized = true;
	}
	
	@Override
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
		//"Run this wizard" action
		//ISampler replicaOfSampler = samplerFactory.replicateSampler();
//		SamplerWizardAction runSamplerWizardAction = new SamplerWizardAction(SamplerWizardAction.MODE_RUN, "Run this sampler", this, workbenchWindow, persister);
//		///runSamplerWizardAction.setSampler(replicaOfSampler);
//		manager.add(runSamplerWizardAction);
		
		if (!samplerFactory.hasWizard()) {
			return;
		}
		
		//Delete action
//		DeleteSamplerAction deleteSamplerAction = new DeleteSamplerAction("Delete", this, workbenchWindow, this.persister);
//		manager.add(deleteSamplerAction);
		
		//"Clone this Sampler" action.  This wizard works with a clone of the base sampler
		//ISampler cloneOfSampler = samplerFactory.cloneSampler();
		ISampler cloneOfSampler = samplerFactory.getBaseSampler().createClone();
		SamplerWizardAction cloneSamplerWizardAction = new SamplerWizardAction(SamplerWizardAction.MODE_CLONE, "Clone this sampler", this, workbenchWindow);
		cloneSamplerWizardAction.setSampler(cloneOfSampler); 
		manager.add(cloneSamplerWizardAction);
	}

	@Override
	public Object getObject() {
		return samplerFactory.getBaseSampler();
	}

}
