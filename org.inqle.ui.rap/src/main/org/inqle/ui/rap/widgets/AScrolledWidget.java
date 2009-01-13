package org.inqle.ui.rap.widgets;


import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.hp.hpl.jena.ontology.OntModel;
 
/**
 * This abstract widget is intended to be subclassed.  
 * The subclass should set the layout for composite, then add form elements to
 * it.  Finally, it should call recomputeSize()
 * @author David Donohue
 * Dec 30, 2008
 */
public abstract class AScrolledWidget extends Composite {
		
	protected Composite composite;
	private ScrolledComposite scrolledComposite;
	protected OntModel ontModel;
	private Composite pageComposite;
	private AScrolledWidget container;
//	protected Composite unscrolledTopComposite;
	
	private static Logger log = Logger.getLogger(AScrolledWidget.class);
	
	/**
	 * ontClass is the super class, to be used for creating this OntResource
	 */
	protected AScrolledWidget(Composite parent, int style) {
		super(parent, style);
		container = this;
		
  	container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
  	container.setLayout(new GridLayout(1, true));

//  	unscrolledTopComposite = new Composite(container, SWT.NONE);
  	
//		ScrolledComposite scrolled = new ScrolledComposite(container, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//		scrolled.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		scrolled.setLayout(new GridLayout());
//		scrolled.setExpandVertical(true);
//		scrolled.setExpandHorizontal(true);
		
  	scrolledComposite = new ScrolledComposite(container, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setLayout(new GridLayout(1, true));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		scrolledComposite.setLayoutData(gridData);
		
		pageComposite = new Composite(scrolledComposite, SWT.NONE);
		pageComposite.setLayout(new GridLayout(1, true));
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		pageComposite.setLayoutData(gridData);
		
//  		messageText = new Text(pageComposite, SWT.WRAP | SWT.READ_ONLY);
//			if (getMessage() != null) {
//				messageText.setText(getMessage());
//			}
//			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//			messageText.setLayoutData(gridData);
		
		composite = new Composite(pageComposite, SWT.NONE);
		
		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		
		scrolledComposite.setContent(pageComposite);
		
  }
	
	public void recomputeSize() {
		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.redraw();
		scrolledComposite.layout(true, true);
	}
    
}

