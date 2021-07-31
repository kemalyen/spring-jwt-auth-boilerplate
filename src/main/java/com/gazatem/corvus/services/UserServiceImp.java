package com.gazatem.corvus.services;

import com.gazatem.corvus.entities.User;
import com.gazatem.corvus.exceptions.EmailExistsException;
import com.gazatem.corvus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Date;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void registerUser(User user, WebRequest request) throws EmailExistsException {

        if (doesEmailExist(user.getEmail())) {
            // System.out.println("Existed email");
            throw new EmailExistsException(user.getEmail());
        }else {

            user.setPassword(passwordEncoder().encode(user.getPassword()));
            user.setCreationDate(new Date());
            user.setActive(false);
            user.setActivationDate(null);
            // user.setRoles(RolesEnum.ROLE_USER.name());
            // user.setRoles(roleRepository.findByRoleName("User"));
            // user.setRoles(Arrays.asList(roleRepository.findByName("ADMIN_ROLE")));
            userRepository.save(user);
            // eventPublisher.publishEvent(new
            // OnRegistrationCompleteEvent(user));
            String appUrl = request.getContextPath();
            System.out.println("Registered!");
        }
    }
        public boolean doesEmailExist(String email) {
            User user = userRepository.findByEmail(email);

            boolean doesUserExistByEmail = user != null;
            return doesUserExistByEmail;
        }
}
