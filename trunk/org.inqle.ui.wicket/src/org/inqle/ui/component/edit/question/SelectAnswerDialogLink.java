/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.ResourceReference;

import com.antilia.web.dialog.DefaultDialog;
import com.antilia.web.dialog.DialogLink;
import com.antilia.web.resources.DefaultStyle;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class SelectAnswerDialogLink extends DialogLink {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public SelectAnswerDialogLink(String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DialogLink#getImage()
	 */
	@Override
	protected ResourceReference getImage() {
		return DefaultStyle.IMG_EDIT;
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DialogLink#getLabel()
	 */
	@Override
	protected String getLabel() {
		return "Select answer";
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DialogLink#getLabelKey()
	 */
	@Override
	protected String getLabelKey() {
		return "Select answer";
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DialogLink#newDialog(java.lang.String)
	 */
	@Override
	public DefaultDialog newDialog(String id) {
		return new AnswersDialog(id, this);
	}

}
