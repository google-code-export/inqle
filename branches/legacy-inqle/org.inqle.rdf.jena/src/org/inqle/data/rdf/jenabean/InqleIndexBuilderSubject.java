package org.inqle.data.rdf.jenabean;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.query.larq.IndexBuilderSubject;
import com.hp.hpl.jena.rdf.model.Statement;

@Deprecated
public class InqleIndexBuilderSubject extends IndexBuilderSubject {

	private static Logger log = Logger.getLogger(InqleIndexBuilderSubject.class);
	
	@Override
	public void indexStatements(com.hp.hpl.jena.rdf.model.StmtIterator sIter) {
		log.info("Indexing statements...");
		super.indexStatements(sIter);
	}
	
	@Override
	public void addedStatement(Statement statement) {
		log.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAdding statement to LARQ index:" + statement + "...");
		super.addedStatement(statement);
	}
}
