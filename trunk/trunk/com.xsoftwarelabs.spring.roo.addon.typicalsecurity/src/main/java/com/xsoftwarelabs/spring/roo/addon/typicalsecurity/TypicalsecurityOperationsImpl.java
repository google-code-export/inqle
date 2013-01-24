package com.xsoftwarelabs.spring.roo.addon.typicalsecurity;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.process.manager.MutableFile;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Repository;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.Shell;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlElementBuilder;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xsoftwarelabs.spring.roo.addon.typicalsecurity.utils.StreamUtils;

/**
 * Implementation of operations this add-on offers.
 *
 * @since 1.1
 */
@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class TypicalsecurityOperationsImpl implements TypicalsecurityOperations {
    
    /**
     * Use ProjectOperations to install new dependencies, plugins, properties, etc into the project configuration
     */
    @Reference private ProjectOperations projectOperations;

    /**
     * Use TypeLocationService to find types which are annotated with a given annotation in the project
     */
    @Reference private TypeLocationService typeLocationService;
    
    /**
     * Use TypeManagementService to change types
     */
    @Reference private TypeManagementService typeManagementService;

    @Reference private Shell shell;
    
    @Reference private PathResolver pathResolver;
    
    @Reference private FileManager fileManager;
    
    private String entityPackage;// = projectOperations.getFocusedTopLevelPackage() + ".security";
	private String controllerPackage;// = projectOperations.getFocusedTopLevelPackage() + ".web";

	private String adminUsername;

	private String adminPassword;
    
	private static char separator = File.separatorChar;
	
    /** {@inheritDoc} */
    public boolean isCommandAvailable() {
        // Check if a project has been created
        return projectOperations.isFocusedProjectAvailable();
    }

    /** {@inheritDoc} */
    @CliCommand(value = "typicalsecurity setup", help = "Install TypicalSecurity for MVC project")
    public void setup(String entityPackage, String controllerPackage, String adminUsername, String adminPassword) {
    	this.entityPackage = entityPackage;
    	this.controllerPackage = controllerPackage;
    	if (adminUsername == null || adminUsername.trim().length() == 0) {
    		adminUsername = "admin";
    	}
    	this.adminUsername = adminUsername;
    	if (adminPassword == null || adminPassword.trim().length() == 0) {
    		adminPassword = "admin";
    	}
    	this.adminPassword = sha256(adminPassword);
    	
        // Install the add-on Google code repository needed to get the annotation 
        projectOperations.addRepository("", new Repository("Typicalsecurity Roo add-on repository", "Typicalsecurity Roo add-on repository", "https://typicalsecurity.googlecode.com/svn/repo"));
        List<Dependency> dependencies = new ArrayList<Dependency>();
        
        //add captcha dependency
        dependencies.add(new Dependency("net.tanesha.recaptcha4j", "recaptcha4j", "0.0.7"));
        
        // Install dependencies defined in external XML file
        for (Element dependencyElement : XmlUtils.findElements("/configuration/batch/dependencies/dependency", XmlUtils.getConfiguration(getClass()))) {
            dependencies.add(new Dependency(dependencyElement));
        }

        // Add all new dependencies to pom.xml
        projectOperations.addDependencies("", dependencies);
        
		createUserRoleEntities();
		createControllers();
		injectDatabasebasedSecurity();
		injectApplicationContext();
		
		insertI18nMessages();
    }
    
    private void injectApplicationContext() {
    	Element configuration = XmlUtils.getConfiguration(this.getClass());
    	Collection<Element> appContextElementsToAdd = XmlUtils.findElements("/configuration/typicalsecurity/applicationContext/*", configuration);
    	
    	String springAppContext = pathResolver.getFocusedIdentifier(
				Path.SRC_MAIN_RESOURCES,
				"META-INF/spring/applicationContext.xml");

		MutableFile mutableConfigXml = null;
		Document webConfigDoc = null;
		try {
			if (fileManager.exists(springAppContext)) {
				mutableConfigXml = fileManager.updateFile(springAppContext);
				
				webConfigDoc = XmlUtils.getDocumentBuilder().parse(
						mutableConfigXml.getInputStream());
				Element rootElement = webConfigDoc.getDocumentElement();
				for (Element elementToAdd: appContextElementsToAdd) {
					Element importedElement = (Element)webConfigDoc.importNode(elementToAdd, true);
					webConfigDoc.adoptNode(importedElement);
					rootElement.appendChild(importedElement);
				}
				XmlUtils.writeXml(mutableConfigXml.getOutputStream(), webConfigDoc);
			} else {
				throw new IllegalStateException("Could not acquire "
						+ springAppContext);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		
	}

	/** {@inheritDoc} */
    public void annotateType(JavaType javaType) {
        // Use Roo's Assert type for null checks
        Validate.notNull(javaType, "Java type required");

        // Obtain ClassOrInterfaceTypeDetails for this java type
        ClassOrInterfaceTypeDetails existing = typeLocationService.getTypeDetails(javaType);

        // Test if the annotation already exists on the target type
        if (existing != null && MemberFindingUtils.getAnnotationOfType(existing.getAnnotations(), new JavaType(RooTypicalsecurity.class.getName())) == null) {
            ClassOrInterfaceTypeDetailsBuilder classOrInterfaceTypeDetailsBuilder = new ClassOrInterfaceTypeDetailsBuilder(existing);
            
            // Create JavaType instance for the add-ons trigger annotation
            JavaType rooRooTypicalsecurity = new JavaType(RooTypicalsecurity.class.getName());

            // Create Annotation metadata
            AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(rooRooTypicalsecurity);
            
            // Add annotation to target type
            classOrInterfaceTypeDetailsBuilder.addAnnotation(annotationBuilder.build());
            
            // Save changes to disk
            typeManagementService.createOrUpdateTypeOnDisk(classOrInterfaceTypeDetailsBuilder.build());
        }
    }

    /** {@inheritDoc} */
    public void annotateAll() {
        // Use the TypeLocationService to scan project for all types with a specific annotation
        for (JavaType type: typeLocationService.findTypesWithAnnotation(new JavaType("org.springframework.roo.addon.javabean.RooJavaBean"))) {
            annotateType(type);
        }
    }

	private void injectDatabasebasedSecurity() {
		// ----------------------------------------------------------------------
		// Run Security Setup Addon
		// ----------------------------------------------------------------------
		shell.executeCommand("security setup");

		// ----------------------------------------------------------------------
		// Copy DatabaseAuthenticationProvider from template
		// ----------------------------------------------------------------------
		createAuthenticationProvider();

		// ----------------------------------------------------------------------
		// Inject database based authentication provider into
		// applicationContext-security.xml
		// ----------------------------------------------------------------------
		injectDatabasebasedAuthProviderInXml();
		
		// ----------------------------------------------------------------------
		// Autowire MessageDigestPasswordEncoder in applicationContext.xml
		// ----------------------------------------------------------------------
//		autowireMessageDigestPasswordEncoder();

		createMailSender();
		addForgotPasswordRegisterUserToLoginPage();
		addChangePasswordToFooter();
		
	}

	private void addChangePasswordToFooter() {
		// Look for following in footer.jspx
		// <a href="${logout}">
		// <spring:message code="security_logout"/>
		// </a>
		// and append
		// <a href="changePassword/index">Change Password</a>

		String footerJspx = pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP,
				"WEB-INF/views/footer.jspx");

		MutableFile mutableFooterJspx = null;
		Document footerJspxDoc;
		try {
			if (fileManager.exists(footerJspx)) {
				mutableFooterJspx = fileManager.updateFile(footerJspx);
				footerJspxDoc = XmlUtils.getDocumentBuilder().parse(
						mutableFooterJspx.getInputStream());
				Element logout = XmlUtils.findFirstElement(
						"//a[@href=\"${logout}\"]",
						footerJspxDoc.getDocumentElement());
				Validate.notNull(logout,
						"Could not find <a href=\"${logout}\"> in "
								+ footerJspx);

				logout.getParentNode().appendChild(
						new XmlElementBuilder("div", footerJspxDoc).addChild(
								footerJspxDoc.createTextNode("|")).build());
//					String contextPath = projectOperations.getFocusedProjectName();
				String contextPath = projectOperations.getFocusedProjectName();
				logout.getParentNode().appendChild(
						new XmlElementBuilder("a", footerJspxDoc)
								.addAttribute(
										"href",
										"/" + contextPath
												+ "/changepassword/index")
								.addChild(
										footerJspxDoc
												.createTextNode("password"))
								.build());
				XmlUtils.writeXml(mutableFooterJspx.getOutputStream(),
						footerJspxDoc);

			} else {
				throw new IllegalStateException("Could not acquire "
						+ footerJspx);
			}
		} catch (Exception e) {
			System.out.println("---> " + e.getMessage());
			throw new IllegalStateException(e);
		}
		
	}

	private void addForgotPasswordRegisterUserToLoginPage() {
		// <div>
		// <a href ="/TypicalSecurity/forgotpassword/index">Forgot Password</a>
		// | Not a User Yet? <a href ="/TypicalSecurity/signup?form">Sign In</a>
		// </div>

		String loginJspx = pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP,
				"WEB-INF/views/login.jspx");

		MutableFile mutableLoginJspx = null;
		Document loginJspxDoc;
		try {
			if (fileManager.exists(loginJspx)) {
				mutableLoginJspx = fileManager.updateFile(loginJspx);
				loginJspxDoc = XmlUtils.getDocumentBuilder().parse(
						mutableLoginJspx.getInputStream());
				Element form = XmlUtils.findFirstElementByName("form",
						loginJspxDoc.getDocumentElement());
				Validate.notNull(form, "Could not find form in " + loginJspx);

				String contextPath = projectOperations.getFocusedProjectName();
				form.appendChild(new XmlElementBuilder("div", loginJspxDoc)
						.addChild(
								loginJspxDoc
										.createTextNode("<br/><a href =\"/"
												+ contextPath
												+ "/forgotpassword/index\">Forgot Password</a> | Not a User Yet? <a href =\"/"
												+ contextPath
												+ "/signup?form\">Sign Up</a>"))
						.build());
				XmlUtils.writeXml(mutableLoginJspx.getOutputStream(),
						loginJspxDoc);

			} else {
				throw new IllegalStateException("Could not acquire "
						+ loginJspx);
			}
		} catch (Exception e) {
			System.out.println("---> " + e.getMessage());
			throw new IllegalStateException(e);
		}
	}

	private void createMailSender() {
		shell.executeCommand("email sender setup --hostServer smtp.gmail.com --port 587 --protocol SMTP --username rohitsghatoltest@gmail.com --password password4me");
		shell.executeCommand("email template setup --from rohitsghatoltest@gmail.com --subject PasswordRecovery");
	}

//	private void autowireMessageDigestPasswordEncoder() {
//		String applicationContext = pathResolver.getFocusedIdentifier(
//				Path.SRC_MAIN_RESOURCES,
//				"META-INF/spring/applicationContext.xml");
//
//		MutableFile mutableConfigXml = null;
//		Document webConfigDoc;
//		try {
//			if (fileManager.exists(applicationContext)) {
//				mutableConfigXml = fileManager.updateFile(applicationContext);
//				webConfigDoc = XmlUtils.getDocumentBuilder().parse(
//						mutableConfigXml.getInputStream());
//			} else {
//				throw new IllegalStateException("Could not acquire "
//						+ applicationContext);
//			}
//		} catch (Exception e) {
//			throw new IllegalStateException(e);
//		}
//
//		Element messageDigestPasswordEncoder = new XmlElementBuilder("bean", webConfigDoc)
//			.addAttribute("id", "messageDigestPasswordEncoder")
//			.addAttribute("class", "org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder")
//			.addChild(
//				new XmlElementBuilder("constructor-arg", webConfigDoc)
//						.addAttribute("value", "sha-256").build()).build();
//		
//		webConfigDoc.getDocumentElement().appendChild(messageDigestPasswordEncoder);
//		
//		XmlUtils.writeXml(mutableConfigXml.getOutputStream(), webConfigDoc);
//		
//	}

	private void injectDatabasebasedAuthProviderInXml() {
		String springSecurity = pathResolver.getFocusedIdentifier(
				Path.SRC_MAIN_RESOURCES,
				"META-INF/spring/applicationContext-security.xml");

		MutableFile mutableConfigXml = null;
		Document webConfigDoc;
		try {
			if (fileManager.exists(springSecurity)) {
				mutableConfigXml = fileManager.updateFile(springSecurity);
				webConfigDoc = XmlUtils.getDocumentBuilder().parse(
						mutableConfigXml.getInputStream());
			} else {
				throw new IllegalStateException("Could not acquire "
						+ springSecurity);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

		Element firstInterceptUrl = XmlUtils.findFirstElementByName(
				"intercept-url", webConfigDoc.getDocumentElement());
		Validate.notNull(firstInterceptUrl, "Could not find intercept-url in "
				+ springSecurity);

		firstInterceptUrl.getParentNode().insertBefore(
				new XmlElementBuilder("intercept-url", webConfigDoc)
						.addAttribute("pattern", "/admin/**")
						.addAttribute("access", "hasRole('ROLE_ADMIN')").build(),
				firstInterceptUrl);

		firstInterceptUrl.getParentNode().insertBefore(
				new XmlElementBuilder("intercept-url", webConfigDoc)
						.addAttribute("pattern", "/")
						.addAttribute("access", "isAuthenticated()").build(),
				firstInterceptUrl);
		
		JavaPackage topLevelPackage = projectOperations.getFocusedTopLevelPackage();
		
		String authenticationProviderClass = topLevelPackage
				.getFullyQualifiedPackageName()
				+ ".provider.DatabaseAuthenticationProvider";

		Element databaseAuthenticationProviderBean = new XmlElementBuilder(
				"beans:bean", webConfigDoc)
				.addAttribute("id", "databaseAuthenticationProvider")
				.addAttribute("class", authenticationProviderClass)
				.addChild(
						new XmlElementBuilder("beans:property", webConfigDoc)
								.addAttribute("name", "adminUser")
								.addAttribute("value", adminUsername).build())
				.addChild(
						new XmlElementBuilder("beans:property", webConfigDoc)
								.addAttribute("name", "adminPassword")
								.addAttribute("value", adminPassword).build())
				.build();

		Element authenticationManager = XmlUtils.findFirstElementByName(
				"authentication-manager", webConfigDoc.getDocumentElement());

		authenticationManager.getParentNode().insertBefore(
				databaseAuthenticationProviderBean, authenticationManager);

		Element oldAuthProvider = XmlUtils.findFirstElementByName(
				"authentication-provider", webConfigDoc.getDocumentElement());

		// <authentication-provider ref="databaseAuthenticationProvider" />

		Element newAuthProvider = new XmlElementBuilder(
				"authentication-provider", webConfigDoc).addAttribute("ref",
				"databaseAuthenticationProvider").build();
		
		authenticationManager.replaceChild(newAuthProvider, oldAuthProvider);

//		newAuthProvider.appendChild(
//				new XmlElementBuilder("password-encoder", webConfigDoc)
//					.addAttribute("hash", "sha-256").build());

		XmlUtils.writeXml(mutableConfigXml.getOutputStream(), webConfigDoc);
		
	}

	private void createAuthenticationProvider() {
		JavaPackage topLevelPackage = projectOperations.getFocusedTopLevelPackage();
		
		String packagePath = topLevelPackage.getFullyQualifiedPackageName().replace('.', separator);

		String finalEntityPackage = entityPackage.replace("~",
				topLevelPackage.getFullyQualifiedPackageName());

		String finalControllerPackage = controllerPackage.replace("~",
				topLevelPackage.getFullyQualifiedPackageName());

		Properties properties = new Properties();
		properties.put("__TOP_LEVEL_PACKAGE__",
				topLevelPackage.getFullyQualifiedPackageName());
		properties.put("__ENTITY_LEVEL_PACKAGE__", finalEntityPackage);
		properties.put("__CONTROLLER_PACKAGE__", finalControllerPackage);

		Map<String, String> map = new HashMap<String, String>();

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA,
				finalControllerPackage.replace('.', separator) + separator
						+ "ChangePasswordController.java"),
				"ChangePasswordController.java-template");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA,
				finalControllerPackage.replace('.', separator) + separator
						+ "ChangePasswordForm.java"),
				"ChangePasswordForm.java-template");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA,
				finalControllerPackage.replace('.', separator) + separator
						+ "ChangePasswordValidator.java"),
				"ChangePasswordValidator.java-template");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA,
				finalControllerPackage.replace('.', separator) + separator
						+ "SignUpController.java"),
				"SignUpController.java-template");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA,
				finalControllerPackage.replace('.', separator) + separator
						+ "UserRegistrationForm.java"),
				"UserRegistrationForm.java-template");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA,
				finalControllerPackage.replace('.', separator) + separator
						+ "SignUpValidator.java"),
				"SignUpValidator.java-template");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA,
				finalControllerPackage.replace('.', separator) + separator
						+ "ForgotPasswordController.java"),
				"ForgotPasswordController.java-template");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA,
				finalControllerPackage.replace('.', separator) + separator
						+ "ForgotPasswordForm.java"),
				"ForgotPasswordForm.java-template");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA, packagePath
				+ separator + "provider" + separator
				+ "DatabaseAuthenticationProvider.java"),
				"DatabaseAuthenticationProvider.java-template");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_JAVA,
				finalControllerPackage.replace('.', separator) + separator
						+ "UserController.java"),
				"UserController.java-template");

		String prefix = separator + "WEB-INF/views";

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "signup" + separator + "index.jspx"),
				"signup/index.jspx");
		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "signup" + separator + "thanks.jspx"),
				"signup/thanks.jspx");
		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "signup" + separator + "error.jspx"),
				"signup/error.jspx");
		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "signup" + separator + "views.xml"),
				"signup/views.xml");
		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "signup" + separator + "activated.jspx"),
				"signup/activated.jspx");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "forgotpassword" + separator + "index.jspx"),
				"forgotpassword/index.jspx");
		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "forgotpassword" + separator + "thanks.jspx"),
				"forgotpassword/thanks.jspx");
		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "forgotpassword" + separator + "views.xml"),
				"forgotpassword/views.xml");

		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "changepassword" + separator + "index.jspx"),
				"changepassword/index.jspx");
		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "changepassword" + separator + "thanks.jspx"),
				"changepassword/thanks.jspx");
		map.put(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, prefix
				+ separator + "changepassword" + separator + "views.xml"),
				"changepassword/views.xml");

		for (Entry<String, String> entry : map.entrySet()) {

			MutableFile mutableFile = null;

			String path = entry.getKey();
			String file = entry.getValue();
			try {

				if (fileManager.exists(path))
					mutableFile = fileManager.updateFile(path);
				else
					mutableFile = fileManager.createFile(path);
				
				
//				TokenReplacementFileCopyUtils.replaceAndCopy(
//						TemplateUtils.getTemplate(getClass(), file),
//						mutableFile.getOutputStream(), properties);
				StreamUtils.replaceAndCopy(
						FileUtils.getInputStream(getClass(), file),
						mutableFile.getOutputStream(), properties);

			} catch (IOException ioe) {
				throw new IllegalStateException(ioe);
			}
		}
		
	}

	private void insertI18nMessages() {
		String appPropertiesPath = pathResolver.getFocusedIdentifier(
				Path.SRC_MAIN_WEBAPP, "WEB-INF/i18n/application.properties");

		MutableFile mutableApplicationProperties = null;

		try {
			if (fileManager.exists(appPropertiesPath)) {
				mutableApplicationProperties = fileManager.updateFile(appPropertiesPath);
				String originalData = StreamUtils.convertStreamToString(mutableApplicationProperties
						.getInputStream());

				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
						mutableApplicationProperties.getOutputStream()));

				out.write(originalData);
				out.write("\n\n#typicalsecurity\n");
				out.write("label_com_training_spring_roo_model_user_id=Id\n");
				out.write("label_com_training_spring_roo_model_user_lastname=Last Name\n");
				out.write("label_com_training_spring_roo_model_user_failedloginattempts=Failed\n");
				out.write("label_com_training_spring_roo_model_user_password=Password\n");
				out.write("label_com_training_spring_roo_model_userstatusmodel_failedloginattempts=Failed Login Attempts\n");
				out.write("label_com_training_spring_roo_model_user_repeat_password=Repeat Password\n");
				out.write("label_com_training_spring_roo_model_user_version=Version\n");
				out.write("label_com_training_spring_roo_model_user_firstname=First Name\n");
				out.write("label_com_training_spring_roo_model_user_plural=Users\n");
				out.write("label_com_training_spring_roo_model_user=User\n");
				out.write("label_com_training_spring_roo_model_user_enabled=Enabled\n");
				out.write("label_com_training_spring_roo_model_user_repeatpassword=Repeat Password\n");
				out.write("label_com_training_spring_roo_model_user_locked=Locked\n");
				out.write("label_com_training_spring_roo_model_user_activationkey=Activation Key\n");
				out.write("label_com_training_spring_roo_model_user_emailaddress=Email Address\n");
				out.write("label_com_training_spring_roo_model_user_activationdate=Activation Date\n");

				out.close();
				
//				InputStream in = fileManager.getInputStream(appPropertiesPath);
//				OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(appPropertiesPath)));
//				Properties appProperties = new Properties();
//				appProperties.load(in);
//				appProperties.setProperty("label_com_training_spring_roo_model_user_id", "Id");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_lastname", "Last Name");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_failedloginattempts", "Failed");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_password", "Password");
//				appProperties.setProperty("label_com_training_spring_roo_model_userstatusmodel_failedloginattempts", "Failed Login Attempts");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_repeat_password", "Repeat Password");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_version", "Version");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_firstname", "First Name");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_plural", "Users");
//				appProperties.setProperty("label_com_training_spring_roo_model_user", "User");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_enabled", "Enabled");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_repeatpassword", "Repeat Password");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_locked", "Locked");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_activationkey", "Activation Key");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_emailaddress", "Email Address");
//				appProperties.setProperty("label_com_training_spring_roo_model_user_activationdate", "Activation Date");
//				appProperties.store(out, "typicalsecurity setup");
//				out.close();

			} else {
				throw new IllegalStateException("Could not acquire "
						+ appPropertiesPath);
			}
		} catch (Exception e) {
			System.out.println("---> " + e.getMessage());
			throw new IllegalStateException(e);
		}
		
		String msgPropertiesPath = pathResolver.getFocusedIdentifier(
				Path.SRC_MAIN_WEBAPP, "WEB-INF/i18n/messages.properties");

		MutableFile mutableMessagesProperties = null;

		try {
			if (fileManager.exists(msgPropertiesPath)) {
				mutableMessagesProperties = fileManager.updateFile(msgPropertiesPath);
				String originalData = StreamUtils.convertStreamToString(mutableMessagesProperties.getInputStream());

				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(mutableMessagesProperties.getOutputStream()));

				//change original values
				originalData = originalData.replace("security_login_form_name=Name", "security_login_form_name=Email");
				originalData = originalData.replace("security_login_form_name_message=Enter your name", "security_login_form_name_message=Log in using your email address");
				
				
				out.write(originalData);
				out.write("\n\n#typicalsecurity\n");
				out.write("typicalsecurity_validate_signup_email_subject=Validate your account with {0}\n");
				out.write("typicalsecurity_forgotpassword_email_subject=New password requested for {0}\n");
				out.write("typicalsecurity_validate_signup_email_body=Hello, {0},\\nThanks for signing up for a new account with {1}.  Please click this link to activate your account:\\n{2}\n");
				out.write("typicalsecurity_forgotpassword_email_body=Hello, {0},\\nYou have requested that your password be reset for {1}.  Here is your new password:\\n{2}\\n\\nYou can log in at this address:\\n{3}\n");

//				InputStream in = fileManager.getInputStream(msgPropertiesPath);
//				OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(msgPropertiesPath)));
//				Properties msgProperties = new Properties();
//				msgProperties.load(in);
//				msgProperties.setProperty("typicalsecurity_validate_signup_email_subject", "Validate your account with {0}");
//				msgProperties.setProperty("typicalsecurity_forgotpassword_email_subject", "New password requested for {0}");
//				msgProperties.setProperty("typicalsecurity_validate_signup_email_body", "Hello, {0},\\nThanks for signing up for a new account with {1}.  Please click this link to activate your account:\\n{2}");
//				msgProperties.setProperty("typicalsecurity_forgotpassword_email_body", "Hello, {0},\\nYou have requested that your password be reset for {1}.  Here is your new password:\\n{2}\\n\\nYou can log in at this address:\\n{3}");
//				msgProperties.store(out, "typicalsecurity setup");
				out.close();

			} else {
				throw new IllegalStateException("Could not acquire "
						+ msgPropertiesPath);
			}
		} catch (Exception e) {
			System.out.println("---> " + e.getMessage());
			throw new IllegalStateException(e);
		}
		
	}

	private void createControllers() {
		
		// -----------------------------------------------------------------------------------
		// Controller for User
		// -----------------------------------------------------------------------------------
		shell.executeCommand("web mvc controller --class " + controllerPackage
				+ ".UserController");
		shell.executeCommand("web mvc scaffold --class " + controllerPackage
				+ ".UserController --backingType " + entityPackage
				+ ".User --path /admin/users");
		
		// -----------------------------------------------------------------------------------
		// Controller for Role
		// -----------------------------------------------------------------------------------
		shell.executeCommand("web mvc controller --class " + controllerPackage
				+ ".RoleController");
		shell.executeCommand("web mvc scaffold --class " + controllerPackage
				+ ".RoleController --backingType " + entityPackage
				+ ".Role --path /admin/roles");
		
		// -----------------------------------------------------------------------------------
		// Controller for User Role
		// -----------------------------------------------------------------------------------
		shell.executeCommand("web mvc controller --class " + controllerPackage
				+ ".UserRoleController");
		shell.executeCommand("web mvc scaffold --class " + controllerPackage
				+ ".UserRoleController --backingType " + entityPackage
				+ ".UserRole --path /admin/userRoles");
		
	}

	private void createUserRoleEntities() {
		// -----------------------------------------------------------------------------------
		// Create User entity
		// -----------------------------------------------------------------------------------
		shell.executeCommand("entity jpa --class " + entityPackage
				+ ".User --testAutomatically --permitReservedWords");
		shell.executeCommand("field string --fieldName firstName --sizeMin 1 --notNull");
		shell.executeCommand("field string --fieldName lastName --sizeMin 1 --notNull");
		shell.executeCommand("field string --fieldName emailAddress --sizeMin 1 --notNull --unique");
		shell.executeCommand("field string --fieldName password --sizeMin 1 --notNull");
		shell.executeCommand("field date --fieldName activationDate --type java.util.Date ");
		shell.executeCommand("field string --fieldName activationKey ");
		shell.executeCommand("field boolean --fieldName enabled ");
		shell.executeCommand("field boolean --fieldName locked ");

		// -----------------------------------------------------------------------------------
		// Create Role entity
		// -----------------------------------------------------------------------------------
		shell.executeCommand("entity jpa --class " + entityPackage
				+ ".Role --testAutomatically --permitReservedWords");
		shell.executeCommand("field string --fieldName roleName --sizeMin 1 --notNull --unique");
		shell.executeCommand("field string --fieldName roleDescription --sizeMin --sizeMax 200 --notNull");

		// -----------------------------------------------------------------------------------
		// Create User Role Mapping
		// -----------------------------------------------------------------------------------
		shell.executeCommand("entity jpa --class " + entityPackage
				+ ".UserRole --testAutomatically");
		shell.executeCommand("field reference --fieldName userEntry --type "
				+ entityPackage + ".User --notNull");
		shell.executeCommand("field reference --fieldName roleEntry --type "
				+ entityPackage + ".Role --notNull");

		// -----------------------------------------------------------------------------------
		// Create Finders for find user by email address and find user role by
		// user
		// -----------------------------------------------------------------------------------
		shell.executeCommand("finder add findUsersByEmailAddress --class " + entityPackage
				+ ".User");
		shell.executeCommand("finder add findUsersByActivationKeyAndEmailAddress --class " + entityPackage
				+ ".User");
		shell.executeCommand("finder add findUserRolesByUserEntry --class " + entityPackage
				+ ".UserRole");
		
	}
	
	public static String sha256(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
    
   
}