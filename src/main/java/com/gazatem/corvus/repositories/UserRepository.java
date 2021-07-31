package com.gazatem.corvus.repositories;

import com.gazatem.corvus.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,String> {

    User findByEmail(String email);
}