package com.gazatem.corvus.repositories;

import com.gazatem.corvus.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,String> {
    Role findByName(String name);
}
