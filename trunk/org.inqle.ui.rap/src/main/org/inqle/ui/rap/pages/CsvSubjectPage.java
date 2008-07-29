/**
 * 
 */
package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.inqle.ui.rap.actions.ICsvImporterWizard;
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
	private Composite formComposite;
	private ScrolledComposite scrollingComposite;
	
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
		try {
			log.info("get csvImporter...");
			CsvImporter csvImporter = getCsvImporter();
			log.info("csvImporter retrieved");
			
			String[][] data = csvImporter.getRawData();
			//log.info("data= " + data);
			String[] headers = data[csvImporter.getHeaderIndex()];
			idTypeList.deselectAll();
			idTypeList.setSelection(csvImporter.getIdType());
			
			subjectColumnList.removeAll();
			subjectColumnList.setItems(headers);
			if (idTypeList.getSelectionIndex() != CsvImporter.ID_TYPE_CELL_VALUE) {
				subjectColumnList.setEnabled(false);
			} else {
				subjectColumnList.setSelection(csvImporter.getSubjectIndex());
			}
			
			subjectClassText.setText("");
			if (csvImporter.getSubjectClassUri() != null) {
				subjectClassText.setText(csvImporter.getSubjectClassUri());
			}
			subjectPrefixText.setText("");
			if (csvImporter.getSubjectPrefix() != null) {
				subjectPrefixText.setText(csvImporter.getSubjectPrefix());
			}
		} catch (Exception e) {
			log.error("Error refreshing table data", e);
		}
		
	}
	
	@Override
	public void createControl(Composite parent) {
		log.info("CsvSubjectPage.createControl()");
		//set the page layout
		GridData gridData;
		try {
			if (getWizard() == null || (!(getWizard() instanceof LoadCsvFileWizard))) {
				log.error("getWizard()=" + getWizard() + "; it is null or not a LoadCsvFileWizard");
				return;
			}
			
			
			scrollingComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
			
			formComposite = new Composite(scrollingComposite, SWT.NONE);
			formComposite.setLayout (new GridLayout (2,false));
		} catch (Exception e) {
			log.error("Error creating controls for CsvSubjectPage", e);
			return;
		}
		
		try {
			new Label (formComposite, SWT.NONE).setText("ID Type");
			idTypeList = new List(formComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
			idTypeList.setItems(CsvImporter.ID_TYPES);
			gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			idTypeList.setToolTipText("This defines which method will be used to generate the ID for each subject (row).");
			//GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			idTypeList.setLayoutData(gridData);
			
			new Label (formComposite, SWT.NONE).setText("Subject Column");
			subjectColumnList = new List(formComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
			gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			subjectColumnList.setLayoutData(gridData);
			
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
			new Label (formComposite, SWT.NONE).setText("Subject Class");
			subjectClassText = new Text(formComposite, SWT.BORDER);
			subjectClassText.setToolTipText("Enter the URI of the RDF class of which each subject (row) is a member.  For each subject (row), a statement will be added that says: 'This row IS A [your value for Subject Class]'.");
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			subjectClassText.setLayoutData(gridData);
			
			new Label (formComposite, SWT.NONE).setText("Subject Prefix");
			subjectPrefixText = new Text(formComposite, SWT.BORDER);
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
			
			scrollingComposite.setContent(formComposite);
			scrollingComposite.setExpandHorizontal(true);
			scrollingComposite.setExpandVertical(true);
			//scrollingComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			formComposite.setSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
			scrollingComposite.addControlListener(new ControlAdapter() {
				public void controlResized(ControlEvent e) {
					Rectangle r = scrollingComposite.getClientArea();
					scrollingComposite.setMinSize(formComposite.computeSize(r.width, SWT.DEFAULT));
				}
			});
			
			setControl(scrollingComposite);
			
//			formComposite.setSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//			formComposite.pack(true);
		} catch (Exception e) {
			log.error("Unable to render CSV Import Page's form", e);
		}
	}

	private CsvImporter getCsvImporter() {
		ICsvImporterWizard loadCsvFileWizard = (ICsvImporterWizard)getWizard();
		//log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		return loadCsvFileWizard.getCsvImporter();
	}

	@Override
	public void addElements() {
		
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering CsvSubjectPage...");
		refreshTableData();
	}
	
	@Override
	public boolean onNextPage() {
		bindValuesToCsvImporter();
		
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
	
	@Override
	public boolean onPreviousPage() {
		bindValuesToCsvImporter();
		return true;
	}
	
	private void bindValuesToCsvImporter() {
		getCsvImporter().setSubjectIndex(getSubjectColumnIndex());
		getCsvImporter().setSubjectPrefix(getSubjectPrefix());
		getCsvImporter().setSubjectClassUri(getSubjectClassUri());
		getCsvImporter().setIdType(getIdTypeIndex());
		
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
