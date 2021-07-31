package com.gazatem.corvus.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import  com.gazatem.corvus.entities.Role;
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String username;

    @NotEmpty
    @NotNull
    private String email;

    @NotNull
    private String password;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Temporal(TemporalType.DATE)
    private Date activationDate;

    private Boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String getUsername()
    {
        return email;
    }

    public void setUsername(String email)
    {
        this.username = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
