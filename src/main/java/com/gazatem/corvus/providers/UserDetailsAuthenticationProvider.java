package com.gazatem.corvus.providers;

import com.gazatem.corvus.services.UserDetailsService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gazatem.corvus.services.UserDetailsServiceImpl;
import org.springframework.util.Assert;


public class UserDetailsAuthenticationProvider  extends AbstractUserDetailsAuthenticationProvider {
    /**
     * The plaintext password used to perform
     * PasswordEncoder#matches(CharSequence, String)}  on when the user is
     * not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    /**
     * The password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is
     * not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private String userNotFoundEncodedPassword;

    public UserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
        this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
        UserDetails loadedUser;

        try {
            loadedUser = this.userDetailsService.loadUserByUsername(auth.getPrincipal()
                    .toString());
        } catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials()
                        .toString();
                passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
            }
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, "
                    + "which is an interface contract violation");
        }
        return loadedUser;
    }
}
