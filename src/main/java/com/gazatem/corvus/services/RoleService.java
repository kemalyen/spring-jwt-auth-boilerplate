package com.gazatem.corvus.services;

import java.util.Collection;
import java.util.List;

import com.gazatem.corvus.entities.Role;
import org.springframework.security.core.GrantedAuthority;

public interface RoleService {

    Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles);

    List<String> getPrivileges(final Collection<Role> roles);

    List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges);

}
