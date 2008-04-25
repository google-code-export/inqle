package org.inqle.ui.rap;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;

/**
 * This view shows a &quot;mail message&quot;. This class is contributed through
 * the plugin.xml.
 */
public class HelloView extends ViewPart implements ISelectionListener {

	public static final String ID = "org.inqle.ui.rap.helloView";
	private Composite parent;
	
	public void createPartControl(Composite parent) {
		this.parent = parent;
		getSite().getPage().addSelectionListener(this);
		
		Composite top = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		top.setLayout(layout);
		// top banner
		Composite banner = new Composite(top, SWT.NONE);
		banner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, true));
		layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 2;
		banner.setLayout(layout);
		
		// setup bold font
		Font boldFont = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);    
		
		Label l = new Label(banner, SWT.WRAP);
		l.setText("Banner Here");
		l.setFont(boldFont);
		l = new Label(banner, SWT.WRAP);
		l.setText("Hello from the banner!  Application installed here:" + 
				Platform.getInstallLocation().getURL().getPath() + 
				"\nappInfo location is :" + Persister.getAppInfoFilePath());
		
		//AppInfo appInfo = Persister.loadAppInfo();
		AppInfo appInfo = Persister.loadAppInfo();
		//l = new Label(banner, SWT.WRAP);
		Text text = new Text(banner, SWT.MULTI | SWT.WRAP);
		//l.setText(JenabeanWriter.toString(appInfo));
		text.setText(JenabeanWriter.toString(appInfo));
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		l.pack();
		//l.setText(appInfoObj.getClass() + "; " + appInfoObj.toString());
	}

	public void setFocus() {
	}

	public void selectionChanged(IWorkbenchPart part, ISelection iSelection) {
		
	  if(iSelection instanceof IStructuredSelection) {
	     IStructuredSelection selection = (IStructuredSelection)iSelection;
	     String msg = "Selected:\n";
	     for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
	       Object selectedObj = iterator.next();
	       msg += selectedObj + ",\n";
	     }
	     //MessageDialog.openInformation(shell.getShell(), "Selection Made in Tree", msg);
	     
	 		 //page.openEditor(editorInput, "org.eclipse.ui.DefaultTextEdtior");
	  }
		
	}
}
