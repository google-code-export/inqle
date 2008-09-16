package org.inqle.ui.rap.widgets;


import org.eclipse.swt.widgets.Shell;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
 
/**
 * Upon clicking "Save" and validating the form, the new SubClass created here will be 
 * stored as a SubClass of the OntClass provided
 * @author David Donohue
 * Jul 23, 2008
 */
public class SubpropertyDialog extends AOntResourceDialog {

		private OntProperty superProperty;

		/**
		 * @param parentShell
		 * @param ontClass upon saving this data, it will be created as a new instance of this ontClass
		 */
		public SubpropertyDialog(Shell parentShell, OntProperty superProperty) {
        super(parentShell);
        this.superProperty = superProperty;
    }

		@Override
		public String getTitle() {
			return "Register a new property";
		}
		
		@Override
		public String getMessage() {
			return "As you fill in the form below, you are registering a new type of property.\n" +
					"Specifically, you are registering a new subclass of this Semantic class:\n" +
					"URI: " + superProperty.getURI()+ "\n" +
					"Name: " + superProperty.getLocalName()+ "\n" +
					"Description: " + superProperty.getComment("EN")+ "\n\n" +
					"Upon registering this type of property, your INQLE server will be able to import and work with data objects \n" +
					"of this type.";
		}

		@Override
		public String getUriLabel() {
			return "Property URI";
		}
		
		@Override
		public String getUriDetail() {
			return "Enter the Universal Resource Identifier (URI) of this property.  " +
					"Example: http://my-institution-name.org/hasEmotionalState";
		}
		
		@Override
		public String getNameLabel() {
			return "Name";
		}
		
		@Override
		public String getNameDetail() {
			return "Enter a common name for this property.  We recommend you capitalize " +
					"the name and use the singular form. " +
					"Example: Emotional State";
		}
		
		@Override
		public String getDescriptionLabel() {
			return "Description";
		}
		
		@Override
		public String getDescriptionDetail() {
			return "Enter a description about this " + superProperty.getLocalName() +
					"We recommend you include synonyms of your concept, such that it is " +
					"easier to find when searching for it.";
		}
		
		@Override
		/**
		 * Creates a new subclass of the OntClass ontClass,
		 * and adds label & comment properties
		 */
		protected OntResource createOntResource() {
			OntModel ontModel = (OntModel)superProperty.getModel();
			OntProperty newSubproperty = ontModel.createOntProperty(getUri());
			newSubproperty.setSuperProperty(superProperty);
    	return newSubproperty;
		}
}