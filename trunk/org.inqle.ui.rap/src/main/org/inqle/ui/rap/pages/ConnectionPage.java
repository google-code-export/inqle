package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.sdb.DBConnector;
import org.inqle.data.rdf.jenabean.JenabeanWriter;

/**
	 * This generates the wizard page for creating a database connection
	 * @author David Donohue
	 * Feb 8, 2008
	 */
	public class ConnectionPage extends WizardPage {
		
		private static Logger log = Logger.getLogger(ConnectionPage.class);
		private Connection connection = null;

		private Shell shell;
		private String pageTitle;
		private Text dbType;
		private Text dbURL;
		private Text dbClass;
		private Text dbUser;
		private Text dbPassword;
		
		private static final String[] DBTYPES = {
			"Derby", 
			"H2",
			"HSQL",
			"MsSQL",
			"MySQL",
			"Oracle",
			"PostgreSQL", 
			"[Other]"
		};
		private static final String[] DBDRIVERS = {
			//"org.apache.derby.jdbc.EmbeddedDriver", 
			"org.apache.derby.jdbc.ClientDriver",
			"org.h2.Driver",
			"org.hsqldb.jdbcDriver",
			//"com.microsoft.sqlserver.jdbc.SQLServerDriver",
			"net.sourceforge.jtds.jdbc.Driver",
			"com.mysql.jdbc.Driver",
			"oracle.jdbc.driver.OracleDriver",
			"org.postgresql.Driver",
			"com.javaclass.of.database.Driver" 
		};
		private static final String[] DBURLS = {
			"jdbc:derby:databasename", 
			"jdbc:h2:databasename",
			"jdbc:hsqldb:file:filename",
			"jdbc:sqlserver://localhost\\SQLExpress;database=databasename",
			"jdbc:mysql://localhost:3306/databasename",
			"jdbc:oracle:thin:@hostname:port:sid",
			"jdbc:postgresql://localhost:5432/databasename", 
			"jdbc:productname://localhost:1234/databasename" 
		};
		
		public ConnectionPage(String pageTitle, Connection connection, Shell shell) {
			super(pageTitle);
			this.pageTitle = pageTitle;
			this.connection = connection;
			this.shell = shell;
		}
		
		@Override
		public String getTitle() {
			return pageTitle;
		}
		
		public void createControl(Composite pageParent) {
			
//			log.info("createControl() using Connection:\n" + JenabeanWriter.toString(connection));
			Composite composite = new Composite(pageParent, SWT.NONE);
	    // create the desired layout for this wizard page
			GridLayout gl = new GridLayout(2, false);
			composite.setLayout(gl);
	    
	    
	    //create the form
			GridData gridData;
			
			new Label (composite, SWT.NONE).setText("Change Database Type");

			final List dbTypeList = new List (composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			dbTypeList.setLayoutData(gridData);
			
			//log.info("DBTYPES has " + DBTYPES.length);
			for (int i=0; i<DBTYPES.length; i++) {
				dbTypeList.add (DBTYPES[i]);
				if (DBTYPES[i].equals(connection.getDbType())) {
					dbTypeList.select(i);
				}
				//log.info("Adding DBType: " + DBTYPES[i]);
			}
			
			/*
			list.addListener (SWT.DefaultSelection, new Listener () {
				public void handleEvent (Event e) {
					String string = "";
					int [] selection = list.getSelectionIndices ();
					for (int i=0; i<selection.length; i++) string += selection [i] + " ";
					System.out.println ("DefaultSelection={" + string + "}");
				}
			});
			*/
			
			new Label (composite, SWT.NONE).setText("Database Type");	
	    dbType = new Text(composite, SWT.BORDER);
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    dbType.setLayoutData(gridData);
			
	    new Label (composite, SWT.NONE).setText("Database URL");	
	    dbURL = new Text(composite, SWT.BORDER);
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    dbURL.setLayoutData(gridData);
	    
	    new Label (composite, SWT.NONE).setText("Database Driver");	
	    dbClass = new Text(composite, SWT.BORDER);
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    dbClass.setLayoutData(gridData);
			
	    new Label (composite, SWT.NONE).setText("Database Username");	
	    dbUser = new Text(composite, SWT.BORDER);
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			dbUser.setLayoutData(gridData);
			
	    new Label (composite, SWT.NONE).setText("Database Password");
	    dbPassword = new Text(composite, SWT.BORDER | SWT.PASSWORD);
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    dbPassword.setLayoutData(gridData);
	    
	    Button testConnection = new Button(composite, SWT.PUSH);
	    testConnection.setText("Test Connection");

	    testConnection.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					showTestConnectionDialog();
				}
			});
	    
	    /* TODO get refresh button to work
	    Button resetConnection = new Button(composite, SWT.PUSH);
	    resetConnection.setText("Reset");
	    resetConnection.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					resetConnection();
					composite.redraw();
				}
			});
	    */
	    
	    dbTypeList.addListener (SWT.Selection, new Listener () {
				public void handleEvent (Event e) {
					int selectionIndex = dbTypeList.getSelectionIndex();
					dbType.setText(dbTypeList.getSelection()[0]);
					//focus out of each, to trigger update to databinding model
					dbType.forceFocus();
					dbURL.setText(DBURLS[selectionIndex]);
					dbURL.forceFocus();
					dbClass.setText(DBDRIVERS[selectionIndex]);
					dbClass.forceFocus();
					dbType.forceFocus();
				}
			});
	    
	    //add data binding
	    // Initiating the realm once sets the default session Realm
	    if( Realm.getDefault() == null ) {
	      SWTObservables.getRealm( Display.getCurrent() );
	    }
	    Realm realm = Realm.getDefault();
	    
	    DataBindingContext bindingContext = new DataBindingContext();
	    
	    //TODO change BeansObservables to PojoObservables, when available:  http://fire-change-event.blogspot.com/2007/10/getting-rid-of-those-pesky-could-not.html
	    IObservableValue dbTypeObserveWidget = SWTObservables.observeText(dbType, SWT.FocusOut);
			IObservableValue dbTypeObserveValue = BeansObservables.observeValue(realm, connection, "dbType");
			bindingContext.bindValue(dbTypeObserveWidget, dbTypeObserveValue, null, null);
	    
	    IObservableValue dbURLObserveWidget = SWTObservables.observeText(dbURL, SWT.FocusOut);
			IObservableValue dbURLObserveValue = BeansObservables.observeValue(realm, connection, "dbURL");
			bindingContext.bindValue(dbURLObserveWidget, dbURLObserveValue, null, null);
			
			IObservableValue dbClassObserveWidget = SWTObservables.observeText(dbClass, SWT.FocusOut);
			IObservableValue dbClassObserveValue = BeansObservables.observeValue(realm, connection, "dbClass");
			bindingContext.bindValue(dbClassObserveWidget, dbClassObserveValue, null, null);
	    
	    IObservableValue dbUserObserveWidget = SWTObservables.observeText(dbUser, SWT.FocusOut);
			IObservableValue dbUserObserveValue = BeansObservables.observeValue(realm, connection, "dbUser");
			bindingContext.bindValue(dbUserObserveWidget, dbUserObserveValue, null, null);

			IObservableValue dbPasswordObserveWidget = SWTObservables.observeText(dbPassword, SWT.FocusOut);
			IObservableValue dbPasswordObserveValue = BeansObservables.observeValue(realm, connection, "dbPassword");
			bindingContext.bindValue(dbPasswordObserveWidget, dbPasswordObserveValue, null, null);
			
	    setControl(composite);
		}

		private void showTestConnectionDialog() {
			DBConnector connector = new DBConnector(connection);
			boolean connectionSucceeds = connector.testConnection();
			String title = "Connection Succeeds";
			String message = "Success connecting to the database.";
			if (!connectionSucceeds) {
				title = "Connection Fails";
				message = "Unable to connect to the database.";
				MessageDialog.openWarning(shell, title, message);
			} else {
				MessageDialog.openInformation(shell, title, message);
			}
		}

		public String getDbType() {
			if (dbType==null) return null;
			return dbType.getText();
		}
		
		public String getDbURL() {
			if (dbURL==null) return null;
			return dbURL.getText();
		}
		
		public String getDbClass() {
			if (dbClass==null) return null;
			return dbClass.getText();
		}
		
		public String getDbUser() {
			if (dbUser==null) return null;
			return dbUser.getText();
		}
		
		public String getDbPassword() {
			if (dbPassword==null) return null;
			return dbPassword.getText();
		}
		
	}