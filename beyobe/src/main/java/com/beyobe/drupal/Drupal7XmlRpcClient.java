package com.beyobe.drupal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.beyobe.Constants;
import com.beyobe.domain.Participant;


/**
 * Testing XML-RPC client with Drupal7.
 * This example uses xml-rpc with methodology for calling remote drupal functionality
 * as presented by "Services 3.X"
 * <p/>
 * For Xml-Rpc I use "org.apache.xmlrpc.client" library with depandencies <a href="http://ws.apache.org/xmlrpc/client.html">http://ws.apache.org/xmlrpc/client.html</a>.
 */
public class Drupal7XmlRpcClient {

    /**
     * Name of remote functions used by this example
     */
    public static final String METHOD_SYSTEM_CONNECT = "system.connect";
    public static final String METHOD_USER_LOGOUT = "user.logout";
    public static final String METHOD_USER_LOGIN = "user.login";
    public static final String METHOD_USER_CREATE = "user.create";
    public static final String METHOD_FILE_SAVE = "file.save";
    

    private static Logger log = Logger.getLogger(Drupal7XmlRpcClient.class.getName());

//    /**
//     * Endpoint is defined within Drupal services module configuration in order to define
//     * a URL that is avilable for serving a specific set of service calls. See drupal
//     * documentation for "Services 3.X". <a href="http://drupal.org/node/783236">http://drupal.org/node/783236</a>
//     */
//    private final String endpointURL;

    /**
     * Xml-Rpc clinet object
     */
    private XmlRpcClient xmlRpcClient;

    /**
     * Cookie value create upon login. We provide it only as an info. User does not have to handle
     * setting of Cookie. Instead, the {@link #login} method do it upon successful login, and allow
     * any following service call, to be handled in the scope of the login user.
     *
     * @see #login(String, String)
     */
    private String cookie;
	private String appId;


    /**
     * Initialize the XmlRpcClient client with basic configuration of endpoint URL.
     * The serviceURL must be a valid URL.
     *
     * @param endpointURL the URL of the Drupal service's endpoint. example: http://www.example.com/drupal/test_endpoint
     * @throws java.net.MalformedURLException if the serviceURL is not a URL
     */
    public Drupal7XmlRpcClient(String appId) throws MalformedURLException {
    	this.appId = appId;
//        this.endpointURL = endpointURL;
        log.setLevel(Level.INFO);

        //create the Xml-Rpc clinet object.
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(Constants.BASEURL_DRUPAL));
        xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(config);
    }

    /**
     * Call system.connect
     *
     * @throws Exception if fail
     */
    public void connect() throws Exception {
        try {
            Map response = (Map) xmlRpcClient.execute(METHOD_SYSTEM_CONNECT, new Object[]{});
//            log.info("Connected to server using SessionID: " + response.get("sessid"));
        } catch (Exception x) {
            throw new Exception("cannot connect to " + Constants.BASEURL_DRUPAL + ": " + x.getMessage(), x);
        }
    }

    /**
     * Call user.login
     *
     * @param username user name
     * @param password password
     * @return Map with details of login user (response.get("user")) and login session-name   and session-id
     *         that are used internally to construct the Cookie used by following calls.
     * @throws Exception if operation fail
     */
    private Map login(String username, String password) throws Exception {
        // Add Login Paramaters
        Vector<Object> params = new Vector<Object>();
        params.add(username);
        params.add(password);
        Map response = (Map) xmlRpcClient.execute(METHOD_USER_LOGIN, params);

//        log.info(response.toString());
//        //The user.login call return two attributes in which we use to construct value for a "Cookie" header.
//        //With then set xmlRpcClient with new XmlRpcTransportFactory that set  'Cookie' header using the composed cookie value
//        cookie = response.get("session_name") + "=" + response.get("sessid");
//        XmlRpcTransportFactory factory = new XmlRpcSunHttpTransportFactory(xmlRpcClient) {
//            public XmlRpcTransport getTransport() {
//                return new XmlRpcSunHttpTransport(xmlRpcClient) {
//                    @Override
//                    protected void initHttpHeaders(XmlRpcRequest request) throws XmlRpcClientException {
//                        super.initHttpHeaders(request);
//                        setRequestHeader("Cookie", cookie);
//                    }
//                };
//            }
//        };
//        xmlRpcClient.setTransportFactory(factory);

        return response;
    }

    /**
     * Call user.logout
     *
     * @throws Exception if operatino fail
     */
    public void logout() throws Exception {
        Vector<Object> params = new Vector<Object>();
        xmlRpcClient.execute(METHOD_USER_LOGOUT, params);
        log.info("Logout Sucessfull");
    }

//    /**
//     * Call user.create that is equivalent to user.register.
//     * <p/>
//     * On the server side this call handle user regitration by emulating user's registration form.
//     * It means that basic fields 'name' , 'pass', 'mail' MUST be provided into and 'account' object
//     * as well as all other Required custom fields. In this case there aretwo such custom fields required
//     * per user registration: 'field_newsletter' and 'field_accept_terms'
//     * <p/>
//     * Important note: on Drupal to 'pass' value provided by form is relevant only when login user is "admin"
//     * by permissions or that user_email_verification is not set to TRUe
//     * <pre>
//     * Taken from "user.module" file:
//     * <p/>
//     * if (!variable_get('user_email_verification', TRUE) || $admin) {
//     * $pass = $form_state['values']['pass'];
//     * }
//     * </pre>
//     * <p/>
//     * In short! if you plan to set custom password by xml-rpc call, make sure that login user is admin.
//     *
//     * @param name  name
//     * @param pass  password
//     * @param email email
//     * @return Map with details of createduser
//     * @throws org.apache.xmlrpc.XmlRpcException
//     *          if operaton fail
//     */
//    public Map createUser(String name, String pass, String email) throws XmlRpcException {
//        HashMap<String, Object> account = new HashMap<String, Object>();
//        account.put("name", name);
//        account.put("pass", pass);
//        account.put("mail", email);
//        account.put("status", "1"); //indicate if user will become active or not
//        account.put("notify", "1"); //indicate ifnotification should be sent
//
//        account.put("field_newsletter", prepareCheckboxValueParam("1")); //this is custom field
//        account.put("field_accept_terms", prepareCheckboxValueParam("1")); //this is custom field
//
//        // Add Login Paramaters
//        Vector<Object> params = new Vector<Object>();
//        params.add(account);
//
//        log.info("Creating user");
//        return (Map) xmlRpcClient.execute(METHOD_USER_CREATE, params);
//    }

//    /**
//     * Checkbox value is set into 3-level associate array like data structure
//     * <p/>
//     * Example of what is expected in Drupal server-side for a custom field of type "checkbox"
//     * <pre>
//     * array(1) {
//     *      ["und"]=> array(1) {
//     *          [0]=> array(1) {
//     *              ["value"]=> int([value])
//     *          }
//     *      }
//     * }
//     * </pre>
//     *
//     * @param value the value to cset for the checkbox - for example "1" or "0" depanding on field definition
//     *              on the specific drupal applicationl
//     * @return Map of params constructed for checkbox field
//     */
//    public static HashMap<String, HashMap> prepareCheckboxValueParam(String value) {
//        HashMap<String, String> arr111 = new HashMap<String, String>();
//        arr111.put("value", value);
//
//        HashMap<String, HashMap> arr11 = new HashMap<String, HashMap>();
//        arr11.put("0", arr111);
//
//        HashMap<String, HashMap> arr1 = new HashMap<String, HashMap>();
//        arr1.put("und", arr11);
//        return arr1;
//    }


    /**
     * Testing flow:
     * 1. Connect <br/>
     * 2. Login <br/>
     * 3. Create user <br/>
     * 4. Logout <br/>
     *
     * @param args not used
     */
    public static void main(String[] args) {
        String adminName = "testuser";
        String adminPass = "password";

        try {
            Drupal7XmlRpcClient service = new Drupal7XmlRpcClient("Test");

            // 1) connect
            service.connect();

            // 2) login
            Map login = service.login(adminName, adminPass);
            String sessionid = (String)login.get("sessid");
            Map user = (Map) login.get("user");
            String email = (String)user.get("mail");
            Map roles = (Map)user.get("roles");
            log.info("Roles=" + roles + "; session=" + sessionid);
            // you can create "watch" on 'login' to see the details of login user (the admin user)

//            // 3) create new user
//            Map userData = service.createUser(newUserName, newUserPass, newUserEmail);
//            //you can create "watch" on 'userData' to see the "uid" of new created user

            // 4) logout
//            service.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Participant getParticipant(String username, String password) throws XmlRpcException {
		Vector<Object> params = new Vector<Object>();
        params.add(username);
        params.add(password);
        Map response = (Map) xmlRpcClient.execute(METHOD_USER_LOGIN, params);
        if (response==null) return null;
        Participant p = new Participant();
        p.setSessionDate(new Date());
        p.setSessionToken((String)response.get("sessid"));
        p.setUsername(username);
        String id = appId + "-" + response.get("uid");
        p.setId(id);
        Integer status = (Integer)response.get("status");
        p.setStatus(status);
        p.setEnabled(status != null && status ==1);
        p.setEmail(String.valueOf(response.get("mail")));
        Map roles = (Map)response.get("roles");
        Collection<String> userRoles = roles.values();
        p.setRoles(userRoles);
        return p;
	}

}