package com.gazatem.corvus.services;

import com.gazatem.corvus.exceptions.EmailNotFoundException;
import com.gazatem.corvus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Service
// @Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private HttpServletRequest request;

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private final String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.gazatem.corvus.entities.User applicationUser = userRepository.findByEmail(email);
        if (applicationUser == null) {
            throw new EmailNotFoundException(email);
        }
        Collection<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
        return new User(applicationUser.getEmail(), applicationUser.getPassword(), true, true, true, true, authorityList);
    }
}
