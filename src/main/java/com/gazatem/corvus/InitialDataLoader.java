package com.gazatem.corvus;

import com.gazatem.corvus.entities.Role;
import com.gazatem.corvus.entities.User;
import com.gazatem.corvus.repositories.RoleRepository;
import com.gazatem.corvus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Service
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        createRoleIfNotFound("ADMIN");
        Role adminRole = roleRepository.findByName("ADMIN");

        User user = new User();
        user.setEmail("scuddy@gazatem.com");
        user.setPassword(passwordEncoder.encode("passsword"));
        user.setCreationDate(new Date());
        user.setActive(false);
        user.setActivationDate(null);

        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);
        alreadySetup = true;
    }

    @Transactional
    private Role createRoleIfNotFound(
            String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name, name);

            roleRepository.save(role);
        }
        return role;
    }
}
