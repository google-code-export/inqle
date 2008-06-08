/**
 * 
 */
package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.inqle.ui.rap.actions.LoadCsvFileWizard;
import org.inqle.ui.rap.csv.CsvImporter;

/**
 * @author David Donohue
 * Jun 4, 2008
 */
public class CsvSubjectPage extends DynaWizardPage {
	
	private static Logger log = Logger.getLogger(CsvSubjectPage.class);
	private Text subjectPrefixText;
	private Text subjectClassText;
	private List idTypeList;
	private List subjectColumnList;
	
	public CsvSubjectPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create and populate the table, using the column names provided in 
	 * <code>setPropertyNames</code> and the row data provided by <code>setRdfTable</code>
	 * 
	 */
	public void refreshTableData() {
		if (getWizard() == null || (!(getWizard() instanceof LoadCsvFileWizard))) {
			log.error("getWizard()=" + getWizard() + "; it is null or not a LoadCsvFileWizard");
			return;
		}
//		log.info("getWizard()= a LoadCsvFileWizard");
		
		LoadCsvFileWizard loadCsvFileWizard = (LoadCsvFileWizard)getWizard();
		//log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		CsvImporter csvImporter = loadCsvFileWizard.getCsvImporter();
		//log.info("csvImporter retrieved");
		String[][] data = csvImporter.getRawData();
		//log.info("data= " + data);
		String[] headers = data[csvImporter.getHeaderIndex()];
		
		//set the page layout
		GridData gridData;
		//Composite pageComposite = new Composite(selfComposite, SWT.H_SCROLL | SWT.V_SCROLL);
//		Composite pageComposite = new Composite(selfComposite, SWT.H_SCROLL | SWT.V_SCROLL);
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//		pageComposite.setLayoutData(gridData);
//		pageComposite.setLayout (new GridLayout (1,false));
		
		//Generate the form at the top of the page
		//Composite formComposite = new Composite(pageComposite, SWT.NONE);
//		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//		formComposite.setLayoutData(gridData);
//		formComposite.setLayout (new GridLayout (2,false));
		
		try {
			new Label (selfComposite, SWT.NONE).setText("ID Type");
			idTypeList = new List(selfComposite, SWT.BORDER | SWT.SINGLE | SWT.DROP_DOWN);//SWT.V_SCROLL | 
			idTypeList.setItems(CsvImporter.ID_TYPES);
			idTypeList.setSelection(csvImporter.getIdType());
			gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			idTypeList.setToolTipText("This defines which method will be used to generate the ID for each subject (row).");
			//GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			idTypeList.setLayoutData(gridData);
			
			new Label (selfComposite, SWT.NONE).setText("Subject Column");
			subjectColumnList = new List(selfComposite, SWT.BORDER | SWT.SINGLE | SWT.DROP_DOWN);//SWT.V_SCROLL | 
			subjectColumnList.setItems(headers);
			gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			subjectColumnList.setLayoutData(gridData);
			
			if (idTypeList.getSelectionIndex() != CsvImporter.ID_TYPE_CELL_VALUE) {
				subjectColumnList.deselectAll();
				subjectColumnList.setEnabled(false);
			}
			idTypeList.addListener (SWT.Selection, new Listener () {
				public void handleEvent (Event e) {
					if (getIdTypeIndex() == CsvImporter.ID_TYPE_CELL_VALUE) {
						subjectColumnList.setEnabled(true);
					} else {
						subjectColumnList.deselectAll();
						subjectColumnList.setEnabled(false);
					}
				}
			});
			new Label (selfComposite, SWT.NONE).setText("Subject Class");
			subjectClassText = new Text(selfComposite, SWT.BORDER);
			subjectClassText.setToolTipText("Enter the URI of the RDF class of which each subject (row) is a member.  For each subject (row), a statement will be added that says: 'This row IS A [your value for Subject Class]'.");
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			subjectClassText.setLayoutData(gridData);
			
			new Label (selfComposite, SWT.NONE).setText("Subject Prefix");
			subjectPrefixText = new Text(selfComposite, SWT.BORDER);
			subjectPrefixText.setToolTipText("Enter the prefix to be used in creating the ID of each subject (row).");
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			subjectPrefixText.setLayoutData(gridData);
			
//			subjectClassText.addVerifyListener (new VerifyListener () {
//				public void verifyText (VerifyEvent event) {
//					subjectPrefixText.setText(subjectClassText.getText() + "/");
//				}
//			});
			subjectClassText.addModifyListener (new ModifyListener () {
				public void modifyText (ModifyEvent event) {
					subjectPrefixText.setText(subjectClassText.getText() + "/");
					setMessage("");
				}
			});
			selfComposite.setSize(selfComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			selfComposite.pack(true);
		} catch (Exception e) {
			log.error("Unable to render CSV Import Page's form", e);
		}
	}

	@Override
	public void addElements() {
		
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		refreshTableData();
	}
	
	@Override
	public boolean onNextPage() {
		String validationMessage = "";
		if (getSubjectClassUri() == null || getSubjectClassUri().length() == 0) {
			validationMessage += "Please enter a value for 'Subject Class'.\n";
		}
		if (getSubjectPrefix() == null || getSubjectPrefix().length() == 0) {
			validationMessage += "Please enter a value for 'Subject Prefix'.\n";
		}
		
		if (validationMessage.length() > 0) {
			setMessage(validationMessage, DialogPage.WARNING);
			return false;
		}
		return true;
	}
	
	public int getIdTypeIndex() {
		if (idTypeList == null) {
			return -1;
		}
		return idTypeList.getSelectionIndex();
	}
	
	public int getSubjectColumnIndex() {
		if (subjectColumnList == null) {
			return -1;
		}
		return subjectColumnList.getSelectionIndex();
	}
	
	public String getSubjectClassUri() {
		if (subjectClassText == null) {
			return null;
		}
		return subjectClassText.getText();
	}
	
	public String getSubjectPrefix() {
		if (subjectPrefixText == null) {
			return null;
		}
		return subjectPrefixText.getText();
	}

}
