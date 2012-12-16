package org.inqle.ui.rap.wiki.parts;

import org.inqle.data.rdf.jena.Datamodel;

public class DatamodelPage {

	private Datamodel datamodel;

	public DatamodelPage(Datamodel datamodel) {
		this.datamodel = datamodel;
	}

	public String toString() {
		return String.valueOf(datamodel);
	}
}
