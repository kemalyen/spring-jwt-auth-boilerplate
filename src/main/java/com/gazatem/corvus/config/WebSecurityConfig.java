package com.gazatem.corvus.config;

import com.gazatem.corvus.providers.UserDetailsAuthenticationProvider;
import com.gazatem.corvus.security.AuthenticationFilter;
import com.gazatem.corvus.security.AuthorizationFilter;
import com.gazatem.corvus.services.RoleService;
import com.gazatem.corvus.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gazatem.corvus.repositories.UserRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers( "/", "hello", "/register", "/confirm/*", "/h2/**", "/resend/*", "/me","/resetpassword/*","/resetpasswordform/*").permitAll()
                // .antMatchers("/test").hasRole("USER_ROLE")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                .addFilter(new AuthorizationFilter(authenticationManager(), userRepository, roleService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //headers
                .headers()
                .defaultsDisabled()
                .cacheControl()
                .and()
                .contentTypeOptions()
                .and()
                .contentSecurityPolicy("default-src 'none'")
                .and()
                .xssProtection().block(true)
                .and()
                .httpStrictTransportSecurity()
                .and()
                .frameOptions().deny();
    }


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(failureHandler());
        return filter;
    }

    public AuthenticationProvider authProvider() {
        UserDetailsAuthenticationProvider provider
                = new UserDetailsAuthenticationProvider(passwordEncoder, userDetailsService);
        return provider;
    }


    @Bean
    public org.springframework.security.web.authentication.AuthenticationFailureHandler failureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}