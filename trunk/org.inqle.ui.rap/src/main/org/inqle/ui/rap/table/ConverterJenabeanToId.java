package org.inqle.ui.rap.table;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.conversion.IConverter;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jenabean.BasicJenabean;

import thewebsemantic.TypeWrapper;

/**
 * For databinding, can convert any class that inherits from BasicJenabean, into its ID
 * @author David Donohue
 * Mar 13, 2008
 */
@Deprecated
public class ConverterJenabeanToId implements IConverter {

	//private Class<? extends BasicJenabean> jenabeanClass;
	private Class<?> jenabeanClass;

	private Object jenabeanObject;

	static Logger log = Logger.getLogger(ConverterJenabeanToId.class);
	
	//public ConverterJenabeanToId(Class<? extends BasicJenabean> jenabeanClass) {
	public ConverterJenabeanToId(Object jenabeanObject) {
		this.jenabeanObject = jenabeanObject;
		this.jenabeanClass = jenabeanObject.getClass();
	}

	public Object convert(Object fromObject) {
		//return ((BasicJenabean)fromObject).getId();
		TypeWrapper typeWrapper = TypeWrapper.wrap(jenabeanClass);
		return typeWrapper.id(fromObject);
	}

	public Object getFromType() {
		return jenabeanObject;
	}

	public Object getToType() {
		return new String();
	}

}
