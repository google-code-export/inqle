package org.inqle.ui.rap.table;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.conversion.IConverter;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.rdf.model.Model;

@Deprecated
public class ConverterIdToJenabean implements IConverter {

	private Class<?> jenabeanClass;
	private Model model;
	private Object jenabeanObject;

	static Logger log = Logger.getLogger(ConverterIdToJenabean.class);
	
	/**
	 * IConverter for converting ID (Strings) to BasicJenabean objects.
	 * @param jenabeanClass
	 * @param model
	 */
	public ConverterIdToJenabean(Object jenabeanObject, Model model) {
		this.jenabeanObject = jenabeanObject;
		this.jenabeanClass = jenabeanObject.getClass();
		this.model = model;
	}

	/**
	 * Convert from ID to the Jenabean object, by reconstituting from the provided Jena Model
	 */
	public Object convert(Object fromObject) {
		return Persister.reconstitute(jenabeanClass, (String)fromObject, model, true);
	}

	public Object getFromType() {
		return new String();
	}

	public Object getToType() {
		return jenabeanObject;
	}

}
