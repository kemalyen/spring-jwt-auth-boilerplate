package com.gazatem.corvus.services;

import com.gazatem.corvus.entities.User;
import com.gazatem.corvus.exceptions.EmailExistsException;
import org.springframework.web.context.request.WebRequest;

public interface UserService {

    void registerUser(User user, WebRequest request) throws EmailExistsException;
}
