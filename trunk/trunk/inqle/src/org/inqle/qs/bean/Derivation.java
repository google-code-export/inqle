package org.inqle.qs.bean;

import org.openrdf.annotations.Iri;
import static org.inqle.qs.Constants.NS_DERIVATION;

@Iri(NS_DERIVATION)
public class Derivation implements HasDatum {

	private Datum datum;

	@Override
	public Datum getDatum() {
		return datum;
	}

	@Override
	public void setDatum(Datum datum) {
		this.datum = datum;
	}

}
