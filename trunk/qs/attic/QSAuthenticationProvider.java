package org.inqle.web.security;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.inqle.domain.security.Principal;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class QSAuthenticationProvider extends
AbstractUserDetailsAuthenticationProvider {

	private EntityManager em;
	
	private static Logger log = Logger.getLogger(QSAuthenticationProvider.class);
	
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        // TODO Auto-generated method stub

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String password = (String) authentication.getCredentials();
        if (!StringUtils.hasText(password)) {
            throw new BadCredentialsException("Please enter password");
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        try {
        	TypedQuery<Principal> accountQueries = Principal.findPrincipalsByUsernameEqualsAndPasswordEquals(username, password);
            List<Principal> accounts = accountQueries.getResultList();
        	if (accounts.size() == 0) {
            	throw new BadCredentialsException("Invalid username or password");
            }
            if (accounts.size() > 1) {
            	throw new BadCredentialsException("Non-unique user, contact administrator");
            }
            Principal account = accounts.get(0);
            log.info("got account: " + account);
        	authorities.add(new GrantedAuthorityImpl(account.getRole().toString()));
//            Role role = Role.findRoles
        } catch (EmptyResultDataAccessException e) {
            throw new BadCredentialsException("Invalid username or password");
        } catch (EntityNotFoundException e) {
            throw new BadCredentialsException("Invalid user");
        } catch (NonUniqueResultException e) {
            throw new BadCredentialsException("Non-unique user, contact administrator");
        }
        return new User(username, password, true, // enabled
                true, // account not expired
                true, // credentials not expired
                true, // account not locked
                authorities);
    }
}