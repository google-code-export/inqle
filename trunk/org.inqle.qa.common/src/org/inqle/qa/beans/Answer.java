package org.inqle.qa.beans;

import org.inqle.qa.common.QAConstants;

import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.TypeWrapper;

@TargetModelName(QAConstants.DEFAULT_ANSWER_MODEL)
@Namespace(RDF.INQLE)
public class Answer extends org.inqle.qa.beans.Answer 
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
