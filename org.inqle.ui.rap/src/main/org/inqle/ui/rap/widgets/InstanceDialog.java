package org.inqle.ui.rap.widgets;


import org.eclipse.swt.widgets.Shell;
import org.inqle.rdf.RDF;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
 
/**
 * Upon clicking "Save" and validating the form, the new Individual created here will be 
 * stored as a instance of the OntClass provided
 * @author David Donohue
 * Aug 25, 2008
 */
public class InstanceDialog extends AOntResourceDialog {

	protected OntClass ontClass;
	
		/**
		 * @param parentShell
		 * @param ontClass upon saving this data, it will be created as a new instance of this ontClass
		 */
		public InstanceDialog(Shell parentShell, OntClass ontClass) {
        super(parentShell);
        this.ontClass = ontClass;
    }

		@Override
		public String getTitle() {
			return "Register a type of " + ontClass.getLocalName();
		}
		
		@Override
		public String getMessage() {
			return "As you fill in the form below, you are registering a new " + ontClass.getLocalName() + ".\n" +
					"That is, you are registering a new instance of this Semantic class:\n" +
					"URI: " + ontClass.getURI()+ "\n" +
					"Name: " + ontClass.getLocalName()+ "\n" +
					"Description: " + ontClass.getComment("EN");
		}

		@Override
		public String getUriLabel() {
			return "URI";
		}
		
		@Override
		public String getUriDetail() {
			return "Enter the Universal Resource Identifier (URI) of this thing, of type\n" + ontClass.getLocalName() + "\n" +
					"Example: http://my-institution-name.org/EmotionalState/Joy";
		}
		
		@Override
		public String getNameLabel() {
			return "Name";
		}
		
		@Override
		public String getNameDetail() {
			return "Enter a common name for this thing.  We recommend you capitalize \n" +
					"the name and use the singular form.  Example: Weather Station C1001";
		}
		
		@Override
		public String getDescriptionLabel() {
			return "Description";
		}
		
		@Override
		public String getDescriptionDetail() {
			return "Enter a description about this " + ontClass.getLocalName() + "\n" +
					"We recommend you include synonyms, such that this thing is \n" +
					"easier to find when searching for it.";
		}
		
		@Override
		/**
		 * Creates a new subclass of the OntClass ontClass,
		 * and adds label & comment properties
		 */
		protected OntResource createOntResource() {
			Individual individual = ontClass.createIndividual(getUri());
    	return individual;
		}

}

