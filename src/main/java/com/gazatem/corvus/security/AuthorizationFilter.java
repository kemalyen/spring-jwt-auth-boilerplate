package com.gazatem.corvus.security;

import com.gazatem.corvus.entities.User;
import com.gazatem.corvus.repositories.UserRepository;
import com.gazatem.corvus.services.RoleService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import java.util.ArrayList;
import java.util.Collection;

import static com.gazatem.corvus.config.SecurityConstants.*;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    private RoleService roleService;

    public AuthorizationFilter(AuthenticationManager authManager, UserRepository userRepository, RoleService roleService) {
        super(authManager);
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            System.out.println("if (header == null || !header.startsWith(TOKEN_PREFIX))");
            chain.doFilter(req, res);
            return;
        }
        System.out.println(header);
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Authentication authenticationn =
        // SecurityContextHolder.getContext().getAuthentication();
        // String currentPrincipalName = authenticationn.getName();
        // System.out.println(currentPrincipalName);

        // UserDetails userDetails =
        // this.userDetailsService.loadUserByUsername(currentPrincipalName);

        chain.doFilter(req, res);

    }

    private void checkUser(String username) {
        User currentUser = null;
        try {

            currentUser = userRepository.findByEmail(username);
        } catch (Exception e) {
            System.out.println(e);
        }

        String foundUsername = "";

        if (currentUser != null) {
            foundUsername = currentUser.getEmail();
        }
        System.out.println("AUTH foundUsername= " + foundUsername);
        if (foundUsername.equals(username)) {
            System.out.println("DB contains this user  (JWTAuthorizationFilter) ");

        } else
            System.out.println("DB does not contain this user (JWTAuthorizationFilter) ");
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {

            Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace("Bearer", "")).getBody();

            String user = claims.getSubject();

            // AppUser usr = (AppUser) userDetailsService.loadUserByUsername(user);
            // String role = claims.get("roles", String.class);
            // Collection<GrantedAuthority> authorityList =
            // AuthorityUtils.commaSeparatedStringToAuthorityList(role);
            try {
                User currentUser = userRepository.findByEmail(user);

                Collection<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
                authorityList = (Collection<GrantedAuthority>) roleService.getAuthorities(currentUser.getRoles());

                if (currentUser != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, authorityList);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return null;
        }
        return null;
    }
}
