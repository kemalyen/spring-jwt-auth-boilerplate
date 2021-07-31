package com.gazatem.corvus.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.gazatem.corvus.config.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.gazatem.corvus.config.SecurityConstants.*;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            com.gazatem.corvus.entities.User creds = new ObjectMapper().readValue(req.getInputStream(), com.gazatem.corvus.entities.User.class);
            String email = creds.getEmail();

            AuthenticationToken authRequest = new AuthenticationToken(creds.getUsername(), creds.getPassword());
            setDetails(req, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
         /*   Authentication authenticated =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
                    creds.getPassword(), new ArrayList<>()));
            return authenticated;*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        User user = (User) auth.getPrincipal();

        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
//		claims.put("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));


        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);


//		String token = Jwts.builder().setSubject(((User) auth.getPrincipal()).getUsername())
//				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
//		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);



        System.out.println("Bearer token is: " + token);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"" + SecurityConstants.HEADER_STRING + "\":\"" + SecurityConstants.TOKEN_PREFIX+token + "\"}"
        );
    }
}