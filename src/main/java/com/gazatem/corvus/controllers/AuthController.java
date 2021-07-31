package com.gazatem.corvus.controllers;

import com.gazatem.corvus.services.UserServiceImp;
import com.gazatem.corvus.entities.User;
import com.gazatem.corvus.exceptions.EmailExistsException;
import com.gazatem.corvus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;

@RestController
public class AuthController {

    @Autowired
    UserServiceImp userService;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> registerUser(@RequestBody User user, WebRequest request) {

        try {
            userService.registerUser(user, request);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (EmailExistsException e) {

            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
            // System.out.println(e);
        }
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(currentPrincipalName);

        System.out.println(authentication.getDetails());
        System.out.println(authentication.getAuthorities());

        return "Welcome" + authentication.getDetails();
    }
}
