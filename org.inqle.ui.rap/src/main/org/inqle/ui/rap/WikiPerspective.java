package org.inqle.ui.rap;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.inqle.ui.rap.tree.PartsView;
import org.inqle.ui.rap.views.DetailView;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class WikiPerspective implements IPerspectiveFactory {

	private static final String EDITOR_FOLDER_ID = "org.inqle.ui.rap.editorFolder";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(PartsView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout folder = layout.createFolder(EDITOR_FOLDER_ID, IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder("*");
		folder.addView(DetailView.ID);
		
		layout.getViewLayout(PartsView.ID).setCloseable(false);
		//layout.getViewLayout(NavigationView.ID).setCloseable(false);
	}
}
