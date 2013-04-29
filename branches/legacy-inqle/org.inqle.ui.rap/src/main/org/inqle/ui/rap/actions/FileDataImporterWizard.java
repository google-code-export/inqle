/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.io.File;
import java.net.URI;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.Jenamodel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.cache.CacheTool;
import org.inqle.data.rdf.jenabean.mapping.DataMapping;
import org.inqle.data.rdf.jenabean.mapping.SubjectMapping;
import org.inqle.data.rdf.jenabean.mapping.TableMapping;
import org.inqle.data.rdf.jenabean.util.JenabeanWriter;
import org.inqle.rdf.RDF;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.csv.CsvReader;
import org.inqle.ui.rap.file.FileDataImporter;
import org.inqle.ui.rap.pages.CsvDisplayPage;
import org.inqle.ui.rap.pages.DateTimeMapperPage;
import org.inqle.ui.rap.pages.InfoPage;
import org.inqle.ui.rap.pages.LoadFilePage;
import org.inqle.ui.rap.pages.MappingLookupPage;
import org.inqle.ui.rap.pages.RowSubjectPropertyMappingsPage;
import org.inqle.ui.rap.pages.RowSubjectPropertyValuesPage;
import org.inqle.ui.rap.pages.RowSubjectUriPage;
import org.inqle.ui.rap.pages.SaveMappingLoadDataPage;
import org.inqle.ui.rap.pages.SubjectClassPage;
import org.inqle.ui.rap.table.RowSubjectClassPage;
import org.inqle.ui.rap.widgets.IDataFieldShower;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class FileDataImporterWizard extends DynaWizard implements ICsvReaderWizard {

	private static final String DEFAULT_THING_CLASS = "type of thing";
	private Model saveToModel;

	public FileDataImporterWizard(Model saveToModel, Shell shell) {
		super(shell);
		this.saveToModel = saveToModel;
	}

	private static Logger log = Logger.getLogger(FileDataImporterWizard.class);
	Composite composite;
	
	private LoadFilePage loadFilePage;
	private CsvReader csvReader;
	
//	private AddSubjectPage addSubjectPage;
	private SaveMappingLoadDataPage saveMappingLoadDataPage;
	private DateTimeMapperPage dateTimeMapperPage;
	private Jenamodel datamodel;
	private IPart part;
	public IPart getPart() {
		return part;
	}

	public void setPart(IPart part) {
		this.part = part;
	}

	private RowSubjectClassPage subjectClassPage;
	private RowSubjectUriPage subjectUriPage;
	private RowSubjectPropertyMappingsPage propertyMappingsPage;
	private RowSubjectPropertyValuesPage propertyValuesPage;
	private MappingLookupPage mappingLookupPage;
//	private TableMapping tableMapping;
	
	//each time a new subject (of either type) is added, each of these 5 lists is appended with 
	//a new page of its type.
//	private List<ASubjectClassPage> subjectClassPages = new ArrayList<ASubjectClassPage>();
//	private List<ASubjectUriPage> subjectUriPages = new ArrayList<ASubjectUriPage>();
//	private List<ASubjectPropertyValuesPage> subjectPropertyValuesPages = new ArrayList<ASubjectPropertyValuesPage>();;
//	private List<ASubjectPropertyMappingsPage> subjectPropertyMappingsPages = new ArrayList<ASubjectPropertyMappingsPage>();;
//	private List<AddSubjectPage> addSubjectPages = new ArrayList<AddSubjectPage>();
	
	@Override
	public void addPages() {
		InfoPage firstPage = new InfoPage(
			"Import Data From Text File",
			"This wizard assists you in importing data from a delimited text file such as " +
			"comma-separated values files (CSV).",
			"Before using this wizard, you should acquire your data into a delimited text \n" +
			"format, such as CSV.  You can generate such a file from your spreadsheet program.\n\n" +
			
			"The data is imported into your database in a format called semantic or \n" +
			"Resource Description Framework (RDF) data.  This means that data is imported as \n" +
			"objects.  \n" +
			"  * Each data object has a unique identifier, called a Uniform Resource Identifier (URI).\n" +
			"    URIs look a lot like web addresses (URLs)\n" +
			"    e.g. the person David Donohue is represented by this URI:\n" +
			"    'http://daviddonohue.com/DD'\n" +
			"  * Each data object has a type (or class), which itself has a URI.\n" +
			"    e.g. David Donohue is of type Person, and the type Person has this URI\n" +
			"    http://xmlns.com/foaf/0.1/Person \n" +
			"  * Data objects have Attributes, which are also identified by a URI.\n" +
			"    Attributes have values- either literal values like '42' or other data\n" +
			"    objects, like 'http://daviddonohue.com/DD'." +
			"    For example, the Person type has an attribute\n" +
			"    http://xmlns.com/foaf/0.1/interest \n" +
			"    The value of this attribute is a website which interests the person.\n\n" +
			
			"" +
			""
		);
		addPage(firstPage);
		
		loadFilePage = new LoadFilePage("Specify the delimited text file to load.");
		loadFilePage.setDescription("This wizard is capable of importing many different formats of " +
			"delimited text files.  In general, you could save your spreadsheet of data as " +
			"'Comma-Separated Values (CSV)' and then import the CSV file here.");
		addPage(loadFilePage);
		
		CsvDisplayPage csvDisplayPage = new CsvDisplayPage("View data to be imported.", null);
		addPage(csvDisplayPage);
		
		mappingLookupPage = new MappingLookupPage();
		addPage(mappingLookupPage);
		
		dateTimeMapperPage = new DateTimeMapperPage(
				"Specify Date & Time of the Data",
				null);
		dateTimeMapperPage.setDescription("All data pertains to a particular date and time.  We call this the PRIMARY date and time.  " +
				"Specify whether all the rows of data have the same primary date & time or whether each row has a different date and time.  " +
				"Note that your data may contain more than one date/time fields.  The one specified below will be stored as the PRIMARY " +
				"date and time of the data, and this will have some implications for how the data is displayed or processed.");
		addPage(dateTimeMapperPage);
		
//		addSubjectPage = new AddSubjectPage();
//		addPage(addSubjectPage);
		
		subjectClassPage = new RowSubjectClassPage();
		addPage(subjectClassPage);
		subjectUriPage = new RowSubjectUriPage();
		addPage(subjectUriPage);
		propertyMappingsPage = new RowSubjectPropertyMappingsPage();
		addPage(propertyMappingsPage);
		propertyValuesPage = new RowSubjectPropertyValuesPage();
		addPage(propertyValuesPage);
		
		saveMappingLoadDataPage = new SaveMappingLoadDataPage();
		addPage(saveMappingLoadDataPage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		//show the "importing..." dialog
		TableMapping tableMapping = null;
		try {
			tableMapping = createTableMapping();
			if (saveMappingLoadDataPage.shouldSaveMapping()) {
				log.info("Saving new TableMapping:\n" + JenabeanWriter.toString(tableMapping));
				Persister persister = Persister.getInstance();
				persister.persist(tableMapping);
			}
		} catch (RuntimeException e) {
			log.error("Error getting/saving mapping", e);
		}
		
		OntModel ontModel = ModelFactory.createOntologyModel();
		
		try {
			log.info("Importing data...");
			FileDataImporter importer = new FileDataImporter(csvReader, tableMapping, ontModel);
			importer.doImport();
		} catch (RuntimeException e) {
			log.error("Error importing data from file", e);
		}
		
		log.info("Saving to model of " + saveToModel.size() + " statements...");
		saveToModel.begin();
		saveToModel.add(ontModel);
		saveToModel.commit();
		
		MessageDialog.openInformation( getShell(), "Success importing data", "Successfully imported " + ontModel.size() + " statements."); 
		
		if (datamodel != null) {
			//flush any text indexes for the dataset
			log.info("Flushing indexes for datamodel: " + datamodel + "...");
			Persister persister = Persister.getInstance();
			persister.flushIndexes(datamodel);
			
			//invalidate the cache for this dataset
			CacheTool.invalidateDataCache(InqleInfo.CORE_DATABASE_ID, datamodel.getId());
		}
		
		log.info("Finished saving.  Model now has " + saveToModel.size() + " statements.");
		if (part != null) {
			part.fireUpdatePart();
		}
		return true;
	}
	
	@Override
	public boolean canFinish() {
//		IWizardPage[] pages = getPages();
//		if (pages.length < 1) {
//			return false;
//		}
//		IWizardPage lastPage = pages[pages.length - 1];
//		if (lastPage instanceof SaveMappingLoadDataPage) {
//			return true;
//		}
//		return false;
		
		if (mappingLookupPage.isTableMappingSelected()) {
			saveMappingLoadDataPage.setShouldSaveMapping(false);
			return true;
		} else {
//			tableMapping = null;
			saveMappingLoadDataPage.setShouldSaveMapping(false);
		}
		
		if (getContainer().getCurrentPage().equals(saveMappingLoadDataPage)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void prepareForClose() {
		//log.info("Disposing of uploaderWidget=" + uploaderWidget);
		if (loadFilePage != null) {
			loadFilePage.closeUploader();
		}
	}

	public CsvReader getCsvReader() {
		if (csvReader == null) {
			log.info("RRRRRRRRRRefreshing csvReader in LoadCsvFileWizard");
			refreshCsvReader();
		}
		return csvReader;
	}

	public void refreshCsvReader() {
		if (loadFilePage.getUploadedFile() == null) {
			log.error("loadFilePage.getUploadedFile()=null");
			return;
		}
		try {
			log.info("Get uploaded file...");
			File uploadedFile = loadFilePage.getUploadedFile();
			log.info("Got file " + uploadedFile + ".  Retrieve CSV importer...");
			//csvReader = new CsvReader(new FileInputStream(uploadedFile));
			csvReader = new CsvReader(uploadedFile);
		} catch (Exception e) {
			log.error("Unable to get uploaded file: " + loadFilePage.getUploadedFile());
			//leave as null
		}
	}

	/**
	 * This is called by MappingLookupPage, after the user has selected a mapping and gone to next page.
	 * set each page to use the values from the mapping
	 * @param tableMapping
	 */
	public void setTableMapping(TableMapping tableMapping) {
		log.info("Populating FileDataImporterWizard with TableMapping: \n" + JenabeanWriter.toString(tableMapping));
//		this.tableMapping = tableMapping;
		dateTimeMapperPage.setTableMapping(tableMapping);
		subjectClassPage.setTableMapping(tableMapping);
		subjectUriPage.setTableMapping(tableMapping);
		propertyMappingsPage.setTableMapping(tableMapping);
		propertyValuesPage.setTableMapping(tableMapping);
	}
//	public AddSubjectOrFinishPage getLastAddSubjectOrFinishPage() {
//		//loop back thru the wizard, and get the first instance of AddSubjectPage which is not the very first
//		IWizardPage[] wizardPages = getPages();
//		for (int i=wizardPages.length; i>0; i--) {
//			IWizardPage page = wizardPages[i];
//			if (page instanceof AddSubjectOrFinishPage) {
//				return (AddSubjectOrFinishPage)page;
//			}
//		}
//		return null;
//	}
	
//	public void addTableSubjectPages() {
//		TableSubjectClassPage subjectClassPage = new TableSubjectClassPage();
//		addPage(subjectClassPage);
//		TableSubjectUriPage subjectUriPage = new TableSubjectUriPage();
//		addPage(subjectUriPage);
//		TableSubjectPropertyValuesPage propertyValuesPage = new TableSubjectPropertyValuesPage();
//		addPage(propertyValuesPage);
//		TableSubjectPropertyMappingsPage propertyMappingsPage = new TableSubjectPropertyMappingsPage();
//		addPage(propertyMappingsPage);
//		AddSubjectOrFinishPage addSubjectOrFinishPage = new AddSubjectOrFinishPage();
//		addPage(addSubjectOrFinishPage);
//		getContainer().updateButtons();
//	}

//	public void addRowSubjectPages() {
//		RowSubjectClassPage subjectClassPage = new RowSubjectClassPage();
//		addPage(subjectClassPage);
//		RowSubjectUriPage subjectUriPage = new RowSubjectUriPage();
//		addPage(subjectUriPage);
//		RowSubjectPropertyMappingsPage propertyMappingsPage = new RowSubjectPropertyMappingsPage();
//		addPage(propertyMappingsPage);
//		RowSubjectPropertyValuesPage propertyValuesPage = new RowSubjectPropertyValuesPage();
//		addPage(propertyValuesPage);
//		AddSubjectOrFinishPage addSubjectOrFinishPage = new AddSubjectOrFinishPage();
//		addPage(addSubjectOrFinishPage);
//		getContainer().updateButtons();
//	}

//	public void addSaveMappingLoadDataPage() {
//		log.info("Adding saveMappingLoadDataPage...");
//		saveMappingLoadDataPage = new SaveMappingLoadDataPage();
//		addPage(saveMappingLoadDataPage);
//		log.info("Added saveMappingLoadDataPage.  Refreshing buttons...");
//		try {
//			getContainer().updateButtons();
//		} catch (RuntimeException e) {
//			log.error("error calling getContainer().updateButtons()", e);
//		}
//		log.info("Finished addSaveMappingLoadDataPage()");
//	}

	/**
	 * Get the subject class URI of the most recent SubjectClassPage, preceding the one passed 
	 * as an argument
	 * @param pageAfterSubjectClassPage
	 * @return
	 */
	public String getSubjectClassUri(IWizardPage pageAfterSubjectClassPage) {
		return subjectClassPage.getSubjectUri();
//		IWizardPage thePage = pageAfterSubjectClassPage;
//		while (thePage.getPreviousPage() != null) {
//			thePage = thePage.getPreviousPage();
//			if (thePage instanceof SubjectClassPage) {
//				SubjectClassPage subjectClassPage = (SubjectClassPage)thePage;
//				return subjectClassPage.getSubjectUri();
//			}
//		}
//		return null;
	}
	
	/**
	 * Get the a readable representation of the subject class URI of the most recent SubjectClassPage, 
	 * preceding the one passed as an argument
	 * @param pageAfterSubjectClassPage
	 * @return
	 */
	public String getThingClass(IWizardPage pageAfterSubjectClassPage) {
		IWizardPage thePage = pageAfterSubjectClassPage;
		while (thePage.getPreviousPage() != null) {
			thePage = thePage.getPreviousPage();
			if (thePage instanceof SubjectClassPage) {
				SubjectClassPage subjectClassPage = (SubjectClassPage)thePage;
				String thingClass = subjectClassPage.getThingClass();
				if (thingClass==null || thingClass.length()==0) {
					thingClass = subjectClassPage.getSubjectUri();
				}
				if (thingClass==null || thingClass.length()==0) {
					thingClass = DEFAULT_THING_CLASS;
				}
				return thingClass;
			}
		}
		return DEFAULT_THING_CLASS;
	}
	
	/**
	 * This is invoked upon submit.  It creates the TableMapping object
	 * (which is a Jenabean and can be persisted and reused).  This TableMapping
	 * will be used by the FileDataimporter to import the data as RDF.
	 * @return a TableMapping object, containing all info to import a single subject
	 * and its data
	 */
	public TableMapping createTableMapping() {
		TableMapping tableMapping = new TableMapping();
		
		DataMapping dateTimeDataMapping = new DataMapping();
		dateTimeDataMapping.setMapsPredicate(URI.create(RDF.DATE_PROPERTY));
		dateTimeDataMapping.setMapsPropertyType(URI.create(RDF.DATA_PROPERTY));
		String globalDateTime = dateTimeMapperPage.getGlobalDateTimeString();
		String dateTimeHeader = dateTimeMapperPage.getRowDateColumnHeader();
		if (globalDateTime!=null && globalDateTime.length()>0) {
			dateTimeDataMapping.setMapsValue(globalDateTime);
			tableMapping.addDataMapping(dateTimeDataMapping);
		} else if (dateTimeHeader!=null && dateTimeHeader.length()>0) {
			dateTimeDataMapping.setMapsHeader(dateTimeHeader);
		}
		
		String headerString = getCsvReader().getHeaderString();
		tableMapping.setMappedText(headerString);
		tableMapping.setName(saveMappingLoadDataPage.getTableMappingName());
		tableMapping.setDescription(saveMappingLoadDataPage.getTableMappingDescription());
	
		//IMPORT SUBJECT
	
		SubjectMapping subjectMapping = new SubjectMapping();
		subjectMapping.addDataMapping(dateTimeDataMapping);
		
		subjectMapping.setInstanceMapping(false);
		
		String subjectClass = subjectClassPage.getSubjectUri();
		String subjectUriPrefix = subjectUriPage.getInstancePrefixUri();
		int subjectUriType = subjectUriPage.getSubjectCreationMethodIndex();
		String subjectHeader = subjectUriPage.getUriSuffixColumnHeader();
		
		subjectMapping.setSubjectClass(URI.create(subjectClass));
		subjectMapping.setSubjectUriPrefix(URI.create(subjectUriPrefix));
		subjectMapping.setSubjectUriType(subjectUriType);
		subjectMapping.setSubjectHeader(subjectHeader);
		
		for (IDataFieldShower shower: propertyValuesPage.getDataFields()) {
			if (shower.getValue()==null || shower.getValue().trim().length()==0) continue;
			//Create a DataMapping for each property
			DataMapping dataMapping = new DataMapping();
			dataMapping.setMapsPredicate(URI.create(shower.getFieldUri()));
			dataMapping.setMapsPropertyType(URI.create(shower.getFieldPropertyType()));
			dataMapping.setMapsValue(shower.getValue());
			subjectMapping.addDataMapping(dataMapping);
		}
		
		for (IDataFieldShower shower: propertyMappingsPage.getDataFields()) {
			if (shower.getValue()==null || shower.getValue().trim().length()==0) continue;
			//Create a DataMapping for each property
			DataMapping dataMapping = new DataMapping();
			log.info("FFFFFFFFFFFFFFFFFF Field: " + shower.getFieldUri() + "\nProperty Type=" + shower.getFieldPropertyType());
			dataMapping.setMapsPredicate(URI.create(shower.getFieldUri()));
			dataMapping.setMapsPropertyType(URI.create(shower.getFieldPropertyType()));
			dataMapping.setMapsHeader(shower.getValue());
			subjectMapping.addDataMapping(dataMapping);
		}
		
		tableMapping.setSubjectMapping(subjectMapping);
	
		return tableMapping;
	}

	public void setDatamodel(Jenamodel namedModel) {
		this.datamodel = namedModel;
	}

}