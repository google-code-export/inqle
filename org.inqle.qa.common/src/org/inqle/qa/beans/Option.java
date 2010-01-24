package org.inqle.qa.beans;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.IUniqueJenabean;
import org.inqle.data.rdf.jenabean.TargetDatabaseId;
import org.inqle.data.rdf.jenabean.TargetModelName;
import org.inqle.qa.common.QAConstants;

import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.TypeWrapper;

@TargetDatabaseId(QAConstants.QA_DATABASE)
@TargetModelName(QAConstants.DEFAULT_QUESTION_MODEL)
@Namespace(RDF.INQLE)
public class Option extends org.inqle.qa.beans.Option 
implements IUniqueJenabean{

	private static final long serialVersionUID = 4758619924199193098L;
	
	@Id
	public String getId() {
		return super.getId();
	}
	
	public String getUri() {
		return TypeWrapper.instanceURI(this);
	}

	@RdfProperty(RDF.DESCRIPTION_PREDICATE)
	public String getDescription() {
		return super.getDescription();
	}

	@RdfProperty(RDF.NAME_PREDICATE)
	public String getName() {
		return super.getName();
	}

}
