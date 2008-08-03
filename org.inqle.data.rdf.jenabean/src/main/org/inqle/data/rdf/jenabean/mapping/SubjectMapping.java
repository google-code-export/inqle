package org.inqle.data.rdf.jenabean.mapping;

import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

public class SubjectMapping extends GlobalJenabean {

	public String getStringRepresentation() {
		
	}

	public void clone(SubjectMapping objectToBeCloned) {
		setMapsHeader(objectToBeCloned.getMapsHeader());
		setMapsPredicate(objectToBeCloned.getMapsPredicate());
		setMapsSubjectClass(objectToBeCloned.getMapsSubjectClass());
		setMapsSubjectInstance(objectToBeCloned.getMapsSubjectInstance());
		setMapsDataAboutSubjectClass(objectToBeCloned.getMapsDataAboutSubjectClass());
		setMapsDataAboutSubjectInstance(objectToBeCloned.getMapsDataAboutSubjectInstance());
		super.clone(objectToBeCloned);
	}
	
	public SubjectMapping createClone() {
		
	}

}
