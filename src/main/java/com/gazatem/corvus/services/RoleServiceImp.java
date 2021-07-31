package com.gazatem.corvus.services;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gazatem.corvus.entities.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImp implements RoleService {

    @Override
    public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    @Override
    public final List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> collection = new ArrayList<String>();

        for (final Role role : roles) {
            collection.add(role.getName());
        }

        return collection;
    }

    @Override
    public final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
