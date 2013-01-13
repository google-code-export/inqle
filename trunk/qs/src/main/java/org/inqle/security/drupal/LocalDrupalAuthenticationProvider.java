package org.inqle.security.drupal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.NonUniqueResultException;
import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * Authenticates a user by Drupal.
 * <p/>
 * The authentication is done against the database table USERS and the
 * column NAME (username) and column PASS (MD5 password)
 * <p/>
 * Tested with Drupal 6
 *
 * @author martin.bergljung@ixxus.co.uk
 */
public class LocalDrupalAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    public static final String SQL_GET_USERID = "SELECT uid, mail FROM USERS WHERE NAME=? and PASS=?";
    public static final String SQL_GET_ROLES = "SELECT distinct r.role from roles r, users_roles ur " +
    		"where ur.rid = r.rid and ur.uid=?";
    public static final String MSG_LOGIN_FAILS = "Invalid username or password";

    private final Logger log = Logger.getLogger(LocalDrupalAuthenticationProvider.class);
    
    /**
     * Spring JDBC template used to query or update a JDBC data source
     */
    private JdbcTemplate jdbcTemplate;
    
//    private DataSource drupalDataSource;
    
    @Autowired
    public void setDrupalDataSource(DataSource drupalDataSource) {
        this.jdbcTemplate = new JdbcTemplate(drupalDataSource);
    }
    /**
     * Authenticate against the Drupal database
     *
     * @param userName the username to authenticate
     * @param password the password to authenticate (passed in as plain text)
     * @throws BadCredentialsException if authentication failed
     */
    protected int authenticate(String username, String hashedPassword) throws BadCredentialsException {
        // Generate an MD5 hash for the password as that is what we get back from Drupal
        // Get the value as hex
       

        log.info("About to authenticate user: " + username + " with SHA-256 password: " + hashedPassword);

        try {
            Integer userId = jdbcTemplate.queryForInt(SQL_GET_USERID, username, hashedPassword);

            log.info("Got userid from Drupal database: " + userId);
            return userId;
        } catch (DataAccessException dae) {
        	String errMessage = "Unable to get password from Drupal database for user: " + username + ".  User may or may not exist in the Drupal database";
        	log.error(errMessage, dae);
            throw new BadCredentialsException(errMessage);
        }
    }
    
    /**
     * Authenticate against the Drupal database
     *
     * @param userName the username to authenticate
     * @param password the password to authenticate (passed in as plain text)
     * @throws BadCredentialsException if authentication failed
     */
    protected List<String> getRoles(int userId) {
        log.info("Looking up roles for user: " + userId);
        List<String> roles = new ArrayList<String>();
        try {
            List<Map<String, Object>> rolesMap = jdbcTemplate.queryForList(SQL_GET_ROLES, userId);
            for (Map<String, Object> roleMap: rolesMap) {
            	roles.add((String)roleMap.get("role"));
            }
        } catch (DataAccessException dae) {
        	String errMessage = "Unable to get roles from Drupal database for user: " + userId;
        	log.error(errMessage, dae);
            throw new BadCredentialsException(errMessage);
        }
        return roles;
    }

	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0,
			UsernamePasswordAuthenticationToken arg1)
			throws org.springframework.security.core.AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws org.springframework.security.core.AuthenticationException {
		String password = (String) authentication.getCredentials();
		 String hashedPassword = DigestUtils.sha512Hex(password);
        if (!StringUtils.hasText(password)) {
            throw new BadCredentialsException("Please enter password");
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        Integer userId = authenticate(username, hashedPassword);
    	if (userId==null || userId < 0) {
    		throw new BadCredentialsException(MSG_LOGIN_FAILS);
    	}
        log.info("got userId: " + userId);
        List<String> roles = getRoles(userId);
        for (String role: roles) {
        	authorities.add(new GrantedAuthorityImpl(role));
        }
        return new User(username, hashedPassword, true, // enabled
                true, // account not expired
                true, // credentials not expired
                true, // account not locked
                authorities);
	}
}