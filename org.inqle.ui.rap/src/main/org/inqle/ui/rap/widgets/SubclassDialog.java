package org.inqle.ui.rap.widgets;


import org.eclipse.swt.widgets.Shell;

import com.hp.hpl.jena.ontology.OntClass;
 
/**
 * Upon clicking "Save" and validating the form, the new Individual created here will be 
 * stored as a instance of the OntClass provided
 * @author David Donohue
 * Jul 23, 2008
 */
public class SubclassDialog extends AResourceDialog {
		
		/**
		 * @param parentShell
		 * @param ontClass upon saving this data, it will be created as a new instance of this ontClass
		 */
		public SubclassDialog(Shell parentShell, OntClass ontClass) {
        super(parentShell, ontClass);
    }

		@Override
		public String getTitle() {
			return "Register a type of " + ontClass.getLocalName();
		}
		
		@Override
		public String getMessage() {
			return "As you fill in the form below, you are registering a new type of " + ontClass.getLocalName() + ".\n" +
					"That is, you are registering a new subclass of this Semantic class:\n" +
					"URI: " + ontClass.getURI()+ "\n" +
					"Name: " + ontClass.getLocalName()+ "\n" +
					"Description: " + ontClass.getComment("EN")+ "\n\n" +
					"Upon registering this type, your INQLE server will be able to import and work with data objects \n" +
					"of this type.  We recommend that you find and use existing RDF classes for your data type.";
		}

		@Override
		public String getUriLabel() {
			return "Type URI";
		}
		
		@Override
		public String getUriDetail() {
			return "Enter the Universal Resource Identifier (URI) of this type of\n" + ontClass.getLocalName() + "\n" +
					"Example: http://my-institution-name.org/EmotionalState";
		}
		
		@Override
		public String getNameLabel() {
			return "Name";
		}
		
		@Override
		public String getNameDetail() {
			return "Enter a common name for this type.  We recommend you capitalize \n" +
					"the name and use the singular form.\n" +
					"Example: Emotional State";
		}
		
		@Override
		public String getDescriptionLabel() {
			return "Description";
		}
		
		@Override
		public String getDescriptionDetail() {
			return "Enter a description about this " + ontClass.getLocalName() + "\n" +
					"We recommend you include synonyms of your concept, such that it is \n" +
					"easier to find when searching for it.";
		}

}

