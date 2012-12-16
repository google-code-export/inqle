package org.inqle.rdf.services.server;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.document.Document;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.larq.ARQLuceneException;
import com.hp.hpl.jena.query.larq.IndexBuilderModel;
import com.hp.hpl.jena.query.larq.LARQ;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.ARQNotImplemented;


/**
 * This IndexBuilder is designed to be used when storing a single Jenabean.  
 * Make sure to 
 * @author gd9345
 *
 */
public class JenabeanIndexBuilder extends IndexBuilderModel {

	private String subjectString;
	private IndexWriter jenabeanIndexWriter;

	public JenabeanIndexBuilder(IndexWriter indexWriter, String subjectString) {
        super(indexWriter);
        this.subjectString = subjectString;
        this.jenabeanIndexWriter = indexWriter;
    }
	
//	@Override
//    public void unindexStatement(Statement s) {
//		Resource subjectResource = ResourceFactory.createResource(subjectString);
//        Node subject = subjectResource.asNode() ;
//        
//		if ( ! s.getObject().isLiteral() ||
//                ! LARQ.isString(s.getLiteral()) )
//               return ;
//           
//           Node object  = s.getObject().asNode() ;
////           IndexReader reader;
////           LARQ.
////           reader.
//          
//           
//           jenabeanIndexWriter.deleteDocuments(term);
////           index.(subject, object.getLiteralLexicalForm()) ;
//	}
	
	@Override
	public void indexStatement(Statement s) {
		try {
			Resource subjectResource = ResourceFactory.createResource(subjectString);
            Node subject = subjectResource.asNode() ;

            if ( ! s.getObject().isLiteral() ||
                 ! LARQ.isString(s.getLiteral()) )
                return ;
            
            Node object  = s.getObject().asNode() ;
            
            // Note: if a subject occurs twice with an indexable string,
            // there will be two hits later.
            index.index(subject, object.getLiteralLexicalForm()) ;
        } catch (Exception e)
        { throw new ARQLuceneException("indexStatement", e) ; }
		
	}
	
	/**
	 * This is not a lARQ method.  Rather, it can be called once upon removing a Jenabean from a model.
	 * This method removes the Jenabean fromt he index
	 */
//	public void unindexJenabean() {
//		Document doc = new Document();
//		Term term = new Term();
//		jenabeanIndexWriter.deleteDocuments(term);
//	}

}
