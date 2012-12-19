package org.inqle.qs.bean;

import static org.inqle.qs.Constants.NS_DATUM;

import java.util.Date;

import org.openrdf.annotations.Iri;
import org.openrdf.model.URI;

@Iri(NS_DATUM)
public class Datum {

	private Date created;
	private Date lastModified;
	private URI choice;
	
	/**
	 * The unique ID of the information that this datum contains
	 */
	private String info;
}
