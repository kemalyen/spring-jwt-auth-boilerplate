package com.gazatem.corvus.security;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationToken extends UsernamePasswordAuthenticationToken {

    public AuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
        super.setAuthenticated(false);
    }

    public AuthenticationToken(Object principal, Object credentials,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);

        super.setAuthenticated(true); // must use super, as we override
    }

}